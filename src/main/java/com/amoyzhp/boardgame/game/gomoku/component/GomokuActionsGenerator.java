package com.amoyzhp.boardgame.game.gomoku.component;

import com.amoyzhp.boardgame.game.gomoku.core.GomokuAction;
import com.amoyzhp.boardgame.game.gomoku.enums.GomokuPlayer;
import com.amoyzhp.boardgame.game.model.common.Player;
import com.amoyzhp.boardgame.game.model.common.Position;
import com.amoyzhp.boardgame.game.model.core.Action;
import com.amoyzhp.boardgame.game.model.core.State;
import com.amoyzhp.boardgame.game.sixinrow.component.Road;

import java.util.*;

/**
 * implementation of actions generator
 *
 * @Author: Tuseday Boy
 * @CreatedDate: 2020/03/30
 */
public class GomokuActionsGenerator {

    private static GomokuActionsGenerator instance = new GomokuActionsGenerator();

    private GomokuActionsGenerator(){

    }

    public static GomokuActionsGenerator getInstance(){
        return instance;
    }

    public static enum  RequiredType {
        KILL,
        MCTS,
        TSS,
        AlphaBetaTS;
    }

    public List<Action> getActionByType(RoadBoard roadBoard, Player player, RequiredType type){
        if(type == RequiredType.KILL){
            return this.getKillAction(roadBoard, player);
        }

        if(type == RequiredType.MCTS){
            return this.getMCTSCandidateActions(roadBoard, player);
        }

        if(type == RequiredType.TSS){
            return this.getTSSCandidateActions(roadBoard, player);
        }

        if(type == RequiredType.AlphaBetaTS){
            return this.getAlphaBetaCandidateActions(roadBoard, player);
        }

        return null;
    }


    public List<Action> getKillAction(RoadBoard roadBoard, Player player) {

        List<Action> actions = new ArrayList<>();
        Set<Position> positions = new HashSet<>();
        //是否有在四路上的空点
        positions = roadBoard.getEmptyPosOnRoad(4, player);
        if (positions == null || positions.size() == 0){
            // 是否又在活三路上的空点
            positions = roadBoard.getPosOnLiveThreeRoad(player);
        }

        if (positions != null && positions.size() > 0){
            for(Position position : positions){
                actions.add(new GomokuAction(position, player));
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
    public List<Action> getAlphaBetaCandidateActions(RoadBoard roadBoard, Player player) {
        List<Action> actions = new ArrayList<>();
        Set<Position> positions = null;

        GomokuPlayer nextPlayer = GomokuPlayer.getNextPlayer(player);

        actions = this.getKillAction(roadBoard, player);
        if(actions != null && actions.size() > 0){
            return  actions;
        }

        actions = this.getKillAction(roadBoard, nextPlayer);
        if(actions != null && actions.size() > 0){
            return  actions;
        }

        positions = roadBoard.getEmptyPosOnRoad(4, player);
        if (positions.size() > 0){
            actions = this.posToAction(positions, player);
            return  actions;
        }

        positions = roadBoard.getEmptyPosOnRoad(4, nextPlayer);
        if (positions.size() > 0){
            actions = this.posToAction(positions, player);
            return  actions;
        }

        // 选择三 、 二路上的点，活三和四路上的点之前已经被选完了
        positions = roadBoard.getEmptyPosOnRoad(3, player);
        positions.addAll(roadBoard.getEmptyPosOnRoad(2,player));

        if (positions.size() > 0){
            actions = this.posToAction(positions, player);
            return  actions;
        }
        positions.addAll(roadBoard.getEmptyPosOnRoad(3, nextPlayer));
        positions.addAll(roadBoard.getEmptyPosOnRoad(2, nextPlayer));
        if (positions.size() > 0){
            actions = this.posToAction(positions, player);
            return  actions;
        }

        positions = roadBoard.getEmptyPosOnRoad(1, player);
        if (positions.size() > 0){
            actions = this.posToAction(positions, player);
            return  actions;
        }
        assert positions.size() > 0;
        return actions;
    }

    public List<Action> getTSSCandidateActions(RoadBoard roadBoard, Player player) {
        List<Action> result = new LinkedList<>();
        Set<Position> positions = new HashSet<>();
        positions.addAll(roadBoard.getPosOnLiveThreeRoad(player));
        positions.addAll(roadBoard.getPosOnLiveTwoRoad(player));
        result = this.posToAction(positions, player);
        return result;
    }

    public List<Action> getMCTSCandidateActions(RoadBoard roadBoard, Player player) {
        List<Action> actions = new ArrayList<>();
        Set<Position> positions = null;
        GomokuPlayer nextPlayer = GomokuPlayer.getNextPlayer(player);
        positions = roadBoard.getEmptyPosOnRoad(3, player);
        positions.addAll(roadBoard.getEmptyPosOnRoad(2,player));
        positions.addAll(roadBoard.getEmptyPosOnRoad(3, nextPlayer));
        positions.addAll(roadBoard.getEmptyPosOnRoad(2, nextPlayer));
        positions = roadBoard.getEmptyPosOnRoad(1, player);
        if (positions.size() > 0){
            actions = this.posToAction(positions, player);
            return  actions;
        }
        return actions;
    }

    private List<Action> posToAction(Set<Position> positions, Player player){
        List<Action> actions = new LinkedList<>();
        for(Position position : positions){
            actions.add(new GomokuAction(position, player));
        }
        return actions;
    }

}
