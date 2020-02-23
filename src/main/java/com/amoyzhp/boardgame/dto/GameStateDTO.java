package com.amoyzhp.boardgame.dto;

import com.amoyzhp.boardgame.game.sixinrow.GameState;
import lombok.Data;

@Data
public class GameStateDTO {
    private int[][] chessboard;
    private int timestep;
    private boolean isTerminal;

    public static GameStateDTO fromRawGameState(GameState gameState){
        GameStateDTO gameStateDTO = new GameStateDTO();
        gameStateDTO.setTimestep(gameState.getTimestep());
        gameStateDTO.setTerminal(gameState.isTerminal());
        gameStateDTO.setChessboard(gameState.getChessboard().clone());
        return  gameStateDTO;
    }
}
