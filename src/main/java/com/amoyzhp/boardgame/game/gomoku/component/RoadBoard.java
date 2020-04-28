package com.amoyzhp.boardgame.game.gomoku.component;

import com.amoyzhp.boardgame.game.gomoku.enums.GomokuPlayer;
import com.amoyzhp.boardgame.game.model.common.Direction;
import com.amoyzhp.boardgame.game.model.common.Position;
import javafx.geometry.Pos;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * roadboard of gomoku
 *
 * @Author: Tuseday Boy
 * @CreatedDate: 2020/03/30
 */
public class RoadBoard {
    final Logger logger = LoggerFactory.getLogger(RoadBoard.class);
    private Road[][][] roadsPool;
    private List<List<Set<Road>>> roadsStatus;
    private int height;
    private int width;

    public RoadBoard(int[][] chessboard) {
        if(chessboard.length <= 0 && chessboard[0].length <= 0){
            return;
        }
        this.height = chessboard.length;
        this.width = chessboard[0].length;

        roadsStatus = new ArrayList<>(6);
        for(int i = 0; i < 6 ; i++){
            //初始化一行。第 i 行表示有 i 个黑子的路
            List<Set<Road>> row = new ArrayList<>(6);
            for(int j = 0; j < 6; j++){
                //初始化列，第 j 个表示有 j 个白子的路
                row.add(new HashSet<>());
            }
            roadsStatus.add(row);
        }

        // 第三维为 4 的原因是有 4 个方向
        roadsPool = new Road[height][width][4];
        Set<Road> zeroRoads = this.getRoads(0,0);
        for(int row = 0; row < height; row++){
            for(int col = 0; col < width; col++){
                for(Direction direction : Direction.values()){
                    if(row + direction.rowOffset()*4 < 0 || row + direction.rowOffset()*4 >= height ||
                        col + direction.colOffset()*4 < 0 || col + direction.colOffset()*4 >= width){
                        roadsPool[row][col][direction.getValue()] = new Road(row, col, direction, false);
                    } else {
                        roadsPool[row][col][direction.getValue()] = new Road(row, col, direction, true);
                        zeroRoads.add(roadsPool[row][col][direction.getValue()]);
                    }
                }
            }
        }
        for(int row = 0; row < height; row++){
            for(int col = 0; col < width; col++){
                if(chessboard[row][col] != GomokuPlayer.EMPTY.getValue()){
                    this.updateRoad(new Position(row,col),GomokuPlayer.paraseValue(chessboard[row][col]));
                }
            }
        }
    }

    public Set<Road> getRoads(int blackCnt, int whiteCnt){
        // 返回有 blackCnt 个黑子， whiteCnt 个白子的路。
        return this.roadsStatus.get(blackCnt).get(whiteCnt);
    }

    public Road getRoad(int row, int col, Direction direction){
        if(row < 0 || row >= this.height || col < 0 || col >= this.width){
            return null;
        }
        return this.roadsPool[row][col][direction.getValue()];
    }

    @Override
    public int hashCode() {
        assert false;
        return 0;
    }

    /**
     * 当玩家在 (row,col) 落子后，更新和 (row,col) 路的相关信息。
     * 主要就是更新四个方向上的路信息，需要沿着路正方向回退更新相关路。总共会更新 4*5 （如果没有非法路的情况）。
     * 因为一个点在一个方向上会被五条路共用
     * @param player 玩家
     * @return 更新是否成功
     */
    public boolean updateRoad(Position position, GomokuPlayer player) {
        int row = position.row();
        int col = position.col();
        boolean flag = true;
        Set<Road> roads;
        for(Direction direction : Direction.values()){
             for(int i = 0; i < 5; i++){
                 int rowPos = row - direction.rowOffset() * i;
                 int colPos = col - direction.colOffset() * i;
                 Road road = this.getRoad(rowPos, colPos, direction);
                 if(road == null || road.legal == false){
                     continue;
                 }
                 // 先把路从旧的位置移除
                 roads =  this.getRoads(road.getBlackStoneCnt(), road.getWhiteStoneCnt());
                 roads.remove(road);
                 if(player == GomokuPlayer.EMPTY){
                    // 如果玩家为空，则表示要添加空白位置
                    road.removeStone(position);
                    roads = this.getRoads(road.getBlackStoneCnt(), road.getWhiteStoneCnt());
                    roads.add(road);

                 } else if(player == GomokuPlayer.BLACK || player == GomokuPlayer.WHITE){
                     road.setStone(position, player);
                     roads = this.getRoads(road.getBlackStoneCnt(), road.getWhiteStoneCnt());
                     roads.add(road);
                 } else {
                     logger.debug(" player type error");
                     flag = false;
                 }
             }
        }
        return flag;
    }

    /**
     * 返回路上只有 i 个 player 玩家的棋子，没有其他玩家棋子的路上的空点。
     * @param i
     * @param player
     * @return 空的位置的集合
     */
    public Set<Position> getEmptyPosOnRoad(int i, GomokuPlayer player) {
        if(i < 0 || i > 5 ){
            return null;
        }
        Set<Position> result = new HashSet<>();
        if(player == GomokuPlayer.BLACK){
            for(Road road : this.roadsStatus.get(i).get(0)){
                result.addAll(road.getEmptyPoes());
            }
        } else {
            for(Road road : this.roadsStatus.get(0).get(i)){
                result.addAll(road.getEmptyPoes());
            }
        }
        return result;
    }

    public Set<Position> getPosOnLiveThreeRoad(GomokuPlayer player) {
        Set<Position> result = new HashSet<>();
        Set<Road> threeRoads = new HashSet<>();
        if(player == GomokuPlayer.BLACK){
            threeRoads = this.getRoads(3, 0);
        } else if (player == GomokuPlayer.WHITE){
            threeRoads = this.getRoads(0,3);
        } else {
            logger.debug("invalid player type");
        }

        for(Road road : threeRoads){
            if(road.getPattern() == 1110){
                // OXXXO 的形式
                result.addAll(road.getEmptyPoes());
            } else if(road.getPattern() == 11010){
                // OXXOXO 的形式
                int rowPos = road.getRowBegin() - road.getDirection().rowOffset();
                int colPos = road.getColBegin() - road.getDirection().colOffset();
                Road lastRoad = this.getRoad(rowPos, colPos, road.getDirection());
                if(lastRoad != null && lastRoad.getPattern() == 1101){
                    Set<Position> roadTemp = road.getEmptyPoes();
                    Set<Position> lastRoadTemp = lastRoad.getEmptyPoes();
                    int cnt = 0;
                    for(Position position : roadTemp){
                        if(lastRoadTemp.contains(position)){
                            result.add(position);
                            cnt += 1;
                        }
                    }
                    if(cnt > 1){
                        logger.debug("live three pos find error");
                    }
                }
            } else if(road.getPattern() == 10110){
                // OXOXXO 的形式
                int rowPos = road.getRowBegin() - road.getDirection().rowOffset();
                int colPos = road.getColBegin() - road.getDirection().colOffset();
                Road lastRoad = this.getRoad(rowPos, colPos, road.getDirection());
                if(lastRoad != null && lastRoad.getPattern() == 1011){
                    Set<Position> roadTemp = road.getEmptyPoes();
                    Set<Position> lastRoadTemp = lastRoad.getEmptyPoes();
                    int cnt = 0;
                    for(Position position : roadTemp){
                        if(lastRoadTemp.contains(position)){
                            result.add(position);
                            cnt += 1;
                        }
                    }
                    if(cnt > 1){
                        logger.debug("live three pos find error");
                    }
                }
            }
        }
        return result;
    }

    private class Road{
        private int rowBegin;
        private int colBegin;
        private Direction direction;
        //有些路在其方向上无法扩展五格，因此是非法的路
        private boolean legal;
        private int whiteStoneCnt;
        private int blackStoneCnt;
        private int pattern;
        private Set<Position> emptyPoes;
        private Map<Position, Integer> placedPoes;
        private Set<Position> legalPos;


        public Road(int rowBegin, int colBegin, Direction direction, boolean legal){
            this.rowBegin = rowBegin;
            this.colBegin = colBegin;
            this.direction = direction;
            this.legal = legal;
            this.whiteStoneCnt = 0;
            this.blackStoneCnt = 0;
            this.pattern = 0;
            this.emptyPoes = new HashSet<>();
            this.placedPoes = new HashMap<>();
            this.legalPos = new HashSet<>();
            for(int i = 0; i < 5; i++){
                int row = rowBegin + direction.rowOffset() * i;
                int col = colBegin + direction.colOffset() * i;
                this.legalPos.add(new Position(row, col));
            }
            for(int i = 0; i < 5; i++){
                int row = rowBegin + direction.rowOffset() * i;
                int col = colBegin + direction.colOffset() * i;
                this.emptyPoes.add(new Position(row, col));
            }
        }

        public Set<Position> getEmptyPoes(){
            return this.emptyPoes;
        }

        public boolean removeStone(Position position){
            if(this.legal == false){
                logger.debug(String.format("operate invalid row %d, col %d",position.row(), position.col()));
                return false;
            }
            if(this.legalPos.contains(position) == false){
                logger.debug(String.format("illegal position row %d, col %d",position.row(), position.col()));
                return false;
            }
            if(this.emptyPoes.contains(position)){
                logger.debug(String.format("position is already empty row %d, col %d",position.row(), position.col()));
                return false;
            } else {
                this.emptyPoes.add(position);
                int player = this.placedPoes.remove(position);
                this.updateRoadPattern(position, -1);

                if(player == GomokuPlayer.BLACK.getValue()){
                    blackStoneCnt --;
                } else if(player == GomokuPlayer.WHITE.getValue()){
                    whiteStoneCnt --;
                } else {
                    logger.debug("invalid player");
                }
            }

            return true;
        }

        public boolean setStone(Position position, GomokuPlayer player){

            if(this.legal == false){
                logger.debug(String.format("operate invalid row %d, col %d",position.row(), position.col()));
                return false;
            }

            if(this.legalPos.contains(position) == false){
                logger.debug(String.format("illegal position row %d, col %d",position.row(), position.col()));
                return false;
            }

            if(this.emptyPoes.contains(position) == false){
                logger.debug(String.format("position is not empty row %d, col %d",position.row(), position.col()));
                return false;
            } else {
                this.emptyPoes.remove(position);
                this.placedPoes.put(position, player.getValue());
                this.updateRoadPattern(position, 1);
                if(player == GomokuPlayer.BLACK){
                    blackStoneCnt ++;
                } else if(player == GomokuPlayer.WHITE){
                    whiteStoneCnt ++;
                } else {
                    logger.debug("invalid player");
                }
            }
            return  true;
        }

        private void updateRoadPattern(Position position, int remove){
            if(this.legal == false){
                logger.debug(String.format("operate invalid row %d, col %d",position.row(), position.col()));
                return;
            }
            if(remove != 1 && remove != -1){
                logger.debug(String.format(" invalid remove value %d",remove));
                return;
            }

            int patternOffset;
            if(this.direction == Direction.HORIZONTAL || this.direction == Direction.LEADING_DIAGONAL){
                patternOffset = position.col() - this.colBegin;
            } else {
                patternOffset = position.row() - this.rowBegin;
            }
            assert patternOffset >= 0 && patternOffset < 5;

            switch (patternOffset){
                case 0 : this.pattern += remove * 1; break;
                case 1 : this.pattern += remove * 10; break;
                case 2 : this.pattern += remove * 100; break;
                case 3 : this.pattern += remove * 1000; break;
                case 4 : this.pattern += remove * 10000; break;
                default: logger.debug("pattern offset error");break;
            }
        }

        public int getRowBegin() {
            return rowBegin;
        }

        public int getColBegin() {
            return colBegin;
        }

        public int getWhiteStoneCnt() {
            return whiteStoneCnt;
        }

        public int getBlackStoneCnt() {
            return blackStoneCnt;
        }

        public boolean isLegal(){
            return this.legal;
        }

        public Direction getDirection() {
            return direction;
        }

        public int getPattern(){
            return this.pattern;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Road road = (Road) o;
            return rowBegin == road.rowBegin &&
                    colBegin == road.colBegin &&
                    direction.getValue() == road.direction.getValue();
        }

        @Override
        public int hashCode() {
            return 100000 + rowBegin * 1000 + colBegin * 10 + direction.getValue();
        }
    }
}
