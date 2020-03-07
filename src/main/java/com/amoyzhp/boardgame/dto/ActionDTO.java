package com.amoyzhp.boardgame.dto;

import com.amoyzhp.boardgame.game.sixinrow.core.Action;
import lombok.Data;
import org.springframework.beans.BeanUtils;

@Data
public class ActionDTO {
    private int player;
    private int x1;
    private int y1;
    private int x2;
    private int y2;

    public ActionDTO(){

    }

    public ActionDTO(Action action){
        this.player = action.getPlayer().getValue();
        this.x1 = action.getX1();
        this.y1 = action.getY1();
        this.x2 = action.getX2();
        this.y2 = action.getY2();
    }

    @Override
    public String toString(){
        return "player: " + this.player + " x1 : " + this.x1 + " y1: " + this.y1 + " x2: " + this.x2 + " y2:" + this.y2;
    }

    public static ActionDTO fromRawAction(Action action){
        ActionDTO actionDTO = new ActionDTO();
        BeanUtils.copyProperties(action, actionDTO);
        return  actionDTO;
    }
}
