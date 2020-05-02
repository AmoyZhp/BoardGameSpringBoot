package com.amoyzhp.boardgame.game.gomoku.policy;

import com.amoyzhp.boardgame.game.gomoku.component.GomokuActionsGenerator;
import com.amoyzhp.boardgame.game.gomoku.component.GomokuEvaluator;
import com.amoyzhp.boardgame.game.gomoku.component.GomokuSimulator;
import com.amoyzhp.boardgame.game.gomoku.enums.GomokuPlayer;
import com.amoyzhp.boardgame.game.model.policy.Policy;
import com.amoyzhp.boardgame.game.model.common.Player;
import com.amoyzhp.boardgame.game.model.core.Action;
import com.amoyzhp.boardgame.game.model.core.State;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * implementaion of alpha beta tree search in gomoku
 *
 * @Author: Tuseday Boy
 * @CreatedDate: 2020/03/30
 */
public class AlphaBetaPolicy implements Policy {

    private final int DEFAULT_DEPTH = 1;
    // -1 就是没有限制
    private final long DEFAULT_TIME_LIMIT = -1;

    final Logger logger = LoggerFactory.getLogger(AlphaBetaPolicy.class);

    private GomokuActionsGenerator moveGenerator;

    private GomokuSimulator simulator;

    private GomokuEvaluator evaluator;

    private long beginTime;
    private long currentTime;
    private long timeLimit;

    public AlphaBetaPolicy(){
        this.moveGenerator = new GomokuActionsGenerator();
        this.evaluator = new GomokuEvaluator();
    }

    public Action getAction(GomokuSimulator simulator, GomokuPlayer player){
        return this.getAction(simulator, player, this.DEFAULT_DEPTH, this.DEFAULT_TIME_LIMIT);
    }

    public Action getAction(GomokuSimulator simulator, GomokuPlayer player, int depth, long timeLimit) {
        if(depth <= 0){
            depth = 1;
        }
        if(this.timeLimit < 0){
            this.timeLimit = -1;
        }
        else {
            this.timeLimit = timeLimit;
        }

        this.beginTime = System.currentTimeMillis();

        this.simulator = simulator;
        int initStateHashcode = simulator.getGameState().hashCode();
        Action action = null;
        int beta = Integer.MAX_VALUE;
        int alpha = Integer.MIN_VALUE;

        int nextPlayer = GomokuPlayer.getNextPlayer(player.getValue()).getValue();

        // 获取用来进行搜索的候选点
        List<Action> candidateActions = this.moveGenerator.getAlphaBetaCandidateActions(this.simulator, player);
        List<ActionNode> selectedActionNodes = new ArrayList<>(candidateActions.size());
        logger.info("ab depth is " + depth);
        logger.info("time limit" + this.timeLimit);
        for(Action act : candidateActions){
            int hash = this.simulator.getGameState().hashCode();
            this.simulator.step(act);
            int temp = this.alphaBetaTreeSearch(alpha, beta, depth-1, nextPlayer, player.getValue());
            this.simulator.moveBack();
            if(hash != this.simulator.getGameState().hashCode()){
                logger.info("zobrist hash code error");
            }
            selectedActionNodes.add(new ActionNode(act, temp));
        }

        // 降序排序， val 值大在前面
        selectedActionNodes.sort((node1, node2) -> node1.getVal() - node2.getVal());

        // 选择分数最高的 action
        if(selectedActionNodes.size() > 0){
            action = selectedActionNodes.get(0).getAction();
        }
        assert  initStateHashcode == simulator.getGameState().hashCode();
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

        int nextPlayer = GomokuPlayer.getNextPlayer(player).getValue();
        List<Action> candidateActions = this.moveGenerator.getAlphaBetaCandidateActions(this.simulator,
                GomokuPlayer.paraseValue(player));
        for(Action action : candidateActions){
            this.simulator.step(action);
            int temp = alphaBetaTreeSearch(alpha, beta, depth-1, nextPlayer, requiredPlayer);
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

    @Override
    public Action getAction(State state, Player player) {
        return null;
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
