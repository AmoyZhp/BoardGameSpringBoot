package com.amoyzhp.boardgame.game.model.common;

/**
 * direction of road
 *
 * @Author: Tuseday Boy
 * @CreatedDate: 2020/03/06
 */
public enum Direction {
    HORIZONTAL(0,0,1),
    VERTICAL(1,1,0),
    LEADING_DIAGONAL(2,1,1),
    DEPUTY_DIAGONAL(3,1,-1)
    ;
    private int value;
    private int rowOffset;
    private int colOffset;
    Direction(int value, int rowOffset, int colOffset){
        this.value = value;
        this.rowOffset = rowOffset;
        this.colOffset = colOffset;
    }

    public int getValue(){
        return this.value;
    }

    public int rowOffset(){
        return this.rowOffset;
    }

    public int colOffset(){
        return this.colOffset;
    }


}
