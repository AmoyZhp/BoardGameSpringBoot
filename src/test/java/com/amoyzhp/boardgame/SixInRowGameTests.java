package com.amoyzhp.boardgame;

import com.amoyzhp.boardgame.game.sixinrow.*;

public class SixInRowGameTests {

    public void writeXmlFileTest(){
        GameWrapper wrapper = new GameWrapper();
        wrapper.init();
        SixInRowEnv env = wrapper.getEnv();
        Agent agent = wrapper.getAgent();
        agent.setPlayer(1);
        GameState gameState = env.getGameState();
        Action action = new Action();
        action.setX1(1);
        action.setY1(1);
        action.setX2(2);
        action.setY2(2);
        action.setPlayer(1);
        env.step(action);
        wrapper.writeGameToXml(env, agent);
    }

    public void readXml(){
        GameUtil.readGameHistoryXml("test.xml");
    }

    public static void main(String[] args) {
        SixInRowGameTests tests = new SixInRowGameTests();
        GameWrapper wrapper = GameUtil.readGameHistoryXml("test.xml");
        System.out.println(wrapper.getEnv().getGameState().getHistoryActions());
    }
}
