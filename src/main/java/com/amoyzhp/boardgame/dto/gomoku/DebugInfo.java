package com.amoyzhp.boardgame.dto.gomoku;

import lombok.Data;

/**
 * debug info object
 *
 * @Author: Tuseday Boy
 * @CreatedDate: 2020/05/01
 */
@Data
public class DebugInfo {
    private int abDepth;
    private int tssDepth;
    private int AItype;
    private long timeLimit;
}
