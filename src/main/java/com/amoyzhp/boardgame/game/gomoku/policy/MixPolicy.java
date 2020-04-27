package com.amoyzhp.boardgame.game.gomoku.policy;

import com.amoyzhp.boardgame.game.gomoku.component.GomokuActionsGenerator;
import com.amoyzhp.boardgame.game.gomoku.component.GomokuSimulator;
import com.amoyzhp.boardgame.game.gomoku.enums.GomokuPlayer;
import com.amoyzhp.boardgame.game.model.policy.Policy;
import com.amoyzhp.boardgame.game.model.common.Player;
import com.amoyzhp.boardgame.game.model.core.Action;
import com.amoyzhp.boardgame.game.model.core.State;

import java.util.List;


/**
 * 将多个策略结合到同一个策略中调用
 *
 * @Author: House Zam
 * @CreatedDate: 2020/04/21
 */
public class MixPolicy implements Policy {

    private GomokuActionsGenerator actionsGenerator;
    private GomokuSimulator simulator;
    private AlphaBetaPolicy alphaBetaPolicy;
    private ThreatSpaceSearch threatSpaceSearch;

    public MixPolicy(){
        this.simulator = null;
        this.actionsGenerator = new GomokuActionsGenerator();
        this.alphaBetaPolicy = new AlphaBetaPolicy();
        this.threatSpaceSearch = new ThreatSpaceSearch();
    }

    public Action getAction(State state, Player player) {
        if (this.simulator == null){
            this.simulator = new GomokuSimulator(state);
        }

        GomokuPlayer currentPlayer = GomokuPlayer.paraseValue(player.getValue());
        GomokuPlayer nextPlayer = GomokuPlayer.getNextPlayer(player.getValue());
        Action action = null;
        List<Action> actions = null;
        // 判断我方是否有必胜点

        actions = this.actionsGenerator.getKillAction(simulator, currentPlayer);
        if (actions != null && actions.size() > 0){
            return actions.get(0);
        }

        // 否则判断对方有没有必胜点
        actions = this.actionsGenerator.getKillAction(simulator, nextPlayer);
        if (actions != null && actions.size() > 0){
            return actions.get(0);
        }

        // 判断我方可否通过 TSS 获胜
        action = this.threatSpaceSearch.getAction(simulator, currentPlayer);
        if (action != null){
            return action;
        }

        // 判断对方可否通过 TSS 获胜
        action = this.threatSpaceSearch.getAction(simulator, nextPlayer);
        if (action != null){
            return action;
        }

        // 上述都无法找到理想点的话
        // 使用 alpha-beta 剪枝
        action = this.alphaBetaPolicy.getAction(simulator, currentPlayer);

        return action;
    }
}
