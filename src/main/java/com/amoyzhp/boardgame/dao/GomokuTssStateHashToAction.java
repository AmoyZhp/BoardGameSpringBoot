package com.amoyzhp.boardgame.dao;

import lombok.Data;

/**
 * the entity of GomokuTssStateHashToAction table
 *
 * @Author: Tuseday Boy
 * @CreatedDate: 2020/05/03
 */
@Data
public class GomokuTssStateHashToAction {
    private int id;
    private int stateHash;
    private int row;
    private int col;
    private int timeStep;

}
