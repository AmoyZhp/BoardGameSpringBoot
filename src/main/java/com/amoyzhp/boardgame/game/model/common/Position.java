package com.amoyzhp.boardgame.game.model.common;

import java.util.Objects;

/**
 * chess position
 *
 * @Author: Tuseday Boy
 * @CreatedDate: 2020/03/09
 */
public class Position {
    private int row;
    private int col;
    public Position(int row, int col){
        this.row = row;
        this.col = col;
    }
    public int row(){
        return this.row;
    }

    public int col(){
        return this.col;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Position position = (Position) o;
        return row == position.row &&
                col == position.col;
    }

    @Override
    public int hashCode() {
        return 10000 + row * 100 + col;
    }
}
