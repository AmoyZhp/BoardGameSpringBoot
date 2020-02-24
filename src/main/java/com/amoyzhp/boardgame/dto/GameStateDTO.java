package com.amoyzhp.boardgame.dto;

import com.amoyzhp.boardgame.game.sixinrow.Action;
import com.amoyzhp.boardgame.game.sixinrow.GameState;
import lombok.Data;

import java.util.LinkedList;

@Data
public class GameStateDTO {
    private int[][] chessboard;
    private LinkedList<ActionDTO> historyActions;
    private int timestep;
    private boolean terminal;


    public static GameStateDTO fromRawGameState(GameState gameState){
        GameStateDTO gameStateDTO = new GameStateDTO();
        gameStateDTO.setTimestep(gameState.getTimestep());
        gameStateDTO.setTerminal(gameState.isTerminal());
        gameStateDTO.setChessboard(gameState.getChessboard().clone());
        LinkedList<Action> rawHistoryActions = gameState.getHistoryActions();
        LinkedList<ActionDTO> historyActionsDTO = new LinkedList<>();
        while(rawHistoryActions.size() > 0){
            historyActionsDTO.addLast(ActionDTO.fromRawAction(rawHistoryActions.pollFirst()));
        }
        gameStateDTO.setHistoryActions(historyActionsDTO);
        return  gameStateDTO;
    }
}
