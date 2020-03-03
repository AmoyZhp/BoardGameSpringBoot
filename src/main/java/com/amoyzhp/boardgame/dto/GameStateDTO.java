package com.amoyzhp.boardgame.dto;

import com.amoyzhp.boardgame.game.sixinrow.core.Action;
import com.amoyzhp.boardgame.game.sixinrow.core.GameState;
import com.amoyzhp.boardgame.game.sixinrow.constant.GameConst;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedList;

@Data
public class GameStateDTO {
    private int[][] chessboard;
    private LinkedList<ActionDTO> historyActions;
    private int timestep;
    private boolean terminal;
    private static final Logger LOG = LoggerFactory.getLogger(GameStateDTO.class);

    public GameStateDTO(){

    }

    public GameStateDTO(GameState gameState){
        this.terminal = gameState.isTerminal();
        this.timestep = gameState.getTimestep();
        int[][] tempChessboard = gameState.getChessboard();
        if(tempChessboard.length != GameConst.BOARD_SIZE || tempChessboard[0].length != GameConst.BOARD_SIZE){
            LOG.info("chesss board size is wrong, the wrong size is {}",tempChessboard.length);
        }
        this.chessboard = new int[tempChessboard.length][tempChessboard[0].length];
        for(int i = 0; i < tempChessboard.length; i++){
            for(int j = 0; j < tempChessboard[0].length; j++){
                this.chessboard[i][j] = tempChessboard[i][j];
            }
        }
        LinkedList<Action> tempActions = gameState.getHistoryActions();
        this.historyActions = new LinkedList<>();
        ActionDTO action;
        for(int i = 0; i < tempActions.size(); i++){
            action = new ActionDTO(tempActions.get(i));
            this.historyActions.add(action);
        }
    }

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
