package com.amoyzhp.boardgame.game.sixinrow.component;

import com.amoyzhp.boardgame.game.sixinrow.core.Action;
import lombok.Data;

@Data
public class ActionNode {
    private Action action;
    private int val;

    public ActionNode(){

    }

    public ActionNode(Action action, int val){
        this.action = action;
        this.val = val;
    }
}
