package com.amoyzhp.boardgame.game.sixinrow.component;

import com.amoyzhp.boardgame.game.sixinrow.constant.GameConst;
import com.amoyzhp.boardgame.game.sixinrow.core.GameState;

import java.util.List;

public class Evaluator {
    private static int[] ROAD_VAL = {0, 9, 520, 2070, 7890, 10020, 1000000};

    public int evaluate(RoadBoard roadBoard){
        List<Road> list;
        int blackVal = 0;
        int whiteVal = 0;
        for(int i = 1; i <= 6; i++){
            list = roadBoard.getRoadList(GameConst.BLACK, i);
            blackVal += (list.size() * ROAD_VAL[i]);
        }
        for(int i = 1; i <= 6; i++){
            list = roadBoard.getRoadList(GameConst.WHITE, i);
            whiteVal += (list.size() * ROAD_VAL[i]);
        }
        return blackVal - whiteVal;
    }

//    public int evaluate(GameState gameState) {
//        int[][] chessboard = gameState.getChessboard();
//        // 记录 0 ～ 6 子路的个数
//        int[] blackMap = new int[7];
//        int[] whiteMap = new int[7];
//        int blackCnt = 0;
//        int whiteCnt = 0;
//        //横
//        for(int[] rows : chessboard){
//            for(int num : rows){
//                if(num == GameConst.BLACK){
//                    blackCnt++;
//                    whiteMap[whiteCnt]++;
//                    // 等于六说明已经获胜直接返回最大值
//                    if(whiteCnt == 6){
//                        break;
//                    }
//                    whiteCnt = 0;
//                } else if(num == GameConst.WHITE){
//                    whiteCnt++;
//                    blackMap[blackCnt]++;
//                    if(blackCnt == 6){
//                        break;
//                    }
//                    blackCnt = 0;
//                } else if(num == GameConst.EMPTY){
//                    whiteMap[whiteCnt]++;
//                    whiteCnt = 0;
//                    blackMap[blackCnt]++;
//                    blackCnt = 0;
//                }
//            }
//        }
//        //竖
//        for(int i = 0; i < chessboard.length; i++){
//            for(int j = 0; j < chessboard[i].length; j++){
//                int num = chessboard[j][i];
//                if(num == GameConst.BLACK){
//                    blackCnt++;
//                    whiteMap[whiteCnt]++;
//                    // 等于六说明已经获胜直接返回最大值
//                    if(whiteCnt == 6){
//                        break;
//                    }
//                    whiteCnt = 0;
//                } else if(num == GameConst.WHITE){
//                    whiteCnt++;
//                    blackMap[blackCnt]++;
//                    if(blackCnt == 6){
//                        break;
//                    }
//                    blackCnt = 0;
//                } else if(num == GameConst.EMPTY){
//                    whiteMap[whiteCnt]++;
//                    whiteCnt = 0;
//                    blackMap[blackCnt]++;
//                    blackCnt = 0;
//                }
//            }
//        }
//        //左斜
//        for(int i = 0; i < chessboard.length;i++){
//            for(int j = 0; j < chessboard.length; j++){
//                if(i+j < chessboard.length){
//                    int num = chessboard[j][i+j];
//                    if(num == GameConst.BLACK){
//                        blackCnt++;
//                        whiteMap[whiteCnt]++;
//                        // 等于六说明已经获胜直接返回最大值
//                        if(whiteCnt == 6){
//                            break;
//                        }
//                        whiteCnt = 0;
//                    } else if(num == GameConst.WHITE){
//                        whiteCnt++;
//                        blackMap[blackCnt]++;
//                        if(blackCnt == 6){
//                            break;
//                        }
//                        blackCnt = 0;
//                    } else if(num == GameConst.EMPTY){
//                        whiteMap[whiteCnt]++;
//                        whiteCnt = 0;
//                        blackMap[blackCnt]++;
//                        blackCnt = 0;
//                    }
//                }
//            }
//        }
//        for(int i = 1; i < chessboard.length;i++){
//            for(int j = 0; j < chessboard.length; j++){
//                if(i+j < chessboard.length){
//                    int num = chessboard[i+j][j];
//                    if(num == GameConst.BLACK){
//                        blackCnt++;
//                        whiteMap[whiteCnt]++;
//                        // 等于六说明已经获胜直接返回最大值
//                        if(whiteCnt == 6){
//                            break;
//                        }
//                        whiteCnt = 0;
//                    } else if(num == GameConst.WHITE){
//                        whiteCnt++;
//                        blackMap[blackCnt]++;
//                        if(blackCnt == 6){
//                            break;
//                        }
//                        blackCnt = 0;
//                    } else if(num == GameConst.EMPTY){
//                        whiteMap[whiteCnt]++;
//                        whiteCnt = 0;
//                        blackMap[blackCnt]++;
//                        blackCnt = 0;
//                    }
//                }
//            }
//        }
//        //右斜
//        for(int i = 0; i < chessboard.length;i++){
//            for(int j = 0; j < chessboard.length; j++){
//                if(i-j >= 0){
//                    int num = chessboard[j][i-j];
//                    if(num == GameConst.BLACK){
//                        blackCnt++;
//                        whiteMap[whiteCnt]++;
//                        // 等于六说明已经获胜直接返回最大值
//                        if(whiteCnt == 6){
//                            break;
//                        }
//                        whiteCnt = 0;
//                    } else if(num == GameConst.WHITE){
//                        whiteCnt++;
//                        blackMap[blackCnt]++;
//                        if(blackCnt == 6){
//                            break;
//                        }
//                        blackCnt = 0;
//                    } else if(num == GameConst.EMPTY){
//                        whiteMap[whiteCnt]++;
//                        whiteCnt = 0;
//                        blackMap[blackCnt]++;
//                        blackCnt = 0;
//                    }
//                }
//            }
//        }
//        return 0;
//    }
}
