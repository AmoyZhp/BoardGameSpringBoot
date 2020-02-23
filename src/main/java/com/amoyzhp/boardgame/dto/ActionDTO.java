package com.amoyzhp.boardgame.dto;

import com.amoyzhp.boardgame.game.sixinrow.Action;
import lombok.Data;
import org.springframework.beans.BeanUtils;

@Data
public class ActionDTO {
    private int player;
    private int x1;
    private int y1;
    private int x2;
    private int y2;


    public static ActionDTO fromRawAction(Action action){
        ActionDTO actionDTO = new ActionDTO();
        BeanUtils.copyProperties(action, actionDTO);
        return  actionDTO;
    }
}
