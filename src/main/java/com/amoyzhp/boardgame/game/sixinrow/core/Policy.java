package com.amoyzhp.boardgame.game.sixinrow.core;

import com.amoyzhp.boardgame.game.sixinrow.component.*;
import com.amoyzhp.boardgame.game.sixinrow.constant.GameConst;
import com.amoyzhp.boardgame.game.sixinrow.core.Action;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Policy {

    private MoveGenerator moveGenerator;

    private Simulator simulator;

    private Evaluator evaluator;

    public Policy(){

    }

    public void init(){
        this.moveGenerator = new MoveGenerator();
        this.evaluator = new Evaluator();
        this.simulator = new Simulator();
    }

    public Action getFirstMove(GameState gameState, int player){
        Action action = new Action();
        if(player == GameConst.BLACK){
            action.setX1(9);
            action.setY1(9);
        } else if(player == GameConst.WHITE){
            Action lastAction = gameState.getHistoryActions().getLast();
            int x = lastAction.getX1();
            int y = lastAction.getY1();
            //对方的第一个落子是不是在一个理想的中心区域
            if(x <= 10 && x >= 8 && y >= 8 && y <= 10){
                action.setX1(x+1);
                action.setY1(y);
                action.setX2(x);
                action.setY2(y+1);
            } else {
                //不是则我方落在中心区域
                action.setX1(9);
                action.setY1(9);
                action.setX2(10);
                action.setY2(10);
            }
        }
        return action;
    }

    // requiredPlayer 是需要为其寻找行动的玩家
    // player 表示当前层数"行动"的玩家，用来区分极大层还是极小层
    public int alphaBetaTreeSearch(int alpha, int beta, int depth,
                                   int player, int requiredPlayer){
        if(depth == 0){
            return this.evaluator.evaluate(this.simulator.getRoadBoard());
        }
        int nextPlayer;
        if(player == GameConst.BLACK){
            nextPlayer = GameConst.WHITE;
        } else {
            nextPlayer = GameConst.BLACK;
        }
        List<Action> candiacateActions = this.moveGenerator.getCandidateAction(
                this.simulator.getGameState(),this.simulator.getRoadBoard(), player);

        for(Action action : candiacateActions){
            this.simulator.step(action);
            int temp = alphaBetaTreeSearch(alpha, beta, depth, nextPlayer, requiredPlayer);
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
            return alpha;
        } else {
            return beta;
        }
    }

    public Action alphaBetaTreeSearchBegin(GameState gameState, int player){
        // 初始化模拟器
        this.simulator.setGameState(gameState);
        Action result = new Action();
        List<ActionNode> actionList = new ArrayList<>();
        int beta = Integer.MAX_VALUE;
        int alpha = Integer.MIN_VALUE;
        int nextPlayer;
        int depth = 1;
        if(player == GameConst.BLACK){
            nextPlayer = GameConst.WHITE;
        } else {
            nextPlayer = GameConst.BLACK;
        }
        ActionNode actionNode;
        List<Action> candiacateActions = this.moveGenerator.getCandidateAction(
                this.simulator.getGameState(),
                this.simulator.getRoadBoard(),player);
        // depth 是奇数时在 max 层终结
        // 偶数时在 min 层终结
        RoadBoard roadBoard = this.simulator.getRoadBoard();
        int initHashCode = this.simulator.getRoadBoard().hashCode();
        for (Action action : candiacateActions){
            this.simulator.step(action);
            int temp = this.alphaBetaTreeSearch(alpha, beta, depth-1, nextPlayer, player);
            this.simulator.moveBack();
            if(initHashCode != this.simulator.getRoadBoard().hashCode()){
                System.out.println("hash code 不一致");
            }
            actionNode = new ActionNode(action, temp);
            actionList.add(actionNode);
        }
        //降序排序，val值大的在前面
        actionList.sort((node1, node2) -> node1.getVal() - node2.getVal());
        // 分数最高的 action
        if(actionList.size() > 0){
            result = actionList.get(0).getAction();
        } else {
            System.out.println("ab 没有搜索到行动");
        }
        return result;
    }

    // take random action
    public Action random(GameState gameState, int player){
        Action action = new Action();
        int x1;
        int y1;
        Random r = new Random();
        do{
            x1 = r.nextInt(GameConst.BOARD_SIZE);
            y1 = r.nextInt(GameConst.BOARD_SIZE);
            if(gameState.isLegalPos(x1,y1,player)){
                action.setX1(x1);
                action.setY1(y1);
                break;
            }
        }while (true);

        if(gameState.getTimestep() > 0){
            int x2;
            int y2;
            do{
                x2 = r.nextInt(GameConst.BOARD_SIZE);
                y2 = r.nextInt(GameConst.BOARD_SIZE);
                if(gameState.isLegalPos(x2,y2,player) && x1 != x2 && y1 != y2){
                    action.setX2(x2);
                    action.setY2(y2);
                    break;
                }
            }while (true);
        } else {
            action.setX2(-1);
            action.setY2(-1);
        }

        return action;
    }

    public void setMoveGenerator(MoveGenerator moveGenerator){
        this.moveGenerator = moveGenerator;
    }

    public void setSimulator(Simulator simulator){
        this.simulator = simulator;
    }
}
