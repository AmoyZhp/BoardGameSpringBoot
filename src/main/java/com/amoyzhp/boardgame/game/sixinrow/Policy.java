package com.amoyzhp.boardgame.game.sixinrow;

import com.amoyzhp.boardgame.game.sixinrow.constant.GameConst;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Policy {

    public List<Action> alphaBetaTreeSearch(GameState gameState, int player){
        List<Action> result = new ArrayList<>();

        return result;
    }

    // take random action
    public Action random(GameState gameState, int player){
        Action action = new Action();
        int x1;
        int y1;
        Random r = new Random();
        do{
            x1 = r.nextInt(GameConst.BOARD_SIZE);
            y1 = r.nextInt(GameConst.BOARD_SIZE);
            if(gameState.isLegalPos(x1,y1)){
                action.setX1(x1);
                action.setY1(y1);
                break;
            }
        }while (true);

        if(gameState.getTimestep() > 0){
            int x2;
            int y2;
            do{
                x2 = r.nextInt(GameConst.BOARD_SIZE);
                y2 = r.nextInt(GameConst.BOARD_SIZE);
                if(gameState.isLegalPos(x2,y2) && x1 != x2 && y1 != y2){
                    action.setX2(x2);
                    action.setY2(y2);
                    break;
                }
            }while (true);
        } else {
            action.setX2(-1);
            action.setY2(-1);
        }

        return action;
    }
}
