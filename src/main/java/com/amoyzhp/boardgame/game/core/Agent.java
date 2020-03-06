package com.amoyzhp.boardgame.game.core;

/**
 * define acting agent
 *
 * @Author: Tuseday Boy
 * @CreatedDate: 2020/3/6
 */
public interface Agent {
    void init();

    /**
     * received a state and return a action based on state
     * @param state
     * @return
     */
    Action act(State state);
}
