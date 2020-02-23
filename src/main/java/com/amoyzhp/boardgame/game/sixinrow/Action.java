package com.amoyzhp.boardgame.game.sixinrow;

import com.amoyzhp.boardgame.dto.ActionDTO;
import lombok.Data;
import org.springframework.beans.BeanUtils;

@Data
public class Action {
    private int player;
    private int x1;
    private int y1;
    private int x2;
    private int y2;

    public static Action fromActionDTO(ActionDTO actionDTO){
        Action action = new Action();
        BeanUtils.copyProperties(actionDTO, action);
        return action;
    }
}
