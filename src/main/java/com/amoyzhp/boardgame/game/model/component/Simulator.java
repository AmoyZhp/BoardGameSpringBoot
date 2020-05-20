package com.amoyzhp.boardgame.game.model.component;

import com.amoyzhp.boardgame.game.model.core.Action;
import com.amoyzhp.boardgame.game.model.core.State;

/**
 * interface of Simulator
 *
 * @Author: Tuseday Boy
 * @CreatedDate: 2020/03/30
 */
public interface Simulator {
    /**
     * 传入一个 Action，环境会基于这个 action 对当前的 state 作出反应
     * @param action  action 可以看作一个多维数组。特别地，在棋盘游戏中，action可以看作一个二维数组
     *                每一行是一个坐标 (x,y)。五子棋只有一行，六子棋有两行，围棋有多行。
     * @return 返回行动后的棋盘状态
     */
    State step(Action action);

    /**
     * 关闭环境，释放资源。（如果有的话）
     */
    State moveBack();
}
