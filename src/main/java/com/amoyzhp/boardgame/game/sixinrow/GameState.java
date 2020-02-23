package com.amoyzhp.boardgame.game.sixinrow;

import com.amoyzhp.boardgame.dto.GameStateDTO;
import com.amoyzhp.boardgame.game.sixinrow.constant.GameConst;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.List;

@Data
public class GameState {
    private int[][] chessboard;
    private boolean isTerminal;
    private int timestep;
    private List<Action> historyAction;

    public void init(){
        this.chessboard = new int[GameConst.BOARD_SIZE][GameConst.BOARD_SIZE];
        for(int[] row : chessboard){
            for(int i = 0; i < row.length; i++){
                row[i] = GameConst.EMPTY;
            }
        }
        this.isTerminal = false;
        this.timestep = 0;
        this.historyAction = new ArrayList<>();
    }

    public static GameState fromGameStateDTO(GameStateDTO gameStateDTO){
        GameState gameState = new GameState();
        BeanUtils.copyProperties(gameStateDTO, gameState);
        return gameState;
    }

    public void setPos(int x, int y, int playerSide) {
        if(this.isLegalPos(x,y)){
            this.chessboard[x][y] = playerSide;
        } else {
            System.out.println("illegal move " + x + " " + y);
        }
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

}
