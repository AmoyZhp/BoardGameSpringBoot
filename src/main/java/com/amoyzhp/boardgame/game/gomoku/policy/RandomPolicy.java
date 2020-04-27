package com.amoyzhp.boardgame.game.gomoku.policy;

import com.amoyzhp.boardgame.game.gomoku.constant.GameConst;
import com.amoyzhp.boardgame.game.gomoku.core.GomokuAction;
import com.amoyzhp.boardgame.game.gomoku.core.GomokuState;
import com.amoyzhp.boardgame.game.gomoku.enums.GomokuPlayer;
import com.amoyzhp.boardgame.game.model.common.Player;
import com.amoyzhp.boardgame.game.model.common.Position;
import com.amoyzhp.boardgame.game.model.core.Action;
import com.amoyzhp.boardgame.game.model.core.State;
import com.amoyzhp.boardgame.game.model.policy.Policy;

import java.util.List;
import java.util.Random;

/**
 * policy
 *
 * @Author: Tuseday Boy
 * @CreatedDate: 2020/03/26
 */
public class RandomPolicy implements Policy {

    @Override
    public Action getAction(State state, Player player) {
        GomokuAction action = null;
        Random r = new Random();
        do{
            int row = r.nextInt(GameConst.HEIGHT);
            int col = r.nextInt(GameConst.WIDTH);
            if(state.isLegalPos(new Position(row,col),player)){
                action = new GomokuAction(row, col, GomokuPlayer.paraseValue(player.getValue()));
                break;
            }
        }while (true);

        return  action;
    }
}
