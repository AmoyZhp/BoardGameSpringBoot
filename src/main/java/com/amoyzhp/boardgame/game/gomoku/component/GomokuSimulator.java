package com.amoyzhp.boardgame.game.gomoku.component;

import com.amoyzhp.boardgame.game.gomoku.core.GomokuAction;
import com.amoyzhp.boardgame.game.gomoku.core.GomokuState;
import com.amoyzhp.boardgame.game.gomoku.enums.GomokuPlayer;
import com.amoyzhp.boardgame.game.model.common.Position;
import com.amoyzhp.boardgame.game.model.component.Simulator;
import com.amoyzhp.boardgame.game.model.core.Action;
import com.amoyzhp.boardgame.game.model.core.State;

import java.util.LinkedList;

/**
 * simulator of gomoku
 *
 * @Author: Tuseday Boy
 * @CreatedDate: 2020/03/30
 */
public class GomokuSimulator implements Simulator {
    private State state;
    private RoadBoard roadBoard;
    private LinkedList<Action> historyActions;

    public GomokuSimulator(State state) {
        this.state = state;
        this.roadBoard = new RoadBoard(this.state.getChessboard());
        this.historyActions = new LinkedList<>();
    }

    public State getGameState() {
        return this.state;
    }

    public RoadBoard getRoadBoard() {
        return this.roadBoard;
    }

    public void step(Action action) {
        Position position = action.getPositions().get(0);
        GomokuPlayer player = GomokuPlayer.paraseValue(action.getPlayer().getValue());
        if(this.state.updateState(position, player)){
            // 该方法会更新和 (row, col) 点相关的路上的信息
            this.roadBoard.updateRoad(position, player);
            this.historyActions.addLast(action);
        }

    }

    public void moveBack() {
        Action action = this.historyActions.removeLast();
        Position position = action.getPositions().get(0);
        this.state.updateState(position, GomokuPlayer.EMPTY);
        this.roadBoard.updateRoad(position, GomokuPlayer.EMPTY);
    }
}
