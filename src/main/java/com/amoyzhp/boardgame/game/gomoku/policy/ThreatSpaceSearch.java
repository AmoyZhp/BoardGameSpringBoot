package com.amoyzhp.boardgame.game.gomoku.policy;

import com.amoyzhp.boardgame.game.gomoku.component.GomokuSimulator;
import com.amoyzhp.boardgame.game.gomoku.enums.GomokuPlayer;
import com.amoyzhp.boardgame.game.model.common.Player;
import com.amoyzhp.boardgame.game.model.core.Action;
import com.amoyzhp.boardgame.game.model.core.State;
import com.amoyzhp.boardgame.game.model.policy.Policy;

/**
 * 胁迫搜索
 *
 * @Author: Tuseday Boy
 * @CreatedDate: 2020/04/21
 */
public class ThreatSpaceSearch implements Policy {


    public Action getAction(GomokuSimulator simulator, GomokuPlayer player) {
        return null;
    }

    @Override
    public Action getAction(State state, Player player) {
        return null;
    }
}
