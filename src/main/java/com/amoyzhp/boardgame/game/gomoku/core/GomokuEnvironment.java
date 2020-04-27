package com.amoyzhp.boardgame.game.gomoku.core;

import com.amoyzhp.boardgame.game.gomoku.constant.GameConst;
import com.amoyzhp.boardgame.game.model.common.Position;
import com.amoyzhp.boardgame.game.model.core.Environment;
import com.amoyzhp.boardgame.game.model.core.Action;
import com.amoyzhp.boardgame.game.model.core.State;


import java.util.LinkedList;
import java.util.List;

/**
 * gomoku environment
 *
 * @Author: Tuseday Boy
 * @CreatedDate: 2020/03/09
 */
public class GomokuEnvironment implements Environment {

    private State state;
    private int timestep;
    private LinkedList<Action> historyActions;


    public void init(State state, LinkedList<Action> historyActions , int timestep){
        this.state = state;
        this.historyActions = historyActions;
        this.timestep = timestep;
    }

    public void reset() {
        this.state.reset();
        this.historyActions.clear();
        this.timestep = 0;
    }

    @Override
    public void init(){
        this.state = new GomokuState(GameConst.WIDTH, GameConst.HEIGHT);
        this.timestep = 0;
        this.historyActions = new LinkedList<>();
    }

    @Override
    public State step(Action action) {
        for(Position position : action.getPositions()){
            this.state.updateState(position, action.getPlayer());
        }
        this.timestep += 1;
        this.historyActions.addLast(action);
        return this.state;
    }

    @Override
    public void close() {

    }
    @Override
    public void seed(int seed) {

    }

    public int getTimestep() {
        return timestep;
    }

    public LinkedList<? super GomokuAction> getHistoryActions(){
        return this.historyActions;
    }
}
