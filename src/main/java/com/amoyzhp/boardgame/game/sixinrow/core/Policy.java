package com.amoyzhp.boardgame.game.sixinrow.core;

import com.amoyzhp.boardgame.game.sixinrow.component.*;
import com.amoyzhp.boardgame.game.sixinrow.constant.GameConst;
import com.amoyzhp.boardgame.game.sixinrow.core.Action;
import com.amoyzhp.boardgame.game.sixinrow.enums.Player;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class Policy {
    final Logger logger = LoggerFactory.getLogger(Policy.class);
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

    public Action threatSpaceSearchBegin(GameState state, Player player){
        // 初始化模拟器
        if( this.simulator.getGameState() == null ||
                this.simulator.getGameState().hashCode() != state.hashCode()){
            this.simulator.setGameState(state);
        }
        Action action = new Action();
        LinkedList<Action> result = new LinkedList<>();
        boolean find = this.threatSpaceSearch(player,player,result);
        if(result.size() > 0){
            return result.getLast();
        }
        return null;
    }


    public Action alphaBetaTreeSearchBegin(GameState gameState, Player player){
        // 初始化模拟器
        if( this.simulator.getGameState() == null ||
                this.simulator.getGameState().hashCode() != gameState.hashCode()){
            this.simulator.setGameState(gameState);
        }
        Action result = new Action();
        List<ActionNode> actionList = new ArrayList<>();
        int beta = Integer.MAX_VALUE;
        int alpha = Integer.MIN_VALUE;
        Player nextPlayer = Player.getNextPlayer(player);
        int depth = GameConst.ALPHA_BETA_DEPTH;
        ActionNode actionNode;
        List<Action> candidateActions = this.moveGenerator.getCandidateAction(
                this.simulator.getGameState(),
                this.simulator.getRoadBoard(),player);
        // depth 是奇数时在 max 层终结
        // 偶数时在 min 层终结
        RoadBoard roadBoard = this.simulator.getRoadBoard();
        int initHashCode = this.simulator.getRoadBoard().hashCode();
        for (Action action : candidateActions){
            this.simulator.step(action);
            int temp = this.alphaBetaTreeSearch(alpha, beta, depth-1, nextPlayer, player);
            this.simulator.moveBack();
            if(initHashCode != this.simulator.getRoadBoard().hashCode()){
                logger.debug("hash code 不一致");
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
            logger.debug("ab 没有搜索到行动");
        }
        return result;
    }

    // requiredPlayer 是需要为其寻找行动的玩家
    // player 表示当前层数"行动"的玩家，用来区分极大层还是极小层
    public int alphaBetaTreeSearch(int alpha, int beta, int depth,
                                   Player player, Player requiredPlayer){
        if(depth == 0){
            return this.evaluator.evaluate(this.simulator.getRoadBoard());
        }
        Player nextPlayer = Player.getNextPlayer(player);
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


    public boolean threatSpaceSearch(Player attack, Player defense, List<Action> result){
        //轮到 attack 行棋，
        if(attack == defense){
            List<Action> threatActions = this.moveGenerator.getCauseThreatActions(
                    this.simulator.getGameState(), this.simulator.getRoadBoard(),
                    attack);
            for(Action action : threatActions){
                int hashcode = this.simulator.getGameState().hashCode();
                this.simulator.step(action);
                List<Action> killActions =  this.moveGenerator.getKillActions(
                        this.simulator.getGameState(),this.simulator.getRoadBoard(),
                        attack);
                List<Action> defenseActions =  this.moveGenerator.getKillActions(
                        this.simulator.getGameState(),this.simulator.getRoadBoard(),
                        Player.getNextPlayer(attack));
                // 如果我方有必胜行动，并且对方没有必胜行动，并且无论对方采取什么行动，我方都能通过该方法获胜
                // 则我方必胜
                if(killActions.size() >= 1 && defenseActions.size() == 0
                    && threatSpaceSearch(attack, Player.getNextPlayer(attack),result)){
                    result.addAll(killActions);
                    this.simulator.moveBack();
                    if(hashcode != this.simulator.getGameState().hashCode()){
                        System.out.println("hash code is not matching");
                    }
                    // 表示 attack 方可以通过 TSS 取得胜利
                    return true;
                }
                this.simulator.moveBack();
            }
            return false;
        } else {
            // 轮到 defense 行棋
            List<Action> defenseActions = this.moveGenerator.getKillActions(
                    this.simulator.getGameState(), this.simulator.getRoadBoard(),
                    attack);
            //  = 0 表示攻击方没有可以必胜的点，不存在胁迫
            if(defenseActions.size() == 0){
                return false;
            }
            for(Action action : defenseActions){
                action.setPlayer(defense);
                this.simulator.step(action);
                List<Action> killActions =  this.moveGenerator.getKillActions(
                        this.simulator.getGameState(),this.simulator.getRoadBoard(),
                        attack);
                if(killActions.size() == 0
                        && threatSpaceSearch(attack, attack,result) == false){
                    this.simulator.moveBack();
                    // 表示 Attack 方不能通过 TSS 取得胜利
                    return false;
                }
                this.simulator.moveBack();
            }
            return true;
        }

    }

    // take random action
    public Action random(GameState gameState, Player player){
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

    public Action getFirstMove(GameState gameState, Player player){
        Action action = new Action();
        if(player == Player.BLACK){
            action.setX1(9);
            action.setY1(9);
        } else if(player == Player.WHITE){
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

    public void setMoveGenerator(MoveGenerator moveGenerator){
        this.moveGenerator = moveGenerator;
    }

    public void setSimulator(Simulator simulator){
        this.simulator = simulator;
    }
}
