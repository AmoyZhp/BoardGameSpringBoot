package com.amoyzhp.boardgame.game.sixinrow.enums;

public enum PlayerSide {
    EMPTY(0),
    BLACK(1),
    WHITE(2);
    private int value;

    PlayerSide(int value){
        this.value = value;
    }
    public int getValue(){
        return this.value;
    }
}
