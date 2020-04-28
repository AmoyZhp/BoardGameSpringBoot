package com.amoyzhp.boardgame.game.gomoku.policy;

import com.amoyzhp.boardgame.game.gomoku.component.GomokuActionsGenerator;
import com.amoyzhp.boardgame.game.gomoku.component.GomokuSimulator;
import com.amoyzhp.boardgame.game.gomoku.core.GomokuAction;
import com.amoyzhp.boardgame.game.gomoku.enums.GomokuPlayer;
import com.amoyzhp.boardgame.game.model.common.Player;
import com.amoyzhp.boardgame.game.model.core.Action;
import com.amoyzhp.boardgame.game.model.core.State;
import com.amoyzhp.boardgame.game.model.policy.Policy;

import java.util.LinkedList;
import java.util.List;

/**
 * 胁迫搜索
 *
 * @Author: Tuseday Boy
 * @CreatedDate: 2020/04/21
 */
public class ThreatSpaceSearch implements Policy {

    private GomokuActionsGenerator actionsGenerator;
    private GomokuSimulator simulator;
    private LinkedList<Action> result;
    public ThreatSpaceSearch(){
        this.actionsGenerator = new GomokuActionsGenerator();
        this.simulator = null;

    }

    public Action getAction(GomokuSimulator simulator, GomokuPlayer player) {
        this.simulator = simulator;
        GomokuPlayer nextPlayer = GomokuPlayer.getNextPlayer(player.getValue());
        List<Action> killActions = this.actionsGenerator.getKillAction(this.simulator, player);
        if(killActions.size() > 0){
            return killActions.get(0);
        }
        killActions = this.actionsGenerator.getKillAction(this.simulator, nextPlayer);
        if(killActions.size() > 0){
            return killActions.get(0);
        }

        this.result = new LinkedList<>();
        if(this.threatSpaceSearch(player,player)){
            System.out.println("TSS shot player" + player.getValue());
            return result.get(0);
        }
        return null;
    }

    public boolean threatSpaceSearch(GomokuPlayer actingPlayer, GomokuPlayer player){
        GomokuPlayer nextPlayer = GomokuPlayer.getNextPlayer(actingPlayer.getValue());
        boolean find = false;
        if(actingPlayer == player){
            // 轮到算法调用方行棋
            List<Action> actions = this.actionsGenerator.getTSSCandidateActions(this.simulator, actingPlayer);
            for(Action action : actions){
                this.simulator.step(action);
                List<Action> killActions = this.actionsGenerator.getKillAction(this.simulator, actingPlayer);
                List<Action> defenseActions = this.actionsGenerator.getKillAction(this.simulator,
                        nextPlayer);
                // 如果我方有必胜行动，并且对方没有必胜行动，则对方只能进行防守
                // 假设对方无论采取什么行动，都无法消除所有必胜行动
                // 则我方必胜
                if(killActions.size() >= 1 && defenseActions.size() == 0 &&
                        this.threatSpaceSearch(nextPlayer, player)){
                    this.result.addFirst(action);
                    find = true;
                }
                this.simulator.moveBack();
                if(find){
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
                if(killActions.size() == 0 && this.threatSpaceSearch(nextPlayer,player) == false){
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
