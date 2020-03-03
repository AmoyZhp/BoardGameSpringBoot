package com.amoyzhp.boardgame.game.sixinrow.component;

import com.amoyzhp.boardgame.game.sixinrow.constant.GameConst;
import com.amoyzhp.boardgame.game.sixinrow.core.Action;
import com.amoyzhp.boardgame.game.sixinrow.core.GameState;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MoveGenerator {

    private List<Pair<Integer, Integer>> getKillerMoves(int player, int[][] chessboard, RoadBoard roadBoard){
        List<Pair<Integer, Integer>> moveList = new ArrayList<>();
        List<Road> roads;
        roads = roadBoard.getRoadList(player,6);
        if(roads.size() > 0){
            for(Road road: roads){
                moveList.addAll(road.getEmptyPos());
            }
            //找到六路上的点说明我们必赢了
            return moveList;
        }
        roads = roadBoard.getRoadList(player,5);
        if(roads.size() > 0){
            for(Road road: roads){
                moveList.addAll(road.getEmptyPos());
            }
            //找到5路上的点也说明我们必赢了
            return moveList;
        }
        roads = roadBoard.getRoadList(player,4);
        if(roads.size() > 0){
            for(Road road: roads){
                moveList.addAll(road.getEmptyPos());
            }
            //找到4路上的点也说明我们必赢了
            return moveList;
        }
        return moveList;
    }

    private List<Pair<Integer, Integer>> getCandidateMoves(int player, int[][] chessboard,RoadBoard roadBoard){
        List<Pair<Integer, Integer>> moveList = new ArrayList<>();
        List<Road> roads = new ArrayList<>();
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

    public List<Action> getCandidateAction(GameState gameState, RoadBoard roadBoard, int player){
        List<Action> actionList = new ArrayList<>();
        List<Pair<Integer, Integer>> moveList = new ArrayList<>();
        int[][] chessboard = gameState.getChessboard();
        moveList.addAll(this.getCandidateMoves(player, chessboard, roadBoard));
        Set<Integer> actionSet = new HashSet<>();
        Action action;
        for(int i = 0; i < moveList.size(); i++){
            Pair<Integer, Integer> firstMove = moveList.get(i);
            for(int j = i + 1; j < moveList.size(); j++){
                Pair<Integer, Integer> secondMove = moveList.get(j);
                if(firstMove.getValue() != secondMove.getValue() &&
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
