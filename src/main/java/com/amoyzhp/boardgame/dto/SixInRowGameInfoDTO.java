package com.amoyzhp.boardgame.dto;

import lombok.Data;

@Data
public class SixInRowGameInfoDTO {
    private ActionDTO actionDTO;
    private GameStateDTO gameStateDTO;
    private int requiredPlayer;
}
