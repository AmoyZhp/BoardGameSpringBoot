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
    // light 只进行最简单的 state 更新，不更新 roadboard
    private boolean useRoadBoard;
    private GomokuEvaluator evaluator;

    public GomokuSimulator(State state) {
        this(state, true);
    }
    public GomokuSimulator(State state, boolean useRoadBoard) {
        this.state = GomokuState.copyState(state);;
        this.historyActions = new LinkedList<>();
        this.useRoadBoard = useRoadBoard;
        if(useRoadBoard){
            this.roadBoard =  new RoadBoard(this.state.getChessboard());
            this.evaluator = null;
        } else {
            this.roadBoard = null;
            this.evaluator = GomokuEvaluator.getInstance();
        }

    }

    public void setState(State state){
        this.state = GomokuState.copyState(state);
        this.roadBoard = new RoadBoard(this.state.getChessboard());
        this.historyActions = new LinkedList<>();
    }

    public State getGameState() {
        return this.state;
    }

    public RoadBoard getRoadBoard() {
        return this.roadBoard;
    }

    public State step(Action action) {
        Position position = action.getPositions().get(0);
        GomokuPlayer player = GomokuPlayer.paraseValue(action.getPlayer().getValue());
        if(this.state.updateState(position, player)){
            // 该方法会更新和 (row, col) 点相关的路上的信息

            if(useRoadBoard){
                this.roadBoard.updateRoad(position, player);
                if(this.state.getEmptyPos().size() == 0 || this.roadBoard.getRoads(player, 5).size() > 0){
                    this.state.setTerminal(true);
                } else {
                    this.state.setTerminal(false);
                }
            } else {
                if(state.getEmptyPos().size() == 0 || evaluator.isTerminal(state, action)){
                    state.setTerminal(true);
                } else {
                    state.setTerminal(false);
                }
            }
            this.historyActions.addLast(action);

            return this.state;
        }
        return null;

    }

    public LinkedList<Action> getHistoryActions(){
        return this.historyActions;
    }

    public State moveBack() {
        Action action = this.historyActions.removeLast();
        Position position = action.getPositions().get(0);
        if(this.state.updateState(position, GomokuPlayer.EMPTY)){
            if(useRoadBoard){
                this.roadBoard.updateRoad(position, GomokuPlayer.EMPTY);
                if(this.state.getEmptyPos().size() > 0 && this.roadBoard.getRoads(action.getPlayer(), 5).size() == 0
                        && this.roadBoard.getRoads(GomokuPlayer.getNextPlayer(action.getPlayer()),
                        5).size() == 0){
                    this.state.setTerminal(false);
                }
            } else {
                if(state.getEmptyPos().size() == 0 || evaluator.isTerminal(state, action)){
                    state.setTerminal(true);
                } else {
                    state.setTerminal(false);
                }
            }
            return this.state;
        }
        return null;
    }
}
