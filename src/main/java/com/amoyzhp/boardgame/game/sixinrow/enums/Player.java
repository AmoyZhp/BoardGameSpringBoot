package com.amoyzhp.boardgame.game.sixinrow.enums;

public enum Player {
    EMPTY(0),
    BLACK(1),
    WHITE(2);
    private int value;
    Player(int value){
        this.value = value;
    }
    public Player paraseValue(int value){
        if(value == EMPTY.value){
            return EMPTY;
        } else if(value == BLACK.value){
            return BLACK;
        } else if(value == WHITE.value){
            return WHITE;
        }
        return EMPTY;
    }
    public int getValue(){
        return this.value;
    }
}
