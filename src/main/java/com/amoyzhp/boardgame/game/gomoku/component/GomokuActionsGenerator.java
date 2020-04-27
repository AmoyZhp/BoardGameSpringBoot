package com.amoyzhp.boardgame.game.gomoku.component;

import com.amoyzhp.boardgame.game.gomoku.core.GomokuAction;
import com.amoyzhp.boardgame.game.gomoku.enums.GomokuPlayer;
import com.amoyzhp.boardgame.game.model.common.Position;
import com.amoyzhp.boardgame.game.model.core.Action;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * implementation of actions generator
 *
 * @Author: Tuseday Boy
 * @CreatedDate: 2020/03/30
 */
public class GomokuActionsGenerator {

    public List<Action> getKillAction(GomokuSimulator simulator, GomokuPlayer player) {

        List<Action> actions = new ArrayList<>();
        Set<Position> positions = new HashSet<>();
        RoadBoard roadBoard = simulator.getRoadBoard();
        //是否有在四路上的空点
        positions = roadBoard.getEmptyPosOnRoad(4, player);
        if (positions == null || positions.size() == 0){
            // 是否又在活三路上的空点
            positions = roadBoard.getPosOnLiveThreeRoad(player);
        }

        if (positions != null && positions.size() > 0){
            for(Position position : positions){
                actions.add(new GomokuAction(position.row(), position.col(), player));
            }
            return  actions;
        }
        return actions;
    }

    /*
        获取候选点的策略是
        1 如果我方有必胜点，则返回我方必胜点
        2 否则查找对方是否有必胜点
        3 都没有必胜点则搜索 3 和 2 路上的路
        4 还未找到点则搜索 1 路上的点
        5 由于已经固定了第一个落子的策略，所以排除了在 0 路上搜索的情况
            K 子棋的落点跟对方和我方的落点有很大关系，所以暂不考虑 0 路
    */
    public List<Action> getAlphaBetaCandidateActions(GomokuSimulator simulator, GomokuPlayer player) {
        List<Action> actions = new ArrayList<>();
        Set<Position> positions = null;
        RoadBoard roadBoard = simulator.getRoadBoard();
        GomokuPlayer nextPlayer = GomokuPlayer.getNextPlayer(player.getValue());

        actions = this.getKillAction(simulator, player);
        if(actions != null && actions.size() > 0){
            return  actions;
        }

        actions = this.getKillAction(simulator, nextPlayer);
        if(actions != null && actions.size() > 0){
            return  actions;
        }

        positions = roadBoard.getEmptyPosOnRoad(4, player);
        if (positions.size() > 0){
            for(Position position : positions){
                actions.add(new GomokuAction(position.row(), position.col(), player));
            }
            return  actions;
        }

        positions = roadBoard.getEmptyPosOnRoad(4, nextPlayer);
        if (positions.size() > 0){
            for(Position position : positions){
                actions.add(new GomokuAction(position.row(), position.col(), player));
            }
            return  actions;
        }

        // 选择三 、 二路上的点，活三和四路上的点之前已经被选完了
        positions = roadBoard.getEmptyPosOnRoad(3, player);
        positions.addAll(roadBoard.getEmptyPosOnRoad(2,player));
        positions.addAll(roadBoard.getEmptyPosOnRoad(3, nextPlayer));
        positions.addAll(roadBoard.getEmptyPosOnRoad(2, nextPlayer));
        if (positions.size() > 0){
            for(Position position : positions){
                actions.add(new GomokuAction(position.row(), position.col(), player));
            }
            return  actions;
        }

        positions = roadBoard.getEmptyPosOnRoad(1, player);
        if (positions.size() > 0){
            for(Position position : positions){
                actions.add(new GomokuAction(position.row(), position.col(), player));
            }
            return  actions;
        }
        assert positions.size() > 0;
        return actions;
    }
}
