package com.amoyzhp.boardgame.game.gomoku.core;

import com.amoyzhp.boardgame.dto.gomoku.GomokuStateDTO;
import com.amoyzhp.boardgame.game.gomoku.component.RoadBoard;
import com.amoyzhp.boardgame.game.gomoku.enums.GomokuPlayer;
import com.amoyzhp.boardgame.game.model.common.Player;
import com.amoyzhp.boardgame.game.model.common.Position;
import com.amoyzhp.boardgame.game.model.core.State;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * gomoku state
 *
 * @Author: Tuseday Boy
 * @CreatedDate: 2020/03/09
 */
public class GomokuState implements State {
    final Logger logger = LoggerFactory.getLogger(GomokuState.class);
    private int width;
    private int height;
    private boolean terminal;
    private int[][] chessboard;

    private GomokuState(){

    }

    public GomokuState(int width, int height) {
        this.width = width;
        this.height = height;
        this.chessboard = new int[height][width];
        for(int i = 0; i < this.chessboard.length; i++){
            for(int j = 0; j < this.chessboard[i].length; j++){
                this.chessboard[i][j] = GomokuPlayer.EMPTY.getValue();
            }
        }
    }

    @Override
    public boolean updateState(Position position, Player player) {
        if(this.isLegalPos(position, player)){
            this.chessboard[position.row()][position.col()] = player.getValue();
            return true;
        }

        return false;
    }

    @Override
    public void reset() {
        for(int i = 0; i < this.chessboard.length; i++){
            for(int j = 0; j < this.chessboard[i].length; j++){
                this.chessboard[i][j] = GomokuPlayer.EMPTY.getValue();
            }
        }
        this.terminal = false;
    }

    public static GomokuState valueOfDTO(GomokuStateDTO dto){
        int[][] temp = dto.getChessboard();
        GomokuState state = new GomokuState(temp.length, temp.length);
        for(int i = 0; i < temp.length; i++){
            for(int j = 0; j < temp[i].length; j++){
                state.updateState(new Position(i,j), GomokuPlayer.paraseValue(temp[i][j]));
            }
        }
        return state;
    }

    public void setChessboard(int[][] board){
        if(board.length > 0 && board[0].length > 0){
            this.height = board.length;
            this.width = board[0].length;
            this.chessboard = new int[height][width];
            for(int i = 0; i < height; i++){
                for(int j = 0; j < width; j++){
                    this.chessboard[i][j] = board[i][j];
                }
            }
        }
    }

    public int[][] getChessboard() {
        return this.chessboard;
    }

    @Override
    public boolean isTerminal() {
        return this.terminal;
    }

    @Override
    public boolean isLegalPos(Position position, Player player) {
        int row = position.row();
        int col = position.col();
        if(row < 0 || row >= this.height || col < 0 || col >= this.width){
            logger.debug(String.format("index out of range : row %d , col %d", row,col));
            return false;
        }
        if(player.getValue() == GomokuPlayer.EMPTY.getValue()){
            return  true;
        }
        if(this.chessboard[row][col] != GomokuPlayer.EMPTY.getValue()){
            logger.debug(String.format("pos in not empty : row %d , col %d, player %d", row,col,player.getValue()));
            return false;
        }
        return true;
    }

    public void setTerminal(boolean terminal){
        this.terminal = terminal;
    }
    
    public int getWidth() {
        return this.width;
    }
    public int getHeight() {
        return this.height;
    }

    @Override
    public String toString(){
        String s = "";
        for(int[] row: this.chessboard){
            s += "[";
            for(int num: row){
                s += num + ", ";
            }
            s += "]\n";
        }
        return s;
    }
}
