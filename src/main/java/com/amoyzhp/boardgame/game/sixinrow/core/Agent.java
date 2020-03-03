package com.amoyzhp.boardgame.game.sixinrow.core;

import com.amoyzhp.boardgame.game.sixinrow.constant.GameConst;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Agent {
    final Logger logger = LoggerFactory.getLogger(Agent.class);
    private Policy policy;
    private int player;

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

    public void setPlayer(int player){
        if(player != GameConst.BLACK && player != GameConst.WHITE){
            logger.debug("illegal player {}",player);
        }
        this.player = player;
    }

    public int getPlayer(){
        return this.player;
    }

}
