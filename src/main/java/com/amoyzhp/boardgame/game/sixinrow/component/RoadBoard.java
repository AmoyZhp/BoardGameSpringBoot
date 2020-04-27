package com.amoyzhp.boardgame.game.sixinrow.component;


import com.amoyzhp.boardgame.game.sixinrow.constant.GameConst;
import com.amoyzhp.boardgame.game.sixinrow.enums.Direction;
import com.amoyzhp.boardgame.game.sixinrow.enums.Player;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RoadBoard {
    final Logger logger = LoggerFactory.getLogger(RoadBoard.class);
    //用来存放所有的路，可以想成一个资源池
    // container 和 list 的路都是这个的引用
    private Map<Direction, Road[][]> roadPool;
    //下标 i，j 表示 i 个黑子 j 个白子的路
    private List<List<Map<Integer, Road>>> roadMatrix;
    public RoadBoard(){

    }

    public void init(){
        this.roadPool = new HashMap<>();
        for(Direction dir : Direction.values()){
            roadPool.put(dir, new Road[GameConst.BOARD_SIZE][GameConst.BOARD_SIZE]);
        }
        this.roadMatrix = new ArrayList<>();
        for(int i = 0; i < 7 ; i++){
            //初始化一行。第 i 行表示有 i 个黑子的路
            List<Map<Integer, Road>> row = new ArrayList<>();
            for(int j = 0; j < 7; j++){
                //初始化列，第 j 个表示有 j 个白子的路
                row.add(new HashMap<>());
            }
            roadMatrix.add(row);
        }
        for(int i = 0; i < GameConst.BOARD_SIZE; i++){
            for(int j = 0; j < GameConst.BOARD_SIZE; j++){
                for(Direction dir : Direction.values()){
                    Road road = new Road(i,j,dir,0,0,i*1000 +j*10 + dir.getValue(),false);
                    int roadEndRow = i + 5 * dir.rowOffset();
                    int roadEndCol = j + 5 * dir.colOffset();
                    if(roadEndRow >= 0 && roadEndRow < GameConst.BOARD_SIZE
                            && roadEndCol >= 0 && roadEndCol < GameConst.BOARD_SIZE){
                        road.setActive(true);
                        this.addToRoadList(road);
                    }
                    this.roadPool.get(dir)[i][j] = road;
                }
            }
        }
    }

    public void addPos(int x, int y, Player player){
        // 表示在 x,y 处 player 落子
        for(Direction dir : Direction.values()){
           for(int i = 0; i < 6; i++){
               // 起点是 row，col 的路
               int row = x - i * dir.rowOffset();
               int col = y - i * dir.colOffset();
               if(row >= 0 && row < GameConst.BOARD_SIZE
                       && col >= 0 && col < GameConst.BOARD_SIZE){
                   Road road = this.roadPool.get(dir)[row][col];
                   if(road.isActive()){
                       this.removeFromRoadList(road);
                       //从上面加入 x,y 节点
                       road.setPos(x,y,player);
                       this.addToRoadList(road);
                   }
               }
           }
        }
    }

    public void removePos(int x, int y, Player player){
        for(Direction dir : Direction.values()){
            for(int i = 0; i < 6; i++){
                int row = x - i * dir.rowOffset();
                int col = y - i * dir.colOffset();
                // 找出起点是 row，col 的路
                if(row >= 0 && row < GameConst.BOARD_SIZE
                        && col >= 0 && col < GameConst.BOARD_SIZE){
                    Road road = this.roadPool.get(dir)[row][col];
                    if(road.isActive()){
                        this.removeFromRoadList(road);
                        //从上面移除 x,y 节点
                        road.removePos(x,y,player);
                        this.addToRoadList(road);
                    }
                }
            }
        }
    }

    //返回纯色的路
    public List<Road> getRoadList(Player player, int cnt){
        if(player == Player.BLACK){
            return new ArrayList<>(this.roadMatrix.get(cnt).get(0).values());
        }else {
            return new ArrayList<>(this.roadMatrix.get(0).get(cnt).values());
        }
    }

    public void addToRoadList(Road road){
        Map<Integer,Road> map =  this.roadMatrix.get(road.getBlack()).get(road.getWhite());
        if(map.containsKey(road.getIndex()) == false){
            map.put(road.getIndex(), road);
        } else {
            logger.debug("road index error {}, map already contains this key", road.getIndex());
        }
    }

    // 把road从原本的位置上移除
    public void removeFromRoadList(Road road){
        // 把最后一个换到被移除的位置上
        Map<Integer,Road> map =  this.roadMatrix.get(road.getBlack()).get(road.getWhite());
        if(map.containsKey(road.getIndex())){
           map.remove(road.getIndex());
        } else {
            logger.debug("road index error {}", road.getIndex());
        }

    }


    @Override
    public int hashCode() {
        int result = 17;
        for(int i = 0; i < GameConst.BOARD_SIZE; i++){
            for(int j = 0; j < GameConst.BOARD_SIZE; j++){
                for(Direction dir : Direction.values()){
                    Road road = this.roadPool.get(dir)[i][j];
                    result = 31 * result + road.getWhite();
                    result = 31 * result + road.getBlack();
                    result = 31 * result + road.getDir().getValue();
                    result = 31 * result + road.getIndex();
                }
            }
        }
        for(int i = 0; i < 7 ; i ++){
            for(int j = 0; j < 7; j++){
                result = 31 * result + this.roadMatrix.get(i).get(j).size();
            }
        }
        return result;
    }
}
