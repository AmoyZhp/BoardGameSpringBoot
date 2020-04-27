package com.amoyzhp.boardgame.game.model.policy;

import com.amoyzhp.boardgame.game.model.common.Player;
import com.amoyzhp.boardgame.game.model.core.Action;
import com.amoyzhp.boardgame.game.model.core.State;

/**
 * policy interface
 *
 * @Author: Tuseday Boy
 * @CreatedDate: 2020/04/25
 */
public interface Policy {

    Action getAction(State state, Player player);
}
