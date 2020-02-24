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
    @Override
    public boolean equals(Object o){
        // 判断是否指向同一个对象
        if (this == o) return true;
        // 判断是不是同一个类型
        if(o == null || this.getClass() != o.getClass()) return false;

        Action that = (Action)o;
        if(this.x1 != that.getX1()) return false;
        if(this.y1 != that.getY1()) return false;
        if(this.x2 != that.getX2()) return false;
        if(this.y2 != that.getY2()) return false;
        if(this.player != that.getPlayer()) return false;
        return true;
    }

    @Override
    public int hashCode() {
        int result = 17;
        result = 31 * result + this.x1;
        result = 31 * result + this.y1;
        result = 31 * result + this.x2;
        result = 31 * result + this.y2;
        result = 31 * result + this.player;
        return result;
    }


}
