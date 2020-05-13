package com.amoyzhp.boardgame.game.gomoku.component;

import com.amoyzhp.boardgame.game.gomoku.enums.GomokuPlayer;
import com.amoyzhp.boardgame.game.model.common.Position;
import com.amoyzhp.boardgame.game.model.core.Action;
import com.amoyzhp.boardgame.game.model.core.State;

import java.util.LinkedList;

/**
 * simulator
 *
 * @Author: Tuseday Boy
 * @CreatedDate: 2020/05/04
 */
public class GomokuSimulatorLight {
    private State state;
    private LinkedList<Action> historyActions;
    private GomokuEvaluator evaluator;

    public GomokuSimulatorLight(State state) {
        this.state = state;
        this.historyActions = new LinkedList<>();
        this.evaluator = new GomokuEvaluator();
    }

    public State getGameState() {
        return this.state;
    }

    public Action getLastAction(){
        return this.historyActions.getLast();
    }

    public State step(Action action) {
        Position position = action.getPositions().get(0);
        GomokuPlayer player = GomokuPlayer.paraseValue(action.getPlayer().getValue());
        if(this.state.updateState(position, player)){
            // 该方法会更新和 (row, col) 点相关的路上的信息
            this.historyActions.addLast(action);
            if(state.getEmptyPos().size() == 0 || evaluator.isTerminal(state, action)){
                state.setTerminal(true);
            } else {
                state.setTerminal(false);
            }

        }
        return this.state;
    }

    public State moveBack() {
        Action action = this.historyActions.removeLast();
        Position position = action.getPositions().get(0);
        this.state.updateState(position, GomokuPlayer.EMPTY);
        if(state.getEmptyPos().size() == 0 || evaluator.isTerminal(state, action)){
            state.setTerminal(true);
        } else {
            state.setTerminal(false);
        }
        return this.state;
    }
}
