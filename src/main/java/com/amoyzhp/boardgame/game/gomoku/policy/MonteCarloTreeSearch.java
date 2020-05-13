package com.amoyzhp.boardgame.game.gomoku.policy;


import com.amoyzhp.boardgame.game.gomoku.component.GomokuActionsGenerator;
import com.amoyzhp.boardgame.game.gomoku.component.GomokuEvaluator;
import com.amoyzhp.boardgame.game.gomoku.component.GomokuSimulator;
import com.amoyzhp.boardgame.game.gomoku.component.GomokuSimulatorLight;
import com.amoyzhp.boardgame.game.gomoku.core.GomokuState;
import com.amoyzhp.boardgame.game.gomoku.enums.GomokuPlayer;
import com.amoyzhp.boardgame.game.model.common.Player;
import com.amoyzhp.boardgame.game.model.component.Simulator;
import com.amoyzhp.boardgame.game.model.core.Action;
import com.amoyzhp.boardgame.game.model.core.State;

import java.util.*;

/**
 * implementation of monte carlo tree search
 *
 * @Author: Tuseday Boy
 * @CreatedDate: 2020/05/03
 */
public class MonteCarloTreeSearch {

    private final long MAX_TIME_LIMIT = 5 * 1000; // 5000 ms  = 5s

    // timeLimit = -1 表示没有限时
    private final long DEFAULT_TIME_LIMIT = -1;

    private final int DEFAULT_SIMULATION_COUNT = 1600;

    private final int MAX_SIMULATION_COUNT = 1600;

    private long timeLimit;
    private int simulationCount;

    private GomokuActionsGenerator actionsGenerator;

    private RandomPolicy randomPolicy;

    public MonteCarloTreeSearch(){
        this.actionsGenerator = new GomokuActionsGenerator();
        this.randomPolicy = new RandomPolicy();
    }

    public TreeNode selection(TreeNode root){
        while(root.getHighestChild() != null){
            root = root.getHighestChild();
        }
        return root;
    };

    public TreeNode expansion(TreeNode parent, GomokuPlayer player){
        // 需要先利用 simulator 的路数组选点
        GomokuSimulator simulator = new GomokuSimulator(GomokuState.copyState(parent.getState()));
        GomokuPlayer actingPlayer = parent.getPlayer();
        GomokuPlayer nextPlayer = GomokuPlayer.getNextPlayer(player);

        List<Action> actionList = actionsGenerator.getKillAction(simulator, actingPlayer);
        if(actionList.size() == 0){
            actionList= actionsGenerator.getKillAction(simulator, nextPlayer);
        }

        if(actionList.size() == 0){
            actionList= actionsGenerator.getMCTSCandidateActions(simulator.getRoadBoard(), actingPlayer);
        }

        List<TreeNode> children = new ArrayList<>(actionList.size());
        // 因为不需要路这么复杂的结构了所以使用 light sim
        GomokuSimulatorLight lightSim = new GomokuSimulatorLight(GomokuState.copyState(parent.getState()));
        for(Action action : actionList){
            lightSim.step(action);
            State nextState = GomokuState.copyState(simulator.getGameState());
            int win = 0;
            if(nextState.isTerminal() && nextState.getEmptyPos().size() > 0){
                // 如果当前行动的是调用方并且到达的终点，说明我方胜利了
                if(player.getValue() == actingPlayer.getValue()){
                    win = 1;
                } else {
                    win = -1;
                }
            } else {
                win = 0;
            }
            TreeNode child = new TreeNode(nextState, nextPlayer, action, parent, win);
            children.add(child);
            lightSim.moveBack();
        }
        parent.setChildren(children);
        return parent.getHighestChild();

    }

    public int simulation(TreeNode node, GomokuPlayer player){
        if(node.getWin() != 0){
            // 说明该节点是一个终局节点
            return node.getWin();
        }
        GomokuSimulatorLight simulator = new GomokuSimulatorLight(GomokuState.copyState(node.getState()));
        GomokuPlayer actingPlayer = node.getPlayer();

        while (simulator.getGameState().isTerminal() == false){
            Action action = randomPolicy.getAction(simulator.getGameState(), actingPlayer);
            simulator.step(action);
            actingPlayer = GomokuPlayer.getNextPlayer(actingPlayer);
        }
        int win = 0;
        if(simulator.getGameState().getEmptyPos().size() == 0){
            win = 0;
        } else if(simulator.getLastAction().getPlayer().getValue() == player.getValue()){
            win = 1;
        } else {
            win = -1;
        }
        return win;
    }

    public void backpropagation(TreeNode node, int win){
        node.backpropagationUpdate(win);
    }

    public Action getAction(GomokuSimulator simulator, GomokuPlayer player){
        return this.getAction(simulator, player,  DEFAULT_TIME_LIMIT, DEFAULT_SIMULATION_COUNT);
    }

    public Action getAction(GomokuSimulator simulator, GomokuPlayer player, long timeLimit, int simulationCount){
        this.timeLimit = timeLimit;
        if(this.timeLimit < 0 ){
            // timeLimit = -1 表示没有限时
            this.timeLimit = DEFAULT_TIME_LIMIT;
        } else {
            this.timeLimit = Math.max(MAX_TIME_LIMIT, this.timeLimit);
        }
        this.simulationCount = simulationCount;
        if(this.simulationCount < 0){
            this.simulationCount = DEFAULT_SIMULATION_COUNT;
        } else {
            this.simulationCount = Math.max(MAX_SIMULATION_COUNT, this.simulationCount);
        }
        TreeNode root = new TreeNode(simulator.getGameState(), player,null,null, 0);
        for(int i = 0; i < this.simulationCount; i++){
            TreeNode selctionNode = this.selection(root);
            TreeNode expansionNode = this.expansion(selctionNode, player);
            int val = this.simulation(expansionNode, player);
            this.backpropagation(expansionNode, val);
        }
        TreeNode child = root.getHighestChild();

        return child.getActionPathFromParent();
    }

    private class TreeNode{

        private PriorityQueue<TreeNode> children;
        private double val;
        private Action actionPathFromParent;
        private TreeNode parent;
        // 当前状态下，下一个行动的玩家
        private GomokuPlayer player;
        private State state;
        private int visitCnt;
        private int winCnt;
        // 标记该点是否有获胜者
        // 0 表示没有
        // 1 表示调用方获胜
        // -1 表示调用方对手获胜
        private int win;

        public TreeNode(State gameState, GomokuPlayer player, Action action,  TreeNode parent, int win) {
            this.state = gameState;
            this.actionPathFromParent = action;
            this.player = player;
            this.parent = parent;
            this.children = null;
            this.val = Integer.MAX_VALUE;
            this.visitCnt = 0;
            this.winCnt = 0;
            this.win = win;
        }

        public TreeNode getHighestChild() {
            if(children == null){
                return null;
            } else {
                return children.peek();
            }
        }

        public Action getActionPathFromParent() {
            return this.actionPathFromParent;
        }

        public State getState() {
            return this.state;
        }

        public void setChildren(List<TreeNode> children) {
            this.children = new PriorityQueue<>(children.size(), new Comparator<TreeNode>() {
                @Override
                public int compare(TreeNode o1, TreeNode o2) {
                    if(o1.getVal() > o2.getVal()){
                        return -1;
                    } else {
                        return 1;
                    }
                }
            });
            this.children.addAll(children);
        }

        public void resortChildren(TreeNode node){
            this.children.remove(node);
            this.children.offer(node);
        }
        public double getVal() {
            return val;
        }

        public TreeNode getParent() {
            return parent;
        }

        public GomokuPlayer getPlayer(){
            return this.player;
        }

        public int getWin(){
            return this.win;
        }

        public int getVisitCnt(){
            return this.visitCnt;
        }

        public void backpropagationUpdate(int win) {
            this.visitCnt += 1;
            if(win == 1){
                this.winCnt += 1;
            }
            if(this.parent != null){
                this.parent.backpropagationUpdate(-win);
                this.val = this.winCnt * 1.0 / this.visitCnt + Math.sqrt(1.96*Math.log(this.parent.getVisitCnt()) / this.visitCnt);
                // 此时子节点的值已经发生了变化，需要重新排序
                this.parent.resortChildren(this);
            }
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            TreeNode node = (TreeNode) o;
            return Objects.equals(state.hashCode(), node.state.hashCode());
        }

        @Override
        public int hashCode() {
            return state.hashCode();
        }
    }
}
