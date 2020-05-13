package com.amoyzhp.boardgame.game.gomoku.core;

import com.amoyzhp.boardgame.dto.gomoku.GomokuStateDTO;
import com.amoyzhp.boardgame.game.gomoku.component.RoadBoard;
import com.amoyzhp.boardgame.game.gomoku.enums.GomokuPlayer;
import com.amoyzhp.boardgame.game.model.common.Player;
import com.amoyzhp.boardgame.game.model.common.Position;
import com.amoyzhp.boardgame.game.model.core.State;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
    private Set<Position> emptyPos;
    private boolean terminal;
    private int[][] chessboard;
    private int[][][] zobrisHashBoard;
    private int hashval;

    private GomokuState(){

    }
    public GomokuState(int width, int height) {
        this.width = width;
        this.height = height;
        this.chessboard = new int[height][width];
        this.zobrisHashBoard = new int[height][width][3];
        this.emptyPos = new HashSet<>();
        int[][][] temp = this.readZorbistHashBoard("./data/zorbist_hash_borad.data");
        this.hashval = 0;
        for(int i = 0; i < this.chessboard.length; i++){
            for(int j = 0; j < this.chessboard[i].length; j++){
                this.chessboard[i][j] = GomokuPlayer.EMPTY.getValue();
                for(int k = 0; k < 3; k++){
                    zobrisHashBoard[i][j][k] = temp[i][j][k];
                }
            }
        }
        this.reset();

    }
    public static GomokuState valueOfDTO(GomokuStateDTO dto){
        int[][] temp = dto.getChessboard();
        GomokuState state = new GomokuState(temp.length, temp.length);
        for(int i = 0; i < temp.length; i++){
            for(int j = 0; j < temp[i].length; j++){
                if(temp[i][j] != GomokuPlayer.EMPTY.getValue()){
                    state.updateState(new Position(i,j), GomokuPlayer.paraseValue(temp[i][j]));
                }
            }
        }
        state.setTerminal(dto.isTerminal());
        return state;
    }

    @Override
    public boolean updateState(Position position, Player player) {
        if(this.isLegalPos(position, player)){
            int row = position.row();
            int col = position.col();
            int playerVal = player.getValue();
            if(playerVal == GomokuPlayer.EMPTY.getValue()){
                // 如果为 EMPTY 说明是回退操作，该位置原本有值。
                // 将原本位置上的值异或
                // 新值等同于该位置为 EMPTY 的值
                this.hashval ^= this.zobrisHashBoard[row][col][this.chessboard[row][col]];
                this.emptyPos.add(position);
            } else {
                // 该形式表示是落下一个子
                this.hashval ^= this.zobrisHashBoard[row][col][playerVal];
                this.emptyPos.remove(position);
            }
            this.chessboard[position.row()][position.col()] = player.getValue();
            return true;
        }
        logger.debug(String.format("invalid operation (row %d , col %d), matched board pos is %d, acting player is %d",
                position.row(), position.col(),
                this.chessboard[position.row()][position.col()], player.getValue()) );
        return false;
    }

    @Override
    public Player getPlayerOnPos(Position position) {
        if(this.isLegalPos(position)){
            return GomokuPlayer.paraseValue(this.chessboard[position.row()][position.col()]);
        } else {
            return null;
        }
    }

    @Override
    public void reset() {
        this.hashval = 0;
        this.emptyPos.clear();
        for(int i = 0; i < this.chessboard.length; i++){
            for(int j = 0; j < this.chessboard[i].length; j++){
                this.emptyPos.add(new Position(i,j));
                this.chessboard[i][j] = GomokuPlayer.EMPTY.getValue();
                this.hashval ^= zobrisHashBoard[i][j][0];
            }
        }
        this.terminal = false;
    }

    @Override
    public int[][] getChessboard() {
        return this.chessboard;
    }

    @Override
    public boolean isTerminal() {
        return this.terminal;
    }

    @Override
    public void setTerminal(boolean terminal){
        this.terminal = terminal;
    }

    @Override
    public Set<Position> getEmptyPos() {
        return this.emptyPos;
    }

    @Override
    public boolean isLegalPos(Position position, Player player) {
        int row = position.row();
        int col = position.col();
        if(isLegalPos(position) == false){
            return false;
        }
        if(player.getValue() == GomokuPlayer.EMPTY.getValue()
                && this.chessboard[row][col] != GomokuPlayer.EMPTY.getValue()){
            return  true;
        }
        if(this.chessboard[row][col] != GomokuPlayer.EMPTY.getValue()){
            return false;
        }
        return true;
    }

    public boolean isLegalPos(Position position) {
        int row = position.row();
        int col = position.col();
        if(row < 0 || row >= this.height || col < 0 || col >= this.width){
            return false;
        }
        return true;
    }

    @Override
    public int getWidth() {
        return this.width;
    }
    @Override
    public int getHeight() {
        return this.height;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GomokuState that = (GomokuState) o;
        return width == that.getWidth() &&
                height == that.getHeight()&&
                terminal == that.isTerminal() &&
                hashval == that.hashCode() &&
                Arrays.equals(chessboard, that.chessboard);
    }

    @Override
    public int hashCode(){
        return this.hashval;
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

    private int[][][] readZorbistHashBoard(String filename) {
        try {
            FileReader fileReader = new FileReader(new File(filename));
            BufferedReader br = new BufferedReader(fileReader);
            int[][][] zobristBoad = new int[15][15][3];
            for(int i = 0 ; i < 15; i++){
                for(int j = 0; j < 15; j++){
                    for(int k = 0; k < 3; k++){
                        zobristBoad[i][j][k] = Integer.valueOf(br.readLine());
                    }
                }
            }
            br.close();
            fileReader.close();
            return zobristBoad;
        } catch (IOException ex){
            return null;
        }
    }

    public static GomokuState copyState(State state){
        GomokuState copyState = new GomokuState(state.getHeight(), state.getWidth());
        int[][] oldBoard = state.getChessboard();
        for(int i = 0; i < oldBoard.length; i++){
            for(int j = 0 ; j < oldBoard[i].length; j++){
                if(oldBoard[i][j] != GomokuPlayer.EMPTY.getValue()){
                    copyState.updateState(new Position(i,j), GomokuPlayer.paraseValue(oldBoard[i][j]));
                }

            }
        }
        copyState.setTerminal(state.isTerminal());
        return copyState;
    }
}
