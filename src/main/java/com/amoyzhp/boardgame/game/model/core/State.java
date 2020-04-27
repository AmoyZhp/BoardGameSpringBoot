package com.amoyzhp.boardgame.game.model.core;

import com.amoyzhp.boardgame.game.model.common.Player;
import com.amoyzhp.boardgame.game.model.common.Position;

import java.util.List;

/**
 * game state interface
 *
 * @Author: Tuseday Boy
 * @CreatedDate: 2020/3/6
 */
public interface State {

    boolean updateState(Position positions, Player player);

    void reset();

    boolean isTerminal();

    boolean isLegalPos(Position position, Player player);

    int[][] getChessboard();

    int getWidth();

    int getHeight();

}
