package com.amoyzhp.boardgame.game.sixinrow.enums;

public enum Player {
    ILLEGAL(-1),
    EMPTY(0),
    BLACK(1),
    WHITE(2);

    private int value;

    Player(int value){
        this.value = value;
    }

    public static Player paraseValue(int value){
        if(value == EMPTY.value){
            return EMPTY;
        } else if(value == BLACK.value){
            return BLACK;
        } else if(value == WHITE.value){
            return WHITE;
        }
        return ILLEGAL;
    }

    public static Player getNextPlayer(Player player){
        assert (player == WHITE || player == BLACK);
        if(player == WHITE){
            return BLACK;
        } else if(player == BLACK){
            return WHITE;
        } else {
            return ILLEGAL;
        }
    }

    public int getValue(){
        return this.value;
    }
}
