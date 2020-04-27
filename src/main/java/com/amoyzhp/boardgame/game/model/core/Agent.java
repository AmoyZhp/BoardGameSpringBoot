package com.amoyzhp.boardgame.game.model.core;

import com.amoyzhp.boardgame.game.model.common.Player;
import com.amoyzhp.boardgame.game.model.common.Position;
import java.util.List;

/**
 * define acting agent
 *
 * @Author: Tuseday Boy
 * @CreatedDate: 2020/3/6
 */
public interface Agent {

    void init(Player player);

    /**
     *
     * @param state 是表示棋局状态
     * @return 返回执行的 action，action 可以看作一个多维数组。特别地，在棋盘游戏中，action可以看作一个二维数组
     *          每一行是一个坐标 (x,y)。五子棋只有一行，六子棋有两行，围棋有多行。
     */
    Action act(State state);

    Player getPlayer();
}
