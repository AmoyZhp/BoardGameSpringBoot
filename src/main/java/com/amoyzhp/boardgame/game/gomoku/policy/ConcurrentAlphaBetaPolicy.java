package com.amoyzhp.boardgame.game.gomoku.policy;

import com.amoyzhp.boardgame.game.gomoku.component.GomokuActionsGenerator;
import com.amoyzhp.boardgame.game.gomoku.component.GomokuEvaluator;
import com.amoyzhp.boardgame.game.gomoku.component.GomokuSimulator;
import com.amoyzhp.boardgame.game.gomoku.core.GomokuState;
import com.amoyzhp.boardgame.game.gomoku.enums.GomokuPlayer;
import com.amoyzhp.boardgame.game.model.common.Player;
import com.amoyzhp.boardgame.game.model.core.Action;
import com.amoyzhp.boardgame.game.model.core.State;
import com.amoyzhp.boardgame.game.model.policy.TreeSearchPolicy;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;

/**
 * concurrent version of alpha beta policy
 *
 * @Author: Tuseday Boy
 * @CreatedDate: 2020/05/13
 */
public class ConcurrentAlphaBetaPolicy  implements TreeSearchPolicy {

    private final int DEFAULT_DEPTH = 1;
    // -1 就是没有限制
    private final long DEFAULT_TIME_LIMIT = -1;

    final Logger logger = LoggerFactory.getLogger(ConcurrentAlphaBetaPolicy.class);

    private GomokuActionsGenerator actionsGenerator;

    private GomokuSimulator simulator;

    private GomokuEvaluator evaluator;

    private Map<Integer, Integer> translationTable;

    private long beginTime;
    private volatile long currentTime;
    private long timeLimit;

    public ConcurrentAlphaBetaPolicy(GomokuActionsGenerator actionsGenerator, GomokuEvaluator evaluator) {
        this.actionsGenerator = actionsGenerator;
        this.evaluator = evaluator;
        this.translationTable = new ConcurrentHashMap<>();
    }

    public Action getAction(State state, Player player){
        return this.getAction(state, player, this.DEFAULT_DEPTH, this.DEFAULT_TIME_LIMIT);
    }

    @Override
    public Action getAction(State state, Player player, int depth, long timeLimit){
        if(depth <= 0){
            depth = 1;
        }
        this.timeLimit = timeLimit;
        if(this.timeLimit < 0){
            this.timeLimit = -1;
        }
        if(this.simulator == null){
            this.simulator = new GomokuSimulator(state);
        } else {
            this.simulator.setState(state);
        }
        this.beginTime = System.currentTimeMillis();

        int initStateHashcode = simulator.getGameState().hashCode();
        Action action = null;
        // 获取用来进行搜索的候选点
        List<Action> candidateActions = this.actionsGenerator.getAlphaBetaCandidateActions(this.simulator.getRoadBoard(), player);
        List<ActionNode> selectedActionNodes = new ArrayList<>(candidateActions.size());
        List<Future<ActionNode>> results = new ArrayList<>();
        logger.info("ab depth is " + depth);
        logger.info("time limit" + this.timeLimit);
        for(Action act : candidateActions){
            this.simulator.step(act);
            SubAlphaBetaTreeSearch subTree = new SubAlphaBetaTreeSearch(this.simulator, player, act, depth-1);
            this.simulator.moveBack();
            FutureTask<ActionNode> task = new FutureTask<>(subTree);
            results.add(task);
            Thread thread = new Thread(task);
            thread.start();
        }
        for(Future<ActionNode> res : results){
            try{
                selectedActionNodes.add(res.get());
            } catch (Exception e){
                e.printStackTrace();
            }
        }
        // 降序排序， val 值大在前面
        selectedActionNodes.sort((node1, node2) -> node1.getVal() - node2.getVal());
        // 选择分数最高的 action
        if(selectedActionNodes.size() > 0){
            action = selectedActionNodes.get(0).getAction();
        }
        assert  initStateHashcode == simulator.getGameState().hashCode();
        this.currentTime = System.currentTimeMillis();
        logger.info("concurrent AB search time is " + (currentTime - beginTime) / 1000.0 + " s ");
        return  action;
    }


    private class SubAlphaBetaTreeSearch implements Callable<ActionNode>{

        private GomokuSimulator simulator;

        private Player player;

        private Action action;

        private int depth;

        public SubAlphaBetaTreeSearch(GomokuSimulator simulator, Player player, Action action, int depth) {
            this.simulator = new GomokuSimulator(GomokuState.copyState(simulator.getGameState()));
            this.player = player;
            this.action = action;
            this.depth = depth;
        }

        @Override
        public ActionNode call() throws Exception {
            int beta = Integer.MAX_VALUE;
            int alpha = Integer.MIN_VALUE;
            Player nextPlayer = GomokuPlayer.getNextPlayer(player);
            int val = alphaBetaTreeSearch(alpha, beta, this.depth, nextPlayer, this.player);
            return new ActionNode(action, val);
        }

        public int alphaBetaTreeSearch(int alpha, int beta, int depth, Player player, Player requiredPlayer) {
            if(depth == 0){
                return evaluator.evaluate(this.simulator.getRoadBoard(), player);
            }
            long currentTime;
            if(timeLimit >= 0){
                currentTime = System.currentTimeMillis();
                if(currentTime - beginTime >= timeLimit){
                    return evaluator.evaluate(this.simulator.getRoadBoard(), player);
                }
            }

            Player nextPlayer = GomokuPlayer.getNextPlayer(player);
            List<Action> candidateActions = actionsGenerator.getAlphaBetaCandidateActions(this.simulator.getRoadBoard(),
                    player);
            for(Action action : candidateActions){
                this.simulator.step(action);
                int temp = translationTable.getOrDefault(this.simulator.getGameState().hashCode(), -1);
                if(temp == -1){
                    temp = alphaBetaTreeSearch(alpha, beta, depth-1, nextPlayer, requiredPlayer);
                    translationTable.put(this.simulator.getGameState().hashCode(), temp);
                } else {
//                    logger.info(" translation table shot");
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
