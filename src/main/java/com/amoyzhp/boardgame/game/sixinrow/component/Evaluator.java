package com.amoyzhp.boardgame.game.sixinrow.component;

import com.amoyzhp.boardgame.game.sixinrow.enums.Player;

import java.util.List;

public class Evaluator {
    private static int[] ROAD_VAL = {0, 9, 520, 2070, 7890, 10020, 100000};

    public int evaluate(RoadBoard roadBoard){
        List<Road> list;
        int blackVal = 0;
        int whiteVal = 0;
        for(int i = 1; i <= 6; i++){
            list = roadBoard.getRoadList(Player.BLACK, i);
            blackVal += (list.size() * ROAD_VAL[i]);
        }
        for(int i = 1; i <= 6; i++){
            list = roadBoard.getRoadList(Player.WHITE, i);
            whiteVal += (list.size() * ROAD_VAL[i]);
        }
        return blackVal - whiteVal;
    }
}
