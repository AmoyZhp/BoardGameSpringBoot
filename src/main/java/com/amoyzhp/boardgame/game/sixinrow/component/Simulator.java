package com.amoyzhp.boardgame.game.sixinrow.component;

import com.amoyzhp.boardgame.game.sixinrow.constant.GameConst;
import com.amoyzhp.boardgame.game.sixinrow.core.Action;
import com.amoyzhp.boardgame.game.sixinrow.core.GameState;
import com.amoyzhp.boardgame.game.sixinrow.enums.Player;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Simulator {

    private GameState gameState;
    private RoadBoard roadBoard;


    public void setGameState(GameState gameState){
        this.gameState = gameState;
        Player[][] chessboard = gameState.getChessboard();
        this.roadBoard = new RoadBoard();
        roadBoard.init();
        for(int i = 0; i < GameConst.BOARD_SIZE; i++){
            for(int j = 0; j < GameConst.BOARD_SIZE; j++){
                if(chessboard[i][j] != Player.EMPTY){
                    roadBoard.addPos(i,j,chessboard[i][j]);
                }
            }
        }
    }

    public RoadBoard getRoadBoard(){
        return this.roadBoard;
    }

    public GameState getGameState(){
        return gameState;
    }

    public void moveBack() {
        Action action = this.gameState.removeLastAction();
        this.gameState.setPos(action.getX1(),action.getY1(), Player.EMPTY);
        this.gameState.setPos(action.getX2(),action.getY2(), Player.EMPTY);
        this.roadBoard.removePos(action.getX1(), action.getY1(),action.getPlayer());
        this.roadBoard.removePos(action.getX2(), action.getY2(),action.getPlayer());
        this.gameState.setTimestep(this.gameState.getTimestep() - 1);
    }

    public GameState step(Action act){
        this.gameState.setPos(act.getX1(), act.getY1(), act.getPlayer());
        this.roadBoard.addPos(act.getX1(), act.getY1(), act.getPlayer());
        if(this.gameState.getTimestep() > 0){
            this.gameState.setPos(act.getX2(), act.getY2(), act.getPlayer());
            this.roadBoard.addPos(act.getX2(), act.getY2(), act.getPlayer());
        }
        this.gameState.addActionsInHistory(act);
        this.gameState.setTimestep(this.gameState.getTimestep() + 1);
        return this.gameState;
    }

}
