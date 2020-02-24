package com.amoyzhp.boardgame.game.sixinrow;

import com.amoyzhp.boardgame.game.sixinrow.constant.GameConst;

import java.util.ArrayList;
import java.util.List;

public class MoveGenerator {

    public List<Action> getCandidateAction(GameState gameState){
        List<Action> actionList = new ArrayList<>();
        List<Action> historyActions = gameState.getHistoryActions();
        Action lastAction = historyActions.get(historyActions.size());
        int[][] chessboard = gameState.getChessboard();
        int x1 = lastAction.getX1();
        int y1 = lastAction.getY1();
        for(int i = Math.max(x1-2,0); i <= Math.min(x1+1, GameConst.BOARD_SIZE - 1); i++){
            for(int j = Math.max(y1-2, 0); j <= Math.min(y1+1, GameConst.BOARD_SIZE - 1); j++){
                if(chessboard[i][j] == GameConst.EMPTY){

                }
            }
        }
        return actionList;
    }
}
