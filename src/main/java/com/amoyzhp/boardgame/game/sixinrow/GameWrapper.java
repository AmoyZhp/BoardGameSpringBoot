package com.amoyzhp.boardgame.game.sixinrow;


public class GameWrapper {
    private SixInRowEnv env;
    private Agent agent;

    public void init(){
        this.env = new SixInRowEnv();
        this.agent = new Agent();
        this.env.init();
        this.agent.init();
    }

    public void setEnv(SixInRowEnv env){
        this.env = env;
    }

    public void setAgent(Agent agent){
        this.agent = agent;
    }

    public SixInRowEnv getEnv(){
        return this.env;
    }

    public Agent getAgent(){
        return this.agent;
    }

}
