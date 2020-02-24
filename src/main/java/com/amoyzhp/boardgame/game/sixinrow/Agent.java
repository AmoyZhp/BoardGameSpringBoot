package com.amoyzhp.boardgame.game.sixinrow;

import com.amoyzhp.boardgame.game.sixinrow.enums.PlayerSide;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

public class Agent {

    private Policy policy;
    private int player;

    public void init(){

    }

    public Action act(GameState gameState){
        Action action;
        policy = new Policy();
        action = this.policy.random(gameState, this.player);
        action.setPlayer(this.player);
        return action;
    }

    public Action act(GameState gameState,SixInRowEnv simulator){
        Action action;
        policy = new Policy();
        action = this.policy.random(gameState, this.player);
        action.setPlayer(this.player);
        return action;
    }

    public void setPlayer(int player){
        this.player = player;
    }

    public int getPlayer(){
        return this.player;
    }

}
