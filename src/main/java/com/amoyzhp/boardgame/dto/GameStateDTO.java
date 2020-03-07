package com.amoyzhp.boardgame.dto;

import com.amoyzhp.boardgame.game.sixinrow.core.Action;
import com.amoyzhp.boardgame.game.sixinrow.core.GameState;
import com.amoyzhp.boardgame.game.sixinrow.constant.GameConst;
import com.amoyzhp.boardgame.game.sixinrow.enums.Player;
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
        Player[][] tempChessboard = gameState.getChessboard();
        if(tempChessboard.length != GameConst.BOARD_SIZE || tempChessboard[0].length != GameConst.BOARD_SIZE){
            LOG.info("chesss board size is wrong, the wrong size is {}",tempChessboard.length);
        }
        this.chessboard = new int[tempChessboard.length][tempChessboard[0].length];
        for(int i = 0; i < tempChessboard.length; i++){
            for(int j = 0; j < tempChessboard[0].length; j++){
                this.chessboard[i][j] = tempChessboard[i][j].getValue();
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

}
