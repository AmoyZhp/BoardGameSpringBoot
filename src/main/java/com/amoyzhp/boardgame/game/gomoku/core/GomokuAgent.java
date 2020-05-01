package com.amoyzhp.boardgame.game.gomoku.core;

import com.amoyzhp.boardgame.dto.gomoku.DebugInfo;
import com.amoyzhp.boardgame.game.gomoku.policy.MixPolicy;
import com.amoyzhp.boardgame.game.gomoku.policy.RandomPolicy;
import com.amoyzhp.boardgame.game.gomoku.enums.GomokuPlayer;
import com.amoyzhp.boardgame.game.model.common.Player;
import com.amoyzhp.boardgame.game.model.common.Position;
import com.amoyzhp.boardgame.game.model.core.Action;
import com.amoyzhp.boardgame.game.model.core.Agent;
import com.amoyzhp.boardgame.game.model.core.State;

/**
 * gomoku agent
 *
 * @Author: Tuseday Boy
 * @CreatedDate: 2020/03/09
 */
public class GomokuAgent implements Agent {

    private int player;
    private RandomPolicy randomPolicy;
    private MixPolicy mixPolicy;

    public GomokuAgent(GomokuPlayer player) {
        this.player = player.getValue();
        this.randomPolicy = new RandomPolicy();
        this.mixPolicy = new MixPolicy();
    }

    public Action act(State state, int timestep, DebugInfo debugInfo) {
        if(timestep == 0 || timestep == 1){
            if(player == GomokuPlayer.BLACK.getValue()){
                return new GomokuAction(new Position(7,7),GomokuPlayer.BLACK);
            } else {
                if(state.isLegalPos(new Position(7,7),GomokuPlayer.WHITE)){
                    return new GomokuAction(new Position(7,7), GomokuPlayer.WHITE);
                } else {
                    return new GomokuAction(new Position(7,8) ,GomokuPlayer.WHITE);
                }
            }
        } else {
            return this.act(state, debugInfo);
        }
    }
    public Action act(State state, DebugInfo debugInfo) {
        return this.mixPolicy.getAction(state, GomokuPlayer.paraseValue(player), debugInfo);
    }

    @Override
    public Action act(State state) {
        return this.mixPolicy.getAction(state, GomokuPlayer.paraseValue(player));
    }


    @Override
    public void init(Player player) {
        this.player = player.getValue();
    }

    @Override
    public Player getPlayer() {
        return GomokuPlayer.paraseValue(player);
    }
}
