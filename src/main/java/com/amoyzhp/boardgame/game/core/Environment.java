package com.amoyzhp.boardgame.game.core;

import javafx.util.Pair;


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
     * @param action
     * @return return the next state and the received reward
     */
    StepMessage step(Action action);

    /**
     * 关闭环境，释放资源。（如果有的话）
     */
    void close();

    /**
     * 传入一个随机种子，使初始化的环境具有随机性（如果环境需要随机性的话）
     */
    void seed(int seed);
}
