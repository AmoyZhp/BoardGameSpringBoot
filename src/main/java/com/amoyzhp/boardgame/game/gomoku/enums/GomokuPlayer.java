package com.amoyzhp.boardgame.game.gomoku.enums;

import com.amoyzhp.boardgame.game.model.common.Player;

/**
 * gomoku player
 *
 * @Author: Tuseday Boy
 * @CreatedDate: 2020/03/09
 */
public enum GomokuPlayer implements Player {
    ILLEGAL(-1),
    EMPTY(0),
    BLACK(1),
    WHITE(2);

    private int value;

    GomokuPlayer(int value){
        this.value = value;
    }

    public static GomokuPlayer paraseValue(int value){
        if(value == EMPTY.value){
            return EMPTY;
        } else if(value == BLACK.value){
            return BLACK;
        } else if(value == WHITE.value){
            return WHITE;
        }
        return ILLEGAL;
    }

    public static GomokuPlayer getNextPlayer(Player player){
        if(player.getValue() == WHITE.getValue()){
            return BLACK;
        } else if(player.getValue() == BLACK.getValue()){
            return WHITE;
        } else {
            return ILLEGAL;
        }
    }
    @Override
    public boolean equals(Player player){
        if(player.getValue() == this.getValue()){
            return true;
        } else {
            return false;
        }
    }

    @Override
    public int getValue() {
        return this.value;
    }
}
