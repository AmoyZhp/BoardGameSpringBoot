package com.amoyzhp.boardgame.game.gomoku.policy;

import com.amoyzhp.boardgame.game.gomoku.component.GomokuActionsGenerator;
import com.amoyzhp.boardgame.game.gomoku.component.GomokuEvaluator;
import com.amoyzhp.boardgame.game.gomoku.component.GomokuSimulator;
import com.amoyzhp.boardgame.game.gomoku.enums.GomokuPlayer;
import com.amoyzhp.boardgame.game.model.component.Simulator;
import com.amoyzhp.boardgame.game.model.policy.Policy;
import com.amoyzhp.boardgame.game.model.common.Player;
import com.amoyzhp.boardgame.game.model.core.Action;
import com.amoyzhp.boardgame.game.model.core.State;
import com.amoyzhp.boardgame.game.model.policy.TreeSearchPolicy;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * implementaion of alpha beta tree search in gomoku
 *
 * @Author: Tuseday Boy
 * @CreatedDate: 2020/03/30
 */
public class AlphaBetaPolicy implements TreeSearchPolicy {

    private final int DEFAULT_DEPTH = 1;
    // -1 就是没有限制
    private final long DEFAULT_TIME_LIMIT = -1;

    final Logger logger = LoggerFactory.getLogger(AlphaBetaPolicy.class);

    private GomokuActionsGenerator moveGenerator;

    private GomokuSimulator simulator;

    private GomokuEvaluator evaluator;

    private Map<Integer, Integer> translationTable;

    private long beginTime;
    private long currentTime;
    private long timeLimit;

    private AlphaBetaPolicy(){

    }

    public AlphaBetaPolicy(GomokuActionsGenerator actionsGenerator, GomokuEvaluator evaluator){
        this.moveGenerator = actionsGenerator;
        this.evaluator = evaluator;
        this.translationTable = new HashMap<>();
    }

    @Override
    public Action getAction(State state, Player player) {
        return this.getAction(state, player, this.DEFAULT_DEPTH, this.DEFAULT_TIME_LIMIT);
    }

    @Override
    public Action getAction(State state, Player player, int depth, long timeLimit) {

        // 如果 depth 小于等于 0 则强制等于 1
        // depth = 0 没有实际意义
        depth = depth <= 0 ? 1 : depth;
        // 如果 timeLimit < 0 则让它等于 -1。即无超时限制。
        this.timeLimit = timeLimit < 0 ? -1 : timeLimit;

        if(this.simulator == null){
            this.simulator = new GomokuSimulator(state);
        } else {
            this.simulator.setState(state);
        }


        this.beginTime = System.currentTimeMillis();
        this.translationTable = new HashMap<>((int)Math.pow(10, depth));
        Action action = null;
        int beta = Integer.MAX_VALUE;
        int alpha = Integer.MIN_VALUE;
        int nextPlayer = GomokuPlayer.getNextPlayer(player).getValue();
        // 获取用来进行搜索的候选点
        List<Action> candidateActions = this.moveGenerator.getAlphaBetaCandidateActions(this.simulator.getRoadBoard(),
                GomokuPlayer.paraseValue(player.getValue()));
        List<ActionNode> selectedActionNodes = new ArrayList<>(candidateActions.size());
        for(Action act : candidateActions){
            this.simulator.step(act);
            int temp = this.alphaBetaTreeSearch(alpha, beta, depth-1, nextPlayer, player.getValue());
            this.simulator.moveBack();
            selectedActionNodes.add(new ActionNode(act, temp));
        }

        // 降序排序， val 值大在前面
        selectedActionNodes.sort((node1, node2) -> node1.getVal() - node2.getVal());

        // 选择分数最高的 action
        if(selectedActionNodes.size() > 0){
            action = selectedActionNodes.get(0).getAction();
        }
        this.currentTime = System.currentTimeMillis();
        logger.info("AB search time is " + (currentTime - beginTime) / 1000.0 + " s ");
        return  action;

    }

    public int alphaBetaTreeSearch(int alpha, int beta, int depth, int player, int requiredPlayer) {
        if(depth == 0){
            return this.evaluator.evaluate(this.simulator.getRoadBoard(), GomokuPlayer.paraseValue(player));
        }
        if(timeLimit >= 0){
            this.currentTime = System.currentTimeMillis();
            if(this.currentTime - this.beginTime >= this.timeLimit){
                return this.evaluator.evaluate(this.simulator.getRoadBoard(), GomokuPlayer.paraseValue(player));
            }
        }

        int nextPlayer = GomokuPlayer.getNextPlayer(GomokuPlayer.paraseValue(player)).getValue();
        List<Action> candidateActions = this.moveGenerator.getAlphaBetaCandidateActions(this.simulator.getRoadBoard(),
                GomokuPlayer.paraseValue(player));
        for(Action action : candidateActions){
            this.simulator.step(action);
            int temp = this.translationTable.getOrDefault(this.simulator.getGameState().hashCode(), -1);
            if(temp == -1){
                temp = alphaBetaTreeSearch(alpha, beta, depth-1, nextPlayer, requiredPlayer);
                this.translationTable.put(this.simulator.getGameState().hashCode(), temp);
            }
            this.simulator.moveBack();
            if(requiredPlayer == player){
                alpha = Math.max(temp, alpha);
                if(alpha > beta){
                    break;
                }
            } else {
                beta = Math.min(temp, beta);
                if(beta < alpha){
                    break;
                }
            }
        }
        if(requiredPlayer == player) {
            // 是极大层
            return alpha;
        } else {
            // 是极小层
            return beta;
        }
    }

    @Data
    private class ActionNode {
        private Action action;
        private int val;

        public ActionNode(Action action, int val){
            this.action = action;
            this.val = val;
        }
    }
}
