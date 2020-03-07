package com.amoyzhp.boardgame.game.sixinrow.component;

import com.amoyzhp.boardgame.game.sixinrow.constant.GameConst;
import com.amoyzhp.boardgame.game.sixinrow.core.Action;
import com.amoyzhp.boardgame.game.sixinrow.core.GameState;
import com.amoyzhp.boardgame.game.sixinrow.enums.Player;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MoveGenerator {

    private List<Pair<Integer, Integer>> getKillerMoves(Player player, Player[][] chessboard, RoadBoard roadBoard){
        List<Pair<Integer, Integer>> moveList = new ArrayList<>();
        List<Road> roads;
        roads = roadBoard.getRoadList(player,6);
        for(Road road: roads){
            moveList.addAll(road.getEmptyPos());
        }
        roads = roadBoard.getRoadList(player,5);
        for(Road road: roads){
            moveList.addAll(road.getEmptyPos());
        }
        roads = roadBoard.getRoadList(player,4);
        for(Road road: roads){
            moveList.addAll(road.getEmptyPos());
        }

        return moveList;
    }
    /*
    获取候选点的策略是
    1 如果我方有必胜点，则返回我方必胜点
    2 否则查找对方是否有必胜点
    3 都没有必胜点则搜索 3 和 2 路上的路
    4 还未找到点则搜索 1 路上的点
    5 由于已经固定了第一个落子的策略，所以排除了在 0 路上搜索的情况
        K 子棋的落点跟对方和我方的落点有很大关系，所以赞不考虑 0 路
     */
    private List<Pair<Integer, Integer>> getCandidateMoves(Player player, Player[][] chessboard,RoadBoard roadBoard){
        List<Pair<Integer, Integer>> moveList = new ArrayList<>();
        List<Road> roads = new ArrayList<>();
        // 我方必胜点
        moveList = this.getKillerMoves(player,chessboard,roadBoard);
        if(moveList.size() > 0){
            return moveList;
        }
        // 对方必胜点
        if(player == Player.BLACK){
            moveList = this.getKillerMoves(Player.WHITE,  chessboard, roadBoard);
        } else {
            moveList = this.getKillerMoves(Player.BLACK,  chessboard, roadBoard);
        }

        if(moveList.size() > 0){
            return moveList;
        }
        //把二路和三路上的点一起考虑
        roads.addAll(roadBoard.getRoadList(player,3));
        roads.addAll(roadBoard.getRoadList(player,2));
        Set<Integer> posSet = new HashSet<>();
        int index;
        if(roads.size() > 0){
            for(Road road: roads){
                List<Pair<Integer, Integer>> empty = road.getEmptyPos();
                for(Pair<Integer, Integer> pos : empty){
                    index = pos.getKey() * 1000 + pos.getValue();
                    if(posSet.contains(index) == false){
                        moveList.add(pos);
                        posSet.add(index);
                    }
                }
            }
            return moveList;
        }
        roads.addAll(roadBoard.getRoadList(player,1));
        if(roads.size() > 0){
            for(Road road: roads){
                moveList.addAll(road.getEmptyPos());
            }
        }
        // 最好不出现直接搜索 0 路的情况。太稀疏。
        return moveList;
    }

    public List<Action> getCandidateAction(GameState gameState, RoadBoard roadBoard, Player player){
        List<Action> actionList = new ArrayList<>();
        List<Pair<Integer, Integer>> moveList = new ArrayList<>();
        Player[][] chessboard = gameState.getChessboard();
        moveList.addAll(this.getCandidateMoves(player, chessboard, roadBoard));
        Set<Integer> actionSet = new HashSet<>();
        Action action;
        for(int i = 0; i < moveList.size(); i++){
            Pair<Integer, Integer> firstMove = moveList.get(i);
            for(int j = i + 1; j < moveList.size(); j++){
                Pair<Integer, Integer> secondMove = moveList.get(j);
                if(firstMove.getValue() != secondMove.getValue() ||
                        firstMove.getKey() != secondMove.getKey()){
                    action = new Action(firstMove.getKey(), firstMove.getValue(),
                            secondMove.getKey(), secondMove.getValue(), player);
                    // 去除重复的行动
                    if(actionSet.contains(action.hashCode()) == false){
                        actionList.add(action);
                    }
                }
            }
        }
        return actionList;
    }
}
