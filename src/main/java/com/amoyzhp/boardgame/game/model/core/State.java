package com.amoyzhp.boardgame.game.model.core;

import com.amoyzhp.boardgame.game.model.common.Player;
import com.amoyzhp.boardgame.game.model.common.Position;

import java.util.List;
import java.util.Set;

/**
 * game state interface
 *
 * @Author: Tuseday Boy
 * @CreatedDate: 2020/3/6
 */
public interface State {

    boolean updateState(Position positions, Player player);

    Player getPlayerOnPos(Position position);

    void reset();

    boolean isTerminal();

    void setTerminal(boolean terminal);

    Set<Position> getEmptyPos();

    boolean isLegalPos(Position position, Player player);

    int[][] getChessboard();

    int getWidth();

    int getHeight();

}
