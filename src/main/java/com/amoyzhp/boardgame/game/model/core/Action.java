package com.amoyzhp.boardgame.game.model.core;

import com.amoyzhp.boardgame.game.model.common.Player;
import com.amoyzhp.boardgame.game.model.common.Position;

import java.util.List;

/**
 * Agent action
 *
* @Author: Tuseday Boy
* @CreatedDate: 2020/3/6
*/
public interface Action {

    Player getPlayer();

    List<Position> getPositions();
}
