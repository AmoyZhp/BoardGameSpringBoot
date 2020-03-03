package com.amoyzhp.boardgame.game.sixinrow.component;

import com.amoyzhp.boardgame.game.sixinrow.constant.GameConst;
import javafx.util.Pair;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

@Data
public class Road {
    final Logger logger = LoggerFactory.getLogger(RoadBoard.class);
    private int row;
    private int col;
    private int black;
    private int white;
    private int dir;
    private boolean active;
    private int index;
    private Set<Integer> legalPos;
    private Map<Integer, Pair<Integer, Integer>> emptyPos;

    public Road(int row, int col,int dir, int black, int white,int index, boolean active) {
        this.row = row;
        this.col = col;
        this.black = black;
        this.white = white;
        this.dir = dir;
        this.active = active;
        this.index = index;
        this.emptyPos = new HashMap<>();
        this.legalPos = new HashSet<>();
        for(int i = 0; i < 6 ;i++){
            int x = row + i*GameConst.DIRECTIONS[dir][0];
            int y = col + i*GameConst.DIRECTIONS[dir][1];
            this.legalPos.add(x*GameConst.BOARD_SIZE + y);
            Pair<Integer, Integer> pos = new Pair<Integer, Integer>(x, y);
            this.emptyPos.put(x*GameConst.BOARD_SIZE + y, pos);
        }
    }

    public void setPos(int x, int y, int player){
        this.increaseStoneCnt(player);
        int index = x*GameConst.BOARD_SIZE + y;
        if(this.emptyPos.containsKey(index)){
            // 加入该点后该位置就不空闲了
            this.emptyPos.remove(index);
        } else {
            logger.debug("road empty pos map index is not exist x  y " + x + " " + y);
        }
    }

    public void removePos(int x, int y, int player){
        this.decreaseStoneCnt(player);
        int index = x*GameConst.BOARD_SIZE + y;
        if(this.isLegal(x, y) && !this.emptyPos.containsKey(index)){
            //移除位置后该位置就空了，所以加入empty pos
            Pair<Integer, Integer> pos = new Pair<Integer, Integer>(x, y);
            this.emptyPos.put(index, pos);
        } else {
            logger.debug("位置不合法 : " + x + " " + y);
        }

    }

    public void increaseStoneCnt(int player){
        if(player == GameConst.BLACK){
            this.black++;
        } else if(player == GameConst.WHITE){
            this.white++;
        } else {
            System.out.println("the player is illegal");
        }
    }

    public void decreaseStoneCnt(int player){
        if(player == GameConst.BLACK){
            this.black--;
        } else if(player == GameConst.WHITE){
            this.white--;
        } else {
            System.out.println("the player is illegal");
        }
    }


    public List<Pair<Integer, Integer>> getEmptyPos(){
        List<Pair<Integer, Integer>> result = new ArrayList<>();
        for(int index : this.emptyPos.keySet()){
            result.add(this.emptyPos.get(index));
        }
        return result;
    }

    private boolean isLegal(int x, int y){
        return this.legalPos.contains(x*GameConst.BOARD_SIZE + y);
    }

}
