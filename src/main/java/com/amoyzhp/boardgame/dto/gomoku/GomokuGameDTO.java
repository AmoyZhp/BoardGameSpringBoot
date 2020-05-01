package com.amoyzhp.boardgame.dto.gomoku;

import com.amoyzhp.boardgame.game.gomoku.core.GomokuAction;
import lombok.Data;

import java.util.List;

/**
 * @Author: Tuseday Boy
 * @CreatedDate: 2020/03/26
 */

@Data
public class GomokuGameDTO {
    private List<GomokuActionDTO> historyActionsDTO;
    private GomokuStateDTO stateDTO;
    private GomokuActionDTO lastActionDTO;
    private int timestep;
    private int requiredPlayer;
    private boolean debugMode;
    private DebugInfo debugInfo;
}
