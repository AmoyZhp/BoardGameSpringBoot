package com.amoyzhp.boardgame.game.core;

import java.util.Map;

/**
 * @program: com.amoyzhp.boardgame.game.core
 * @Author: Tuseday Boy
 * @Description: the return object by envrionment step function, which contains the required information
 * @Date: 2020-03-06
 */
public interface StepMessage {
    /**
     * 获取信息中存储的游戏状态，通常是执行 step 方法后的下一个状态
     * @return state
     */
    State getState();
    void setState();

    /**
     * 获取 reward 值
     * @return reward value
     */
    double getReward();
    void setReward(double reward);

    /**
     * message 中留存的 state 是不是终局
     * @return
     */
    boolean getIsTerminal();
    void setIsTerminal();

    /**
     * 一些额外需要说明的信息
     * @return
     */
    Map<String, String> getInfo();
    void setInfo(String key, String value);
}
