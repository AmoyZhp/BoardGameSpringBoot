package com.amoyzhp.boardgame.game.sixinrow;

import com.amoyzhp.boardgame.dto.ActionDTO;
import com.amoyzhp.boardgame.dto.GameStateDTO;
import com.amoyzhp.boardgame.game.sixinrow.constant.GameConst;
import lombok.Data;

import java.util.LinkedList;

@Data
public class GameState {
    private int[][] chessboard;
    private boolean terminal;
    private int timestep;
    private LinkedList<Action> historyActions;

    public void init(){
        this.chessboard = new int[GameConst.BOARD_SIZE][GameConst.BOARD_SIZE];
        for(int[] row : chessboard){
            for(int i = 0; i < row.length; i++){
                row[i] = GameConst.EMPTY;
            }
        }
        this.terminal = false;
        this.timestep = 0;
        this.historyActions = new LinkedList<>();
    }

    public void setPos(int x, int y, int playerSide) {
        if(this.isLegalPos(x,y)){
            this.chessboard[x][y] = playerSide;
        } else {
            System.out.println("illegal move " + x + " " + y);
        }
    }

    public void addActionsInHistory(Action action){
        this.historyActions.addLast(action);
    }

    public boolean checkTerminal(){
        Action lastAction = this.historyActions.getLast();
        if(this.hasSixInRow(lastAction.getX1(),lastAction.getY1(), lastAction.getPlayer())
            ||this.hasSixInRow(lastAction.getX2(),lastAction.getY2(), lastAction.getPlayer()) ){
            this.terminal = true;
            return true;
        } else {
            this.terminal = false;
            return false;
        }
    }

    public boolean hasSixInRow(int x, int y, int player){
        if(x < 0 || x >= GameConst.BOARD_SIZE || y  < 0 || y >= GameConst.BOARD_SIZE){
            return false;
        }
        int cnt = 0;
        // 横向
        cnt = 0;
        for(int i = Math.max(0,x-5); i < Math.min(x+5, GameConst.BOARD_SIZE - 1); i++){
            if(this.chessboard[i][y] == player){
                cnt++;
                if(cnt == 6){
                    return true;
                }
            } else {
                cnt = 0;
            }
        }
        // 纵向
        cnt = 0;
        for(int i = Math.max(0,y-5); i < Math.min(y+5, GameConst.BOARD_SIZE - 1); i++){
            if(this.chessboard[x][i] == player){
                cnt++;
                if(cnt == 6){
                    return true;
                }
            } else {
                cnt = 0;
            }
        }
        // 135 度
        cnt = 0;
        for(int i = -5; i <= 5; i++){
            if(x+i < 0 || x+i >= GameConst.BOARD_SIZE || y+i < 0 || y+i >= GameConst.BOARD_SIZE){
                continue;
            }
            if(this.chessboard[x+i][y+i] == player){
                cnt++;
                if(cnt == 6){
                    return true;
                }
            } else {
                cnt = 0;
            }
        }
        // 45 度
        cnt = 0;
        for(int i = -5; i <= 5; i++){
            if(x+i < 0 || x+i >= GameConst.BOARD_SIZE || y-i < 0 || y-i >= GameConst.BOARD_SIZE){
                continue;
            }
            if(this.chessboard[x+i][y-i] == player){
                cnt++;
                if(cnt == 6){
                    return true;
                }
            } else {
                cnt = 0;
            }
        }
        return false;
    }

    public boolean isLegalPos(int x, int y) {
        if(x < 0 || x >= GameConst.BOARD_SIZE || y < 0 || y >= GameConst.BOARD_SIZE){
            return  false;
        }
        if(this.chessboard[x][y] != GameConst.EMPTY){
            return false;
        }
        return true;

    }


    public static GameState fromGameStateDTO(GameStateDTO gameStateDTO){
        GameState gameState = new GameState();
        gameState.setTimestep(gameStateDTO.getTimestep());
        gameState.setChessboard(gameStateDTO.getChessboard().clone());
        gameState.setTerminal(gameStateDTO.isTerminal());
        LinkedList<ActionDTO> rawHistoryActions = gameStateDTO.getHistoryActions();
        LinkedList<Action> historyActionsDTO = new LinkedList<>();
        while(rawHistoryActions.size() > 0){
            historyActionsDTO.addLast(Action.fromActionDTO(rawHistoryActions.pollFirst()));
        }
        gameState.setHistoryActions(historyActionsDTO);
        return gameState;
    }

}
