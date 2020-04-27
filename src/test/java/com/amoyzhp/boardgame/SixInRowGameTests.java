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
    }

    public void readXml(){
        GameUtil.readGameHistoryXml("test.xml");
    }

    public static void main(String[] args) {
        GameState state = new GameState();
        state.init();
        System.out.println(state.hashCode());
        state.setPos(1,1,Player.EMPTY);
        System.out.println(state.hashCode());
        state.setPos(1,1,Player.BLACK);
        System.out.println(state.hashCode());
        state.setPos(1,1,Player.EMPTY);
        System.out.println(state.hashCode());
        state.setPos(1,1,Player.WHITE);
        System.out.println(state.hashCode());
        state.setPos(1,1,Player.EMPTY);
        System.out.println(state.hashCode());
        state.setPos(1,1,Player.WHITE);
        System.out.println(state.hashCode());
    }
}
