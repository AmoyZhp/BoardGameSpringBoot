package com.amoyzhp.boardgame.dto.gomoku;

import com.amoyzhp.boardgame.game.model.core.State;
import lombok.Data;

/**
 * gmoku state dto
 *
 * @Author: Tuseday Boy
 * @CreatedDate: 2020/03/26
 */
@Data
public class GomokuStateDTO {
    private int[][] chessboard;
    private boolean terminal;

    public static GomokuStateDTO valueOfState(State state){
        GomokuStateDTO stateDTO = new GomokuStateDTO();
        int[][] temp = state.getChessboard();
        int[][] board = new int[state.getHeight()][state.getWidth()];
        for(int i = 0; i < temp.length; i++){
            for(int j = 0; j < temp[i].length; j++){
                board[i][j] = temp[i][j];
            }
        }
        stateDTO.setChessboard(board);
        stateDTO.setTerminal(state.isTerminal());
        return  stateDTO;
    }
}
