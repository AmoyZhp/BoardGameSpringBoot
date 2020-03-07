package com.amoyzhp.boardgame;

import com.amoyzhp.boardgame.game.sixinrow.*;
import com.amoyzhp.boardgame.game.sixinrow.core.Action;
import com.amoyzhp.boardgame.game.sixinrow.core.Agent;
import com.amoyzhp.boardgame.game.sixinrow.core.GameState;
import com.amoyzhp.boardgame.game.sixinrow.core.Environment;
import com.amoyzhp.boardgame.game.sixinrow.enums.Player;

public class SixInRowGameTests {

    public void writeXmlFileTest(){
        GameWrapper wrapper = new GameWrapper();
        wrapper.init();
        Environment env = wrapper.getEnv();
        Agent agent = wrapper.getAgent();
        agent.setPlayer(Player.BLACK);
        GameState gameState = env.getGameState();
        Action action = new Action();
        action.setX1(1);
        action.setY1(1);
        action.setX2(2);
        action.setY2(2);
        action.setPlayer(Player.BLACK);
        env.step(action);
        wrapper.writeGameToXml(env, agent);
    }

    public void readXml(){
        GameUtil.readGameHistoryXml("test.xml");
    }

    public static void main(String[] args) {
//        SixInRowGameTests tests = new SixInRowGameTests();
//        GameWrapper wrapper = GameUtil.readGameHistoryXml("test.xml");
//        System.out.println(wrapper.getEnv().getGameState().getHistoryActions());
        Action action = new Action(8,9,10,11,Player.BLACK);
        Action action2 = new Action(10,11,8,9,Player.BLACK);
        if(action.hashCode() == action2.hashCode()){
            System.out.println(action.hashCode());
        }
    }
}
