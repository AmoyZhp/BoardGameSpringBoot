package com.amoyzhp.boardgame.game.core;


import java.util.List;

/**
 * Contain the necessary component of a game which could be seen as instance of a "game"
 *
 * @Author: Tuseday Boy
 * @CreatedDate: 2020/3/6
 */
public interface GameContainer {

    Environment getEnv();
    void setEnv(Environment env);

    /**
     * consider the multi player game, use list to contains agents
     * @return
     */
    List<Agent> getAgent();
    void addAgent(Agent agent);

    void init();

    /**
    * @DemoCode
    * public static void main(){
    *    GameContainer container = new GameContainer();
    *    container.init();
    *    Environment env = container.getEnv();
    *    Agent agent = container.getAgent();
    *    StepMessage stepMessage;
    *    Action action;
    *    double accumulatedReward = 0;
    *    for(int t = 0; t < episode; t++){
    *        stepMessage = env.reset();
    *        while(true){
    *            action = agent.act(stepMessage.getState());
    *            stepMessage = env.step(action);
    *            accumulatedReward += stepMessage.getReward();
    *            if(steMessage.getIsTerminal()){
    *                break;
    *            }
    *        }
    *    }
    * }
    */
}
