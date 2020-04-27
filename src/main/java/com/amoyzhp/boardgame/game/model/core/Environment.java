package com.amoyzhp.boardgame.game.model.core;
import com.amoyzhp.boardgame.game.model.common.Position;
import java.util.List;

/**
 * The Game Environment
 *
 * @Author: Tuseday Boy
 * @CreatedDate: 2020/3/6
 */
public interface Environment {
    /**
     * 初始化环境，主要是对环境进行初始化
     */
    void init();

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
    void close();

    /**
     * 传入一个随机种子，使初始化的环境具有随机性（如果环境需要随机性的话）
     */
    void seed(int seed);
}
