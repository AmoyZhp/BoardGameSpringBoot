package com.amoyzhp.boardgame.dto.gomoku;

import com.amoyzhp.boardgame.game.gomoku.core.GomokuAction;
import com.amoyzhp.boardgame.game.model.core.Action;
import lombok.Data;

/**
 * gomoku action dto
 *
 * @Author: Tuseday Boy
 * @CreatedDate: 2020/03/26
 */
@Data
public class GomokuActionDTO {
    private int row;
    private int col;
    private int player;

    public static GomokuActionDTO valueOfAction(Action action){
        GomokuActionDTO actionDTO = new GomokuActionDTO();
        actionDTO.setRow(action.getPositions().get(0).row());
        actionDTO.setCol(action.getPositions().get(0).col());
        actionDTO.setPlayer(action.getPlayer().getValue());
        return actionDTO;
    }
}
