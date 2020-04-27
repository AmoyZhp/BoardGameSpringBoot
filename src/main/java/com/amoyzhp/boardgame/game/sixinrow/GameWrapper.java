package com.amoyzhp.boardgame.game.sixinrow;

import com.amoyzhp.boardgame.game.sixinrow.core.Agent;
import com.amoyzhp.boardgame.game.sixinrow.core.Environment;


public class GameWrapper {
    private Environment env;
    private Agent agent;

    public void init(){
        this.env = new Environment();
        this.agent = new Agent();
        this.env.init();
        this.agent.init();
    }

    public void setEnv(Environment env){
        this.env = env;
    }

    public void setAgent(Agent agent){
        this.agent = agent;
    }

    public Environment getEnv(){
        return this.env;
    }

    public Agent getAgent(){
        return this.agent;
    }

}
