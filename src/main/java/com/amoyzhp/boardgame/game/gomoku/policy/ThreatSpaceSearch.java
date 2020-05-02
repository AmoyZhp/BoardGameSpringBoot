package com.amoyzhp.boardgame.game.gomoku.policy;

import com.amoyzhp.boardgame.game.gomoku.component.GomokuActionsGenerator;
import com.amoyzhp.boardgame.game.gomoku.component.GomokuSimulator;
import com.amoyzhp.boardgame.game.gomoku.core.GomokuAction;
import com.amoyzhp.boardgame.game.gomoku.enums.GomokuPlayer;
import com.amoyzhp.boardgame.game.model.common.Player;
import com.amoyzhp.boardgame.game.model.core.Action;
import com.amoyzhp.boardgame.game.model.core.State;
import com.amoyzhp.boardgame.game.model.policy.Policy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedList;
import java.util.List;

/**
 * 胁迫搜索
 *
 * @Author: Tuseday Boy
 * @CreatedDate: 2020/04/21
 */
public class ThreatSpaceSearch implements Policy {
    final Logger logger = LoggerFactory.getLogger(ThreatSpaceSearch.class);

    // -1 就是没有限制
    private final int DEFAULT_DEPTH = -1;
    private final long DEFAULT_TIME_LIMIT = -1;

    private GomokuActionsGenerator actionsGenerator;
    private GomokuSimulator simulator;
    private LinkedList<Action> result;

    private long beginTime;
    private long currentTime;
    private long timeLimit;

    public ThreatSpaceSearch(){
        this.actionsGenerator = new GomokuActionsGenerator();
        this.simulator = null;
    }

    public Action getAction(GomokuSimulator simulator, GomokuPlayer player){
        return this.getAction(simulator, player, this.DEFAULT_DEPTH, this.DEFAULT_TIME_LIMIT);
    }

    public Action getAction(GomokuSimulator simulator, GomokuPlayer player, int depth, long timeLimit) {
        // 默认情况下 depth = -1
        // 避免因为 depth = 0 而返回的情况
        if(depth <= 0){
            depth = -1;
        }
        // 默认情况 timeLimit = -1
        // 表示不管超时的情况
        if(timeLimit < 0){
            this.timeLimit = -1;
        } else {
            this.timeLimit = timeLimit;
        }

        this.beginTime = System.currentTimeMillis();
        this.simulator = simulator;
        GomokuPlayer nextPlayer = GomokuPlayer.getNextPlayer(player.getValue());

        // 先查找我方是否有必胜的点
        List<Action> killActions = this.actionsGenerator.getKillAction(this.simulator, player);
        if(killActions.size() > 0){
            return killActions.get(0);
        }

        // 再查找对方是否有必胜点
        killActions = this.actionsGenerator.getKillAction(this.simulator, nextPlayer);
        if(killActions.size() > 0){
            return killActions.get(0);
        }

        // 若没有则进行 TSS 搜索
        this.result = new LinkedList<>();
        Action action = null;
        logger.info("tss depth is " + depth);
        logger.info("time limit" + this.timeLimit);
        if(this.threatSpaceSearch(player,player, depth)){
            action = result.get(0);
            logger.info(String.format("TSS shot player %d : (row %d , col %d)", player.getValue(),
                    action.getPositions().get(0).row(), action.getPositions().get(0).col()) );

        }
        this.currentTime = System.currentTimeMillis();
        logger.info("TSS search time is " + (currentTime - beginTime) / 1000.0 + " s ");
        return action;
    }

    public boolean threatSpaceSearch(GomokuPlayer actingPlayer, GomokuPlayer player, int depth){
        // 默认情况下 depth = -1
        // 因此不会出现因为 depth = 0 而返回的情况
        if(depth == 0){
            return false;
        }
        // 默认情况 timeLimit = -1
        // 表示不管超时的情况
        if(this.timeLimit >= 0){
            this.currentTime = System.currentTimeMillis();
            if(this.currentTime - this.beginTime >= this.timeLimit){
                return false;
            }
        }

        GomokuPlayer nextPlayer = GomokuPlayer.getNextPlayer(actingPlayer.getValue());
        boolean find = false;
        if(actingPlayer == player){
            // 轮到算法调用方行棋
            List<Action> actions = this.actionsGenerator.getTSSCandidateActions(this.simulator, actingPlayer);
            for(Action action : actions){
                int hash = this.simulator.getGameState().hashCode();
                this.simulator.step(action);
                List<Action> killActions = this.actionsGenerator.getKillAction(this.simulator, actingPlayer);
                List<Action> defenseActions = this.actionsGenerator.getKillAction(this.simulator,
                        nextPlayer);
                // 如果我方有必胜行动，并且对方没有必胜行动，则对方只能进行防守
                // 假设对方无论采取什么行动，都无法消除所有必胜行动
                // 则我方必胜
                if(killActions.size() >= 1 && defenseActions.size() == 0 &&
                        this.threatSpaceSearch(nextPlayer, player, depth-1)){
                    this.result.addFirst(action);
                    find = true;
                }
                this.simulator.moveBack();
                if(hash != this.simulator.getGameState().hashCode()){
                    logger.info("zobrist hash code error");
                }
                if(find){
                    logger.info("search tree depth is " + depth + " player is " + player.getValue());
                    return true;
                }
            }
            return false;

        } else {
            // 轮到算法调用方对手行棋

            // 算法调用方的必胜行动是对手的防守行动
            List<Action> defenseActions = new LinkedList<>();
            for(Action action : this.actionsGenerator.getKillAction(this.simulator,
                    player)){
                defenseActions.add(new GomokuAction(action.getPositions().get(0), actingPlayer));
            }

            // 如果没有防守行动，表示算法调用方没有必胜行动了
            if(defenseActions.size() == 0){
                return false;
            }

            for(Action action : defenseActions){
                this.simulator.step(action);
                List<Action> killActions = this.actionsGenerator.getKillAction(this.simulator, player);
                if(killActions.size() == 0 && this.threatSpaceSearch(nextPlayer,player, depth - 1) == false){
                    find = false;
                } else {
                    find = true;
                }
                this.simulator.moveBack();
                if(find == false){
                    // 对方有一个行动可以阻止我方必胜
                    return false;
                }
            }
            // 表示对方的所有行动都不能阻止我们获胜
            return true;
        }
    }

    @Override
    public Action getAction(State state, Player player) {
        return null;
    }
}
