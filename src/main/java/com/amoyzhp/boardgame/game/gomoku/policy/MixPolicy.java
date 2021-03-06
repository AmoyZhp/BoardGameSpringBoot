package com.amoyzhp.boardgame.game.gomoku.policy;

import com.amoyzhp.boardgame.dto.gomoku.DebugInfo;
import com.amoyzhp.boardgame.game.gomoku.component.GomokuActionsGenerator;
import com.amoyzhp.boardgame.game.gomoku.component.GomokuSimulator;
import com.amoyzhp.boardgame.game.gomoku.enums.GomokuPlayer;
import com.amoyzhp.boardgame.game.model.policy.Policy;
import com.amoyzhp.boardgame.game.model.common.Player;
import com.amoyzhp.boardgame.game.model.core.Action;
import com.amoyzhp.boardgame.game.model.core.State;
import com.amoyzhp.boardgame.game.model.policy.TreeSearchPolicy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedList;
import java.util.List;


/**
 * 将多个策略结合到同一个策略中调用
 *
 * @Author: House Zam
 * @CreatedDate: 2020/04/21
 */
public class MixPolicy implements Policy {

    final Logger logger = LoggerFactory.getLogger(MixPolicy.class);
    // -1 就是没有限制
    private final long DEFAULT_TIME_LIMIT = -1;

    private GomokuActionsGenerator actionsGenerator;
    private GomokuSimulator simulator;
    private TreeSearchPolicy basicTreeSearchPolicy;
    private ThreatSpaceSearch threatSpaceSearch;

    private long timeLimit;
    private long beginTime;
    private long currentTime;

    private MixPolicy(){

    }

    public MixPolicy(GomokuActionsGenerator actionsGenerator, TreeSearchPolicy treeSearchPolicy, ThreatSpaceSearch threatSpaceSearch){
        this.simulator = null;
        this.actionsGenerator = actionsGenerator;
        this.basicTreeSearchPolicy = treeSearchPolicy;
        this.threatSpaceSearch = threatSpaceSearch;
    }

    @Override
    public Action getAction(State state, Player player){
        return this.getAction(state, player, null);
    }

    public Action getAction(State state, Player player, DebugInfo debugInfo) {
        if(debugInfo != null){
            this.timeLimit = debugInfo.getTimeLimit();
            this.beginTime = System.currentTimeMillis();
        } else{
            this.timeLimit = this.DEFAULT_TIME_LIMIT;
        }
        logger.info("total time limit " + this.timeLimit);

        if (this.simulator == null){
            this.simulator = new GomokuSimulator(state);
        } else {
            this.simulator.setState(state);
        }

        GomokuPlayer currentPlayer = GomokuPlayer.paraseValue(player.getValue());
        GomokuPlayer nextPlayer = GomokuPlayer.getNextPlayer(player);
        Action action = null;
        List<Action> actions = new LinkedList<>();
        // 判断我方是否有必胜点

        actions = this.actionsGenerator.getKillAction(simulator.getRoadBoard(), currentPlayer);
        if (actions != null && actions.size() > 0){
            return actions.get(0);
        }

        // 否则判断对方有没有必胜点
        actions = this.actionsGenerator.getKillAction(simulator.getRoadBoard(), nextPlayer);
        if (actions != null && actions.size() > 0){
            return actions.get(0);
        }

        if(debugInfo != null){
            this.currentTime = System.currentTimeMillis();
            // 如果 timeLimit 等于 -1 表示没有超时限制
            if(this.timeLimit >= 0){
                this.timeLimit = Math.max(0, this.timeLimit - (currentTime - beginTime));
            }
            action = this.threatSpaceSearch.getAction(simulator.getGameState(), currentPlayer, debugInfo.getTssDepth(), this.timeLimit);
            if (action != null){
                return action;
            }

            // 判断对方可否通过 TSS 获胜
            this.currentTime = System.currentTimeMillis();
            if(this.timeLimit >= 0){
                this.timeLimit = Math.max(0, this.timeLimit - (currentTime - beginTime));
            }
            action = this.threatSpaceSearch.getAction(simulator.getGameState(), nextPlayer, debugInfo.getTssDepth(),this.timeLimit);
            if (action != null){
                return action;
            }

            this.currentTime = System.currentTimeMillis();
            if(this.timeLimit >= 0){
                this.timeLimit = Math.max(0, this.timeLimit - (currentTime - beginTime));
            }

            action = this.basicTreeSearchPolicy.getAction(simulator.getGameState(), currentPlayer, debugInfo.getAbDepth(), this.timeLimit);
        } else {

            // 判断我方可否通过 TSS 获胜
            action = this.threatSpaceSearch.getAction(simulator.getGameState(), currentPlayer);
            if (action != null){
                return action;
            }

            // 判断对方可否通过 TSS 获胜
            action = this.threatSpaceSearch.getAction(simulator.getGameState(), nextPlayer);
            if (action != null){
                return action;
            }

            // 上述都无法找到理想点的话
            // 使用 alpha-beta 剪枝
            action = this.basicTreeSearchPolicy.getAction(simulator.getGameState(), currentPlayer);
        }

        return action;
    }
}
