package com.amoyzhp.boardgame.game.model.policy;

import com.amoyzhp.boardgame.game.model.common.Player;
import com.amoyzhp.boardgame.game.model.core.Action;
import com.amoyzhp.boardgame.game.model.core.State;

/**
 * tree search policy interface
 *
 * @Author: Tuseday Boy
 * @CreatedDate: 2020/05/15
 */
public interface TreeSearchPolicy extends Policy {

    Action getAction(State state, Player player, int depth, long timeLimit);

}
