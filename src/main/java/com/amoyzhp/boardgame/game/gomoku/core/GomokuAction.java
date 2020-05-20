package com.amoyzhp.boardgame.game.gomoku.core;

import com.amoyzhp.boardgame.dto.gomoku.GomokuActionDTO;
import com.amoyzhp.boardgame.game.gomoku.enums.GomokuPlayer;
import com.amoyzhp.boardgame.game.model.common.Player;
import com.amoyzhp.boardgame.game.model.common.Position;
import com.amoyzhp.boardgame.game.model.core.Action;

import java.util.ArrayList;
import java.util.List;

/**
 * gomoku action
 *
 * @Author: Tuseday Boy
 * @CreatedDate: 2020/03/09
 */
public class GomokuAction implements Action {
    private Player player;
    private Position position;

    public GomokuAction(Position position, Player player){
        this.position = position;
        this.player = player;
    }

    public static GomokuAction valueOfDTO(GomokuActionDTO actionDTO){
        GomokuAction action = new GomokuAction(new Position(actionDTO.getRow(), actionDTO.getCol()),
                GomokuPlayer.paraseValue(actionDTO.getPlayer()));
        return action;
    }

    public final Position getPosition() {
        return position;
    }


    @Override
    public Player getPlayer() {
        return this.player;
    }

    @Override
    public List<Position> getPositions() {
        List<Position> positions = new ArrayList<>(1);
        positions.add(position);
        return positions;
    }

}
