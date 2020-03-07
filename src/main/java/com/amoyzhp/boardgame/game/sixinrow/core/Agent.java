package com.amoyzhp.boardgame.game.sixinrow.core;

import com.amoyzhp.boardgame.game.sixinrow.constant.GameConst;
import com.amoyzhp.boardgame.game.sixinrow.enums.Player;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Agent {
    final Logger logger = LoggerFactory.getLogger(Agent.class);
    private Policy policy;
    private Player player;

    public void init(){

    }

    public Action act(GameState gameState){
        Action action;
        policy = new Policy();
        this.policy.init();
        if(gameState.getTimestep() <= 1){
            action = this.policy.getFirstMove(gameState, this.player);
        } else {
            action = this.policy.alphaBetaTreeSearchBegin(gameState, this.player);
        }
        action.setPlayer(this.player);
        return action;
    }

    public void setPlayer(Player player){
        if(player != Player.BLACK && player != Player.WHITE){
            logger.debug("illegal player {}",player);
        }
        this.player = player;
    }

    public Player getPlayer(){
        return this.player;
    }

}
