package com.amoyzhp.boardgame.game.gomoku.component;

import com.amoyzhp.boardgame.game.gomoku.constant.GameConst;
import com.amoyzhp.boardgame.game.gomoku.enums.GomokuPlayer;
import com.amoyzhp.boardgame.game.model.common.Direction;
import com.amoyzhp.boardgame.game.model.common.Player;
import com.amoyzhp.boardgame.game.model.common.Position;
import com.amoyzhp.boardgame.game.model.component.Evaluator;
import com.amoyzhp.boardgame.game.model.core.Action;
import com.amoyzhp.boardgame.game.model.core.State;

import java.util.Set;

/**
 * evaluator of gomoku
 *
 * @Author: Tuseday Boy
 * @CreatedDate: 2020/03/30
 */
public class GomokuEvaluator implements Evaluator {

    private static GomokuEvaluator instance = new GomokuEvaluator();

    private GomokuEvaluator(){

    }

    public static GomokuEvaluator getInstance(){
        return instance;
    }


    public int evaluate(RoadBoard roadBoard, Player player) {
        int whiteVal = 0;
        int blackVal = 0;
        for(int i = 1; i <= 5; i++){
            blackVal += roadBoard.getRoads(i,0).size() * GameConst.ROAD_VALUES[i];
            whiteVal += roadBoard.getRoads(0,i).size() * GameConst.ROAD_VALUES[i];
        }
        if(player.equals(GomokuPlayer.BLACK) ){
            return blackVal - whiteVal;
        } else if(player.equals(GomokuPlayer.WHITE)){
            return whiteVal - blackVal;
        }
        return -1;
    }

    // 通过上一个行动来判断是否结束
    public boolean isTerminal(State state, Action lastAction){
        int row = lastAction.getPositions().get(0).row();
        int col = lastAction.getPositions().get(0).col();
        Player player = lastAction.getPlayer();
        int cnt = 0;
        // 横向
        for(int offset = - 4; offset <= 4; offset++){
            int newRow = row + Direction.HORIZONTAL.rowOffset() * offset;
            int newCol = col + Direction.HORIZONTAL.colOffset() * offset;
            newRow = Math.min(Math.max(0, newRow), state.getHeight()-1);
            newCol = Math.min(Math.max(0, newCol), state.getWidth()-1);
            if(state.getPlayerOnPos(new Position(newRow, newCol)).getValue() == player.getValue()){
                cnt ++;
            } else {
                cnt = 0;
            }
            if(cnt == 5){
                return true;
            }
        }

        // 纵向
        for(int offset = - 4; offset <= 4; offset++){
            int newRow = row + Direction.VERTICAL.rowOffset() * offset;
            int newCol = col + Direction.VERTICAL.colOffset() * offset;
            newRow = Math.min(Math.max(0, newRow), state.getHeight()-1);
            newCol = Math.min(Math.max(0, newCol), state.getWidth()-1);
            if(state.getPlayerOnPos(new Position(newRow, newCol)).getValue() == player.getValue()){
                cnt ++;
            } else {
                cnt = 0;
            }
            if(cnt == 5){
                return true;
            }
        }
        // 主对角线
        for(int offset = - 4; offset <= 4; offset++){
            int newRow = row + Direction.LEADING_DIAGONAL.rowOffset() * offset;
            int newCol = col + Direction.LEADING_DIAGONAL.colOffset() * offset;
            newRow = Math.min(Math.max(0, newRow), state.getHeight()-1);
            newCol = Math.min(Math.max(0, newCol), state.getWidth()-1);
            if(state.getPlayerOnPos(new Position(newRow, newCol)).getValue() == player.getValue()){
                cnt ++;
            } else {
                cnt = 0;
            }
            if(cnt == 5){
                return true;
            }
        }

        // 副对角线
        for(int offset = - 4; offset <= 4; offset++){
            int newRow = row + Direction.DEPUTY_DIAGONAL.rowOffset() * offset;
            int newCol = col + Direction.DEPUTY_DIAGONAL.colOffset() * offset;
            newRow = Math.min(Math.max(0, newRow), state.getHeight()-1);
            newCol = Math.min(Math.max(0, newCol), state.getWidth()-1);
            if(state.getPlayerOnPos(new Position(newRow, newCol)).getValue() == player.getValue()){
                cnt ++;
            } else {
                cnt = 0;
            }
            if(cnt == 5){
                return true;
            }
        }
        return false;
    }

    public boolean isTerminal(RoadBoard roadBoard){

        return false;
    }

}
