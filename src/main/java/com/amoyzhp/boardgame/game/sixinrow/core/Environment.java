package com.amoyzhp.boardgame.game.sixinrow.core;

import lombok.Data;

@Data
public class Environment {
    private GameState gameState;

    public Environment(){

    }

    public void init(){
        this.gameState = new GameState();
        this.gameState.init();
    }

    public GameState step(Action act){
        this.gameState.setPos(act.getX1(), act.getY1(), act.getPlayer());
        if(this.gameState.getTimestep() > 0){
            this.gameState.setPos(act.getX2(), act.getY2(), act.getPlayer());
        }
        this.gameState.addActionsInHistory(act);
        this.gameState.setTimestep(this.gameState.getTimestep() + 1);
        return this.gameState;
    }

    public void reset(){

    }

    public void close(){

    }

}
