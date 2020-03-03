package com.amoyzhp.boardgame.game.sixinrow.constant;

import com.amoyzhp.boardgame.game.sixinrow.enums.PlayerSide;

public class GameConst {
    public static final int BOARD_SIZE = 19;
    public static final int BLACK = PlayerSide.BLACK.getValue();
    public static final int WHITE = PlayerSide.WHITE.getValue();
    public static final int EMPTY = PlayerSide.EMPTY.getValue();
    // 四个方向分别是
    // 横向 : x + 0 , y + 1
    // 纵向 : x + 1, y + 0
    // 135度 : x + 1, y + 1
    // 45度角 : x - 1, y + 1
    public static final int[][] DIRECTIONS = {{0,1},{1,0},{1,1},{-1,1}};

}
