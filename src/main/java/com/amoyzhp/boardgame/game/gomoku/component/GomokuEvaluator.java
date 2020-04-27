package com.amoyzhp.boardgame.game.gomoku.component;

import com.amoyzhp.boardgame.game.gomoku.constant.GameConst;
import com.amoyzhp.boardgame.game.gomoku.enums.GomokuPlayer;
import com.amoyzhp.boardgame.game.model.component.Evaluator;

import java.util.Set;

/**
 * evaluator of gomoku
 *
 * @Author: Tuseday Boy
 * @CreatedDate: 2020/03/30
 */
public class GomokuEvaluator implements Evaluator {


    public int evaluate(RoadBoard roadBoard, GomokuPlayer player) {
        int whiteVal = 0;
        int blackVal = 0;
        for(int i = 1; i <= 5; i++){
            blackVal += roadBoard.getRoads(i,0).size() * GameConst.ROAD_VALUES[i];
            whiteVal += roadBoard.getRoads(0,i).size() * GameConst.ROAD_VALUES[i];
        }
        if(player == GomokuPlayer.BLACK){
            return blackVal - whiteVal;
        } else if(player == GomokuPlayer.WHITE){
            return whiteVal - blackVal;
        }
        return -1;
    }

    public int evaluate(RoadBoard roadBoard) {
        return 0;
    }
}
