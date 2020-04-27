package com.amoyzhp.boardgame.game.sixinrow.core;

import com.amoyzhp.boardgame.dto.connectsix.ActionDTO;
import com.amoyzhp.boardgame.game.sixinrow.enums.Player;
import lombok.Data;

@Data
public class Action {
    private Player player;
    private int x1;
    private int y1;
    private int x2;
    private int y2;

    public Action(int x1, int y1, int x2, int y2, Player player) {
        this.player = player;
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
    }

    public Action(){

    }

    public Action(ActionDTO actionDTO){
        this.player = Player.paraseValue(actionDTO.getPlayer());
        this.x1 = actionDTO.getX1();
        this.x2 = actionDTO.getX2();
        this.y1 = actionDTO.getY1();
        this.y2 = actionDTO.getY2();
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
        if(this.x1 < this.x2){
            result = 31 * result + this.x1;
            result = 31 * result + this.y1;
            result = 31 * result + this.x2;
            result = 31 * result + this.y2;
        } else if(this.x1 > this.x2){
            result = 31 * result + this.x2;
            result = 31 * result + this.y2;
            result = 31 * result + this.x1;
            result = 31 * result + this.y1;
        } else {
            // 此时 x1 = x2
            if (this.y1 < this.y2){
                result = 31 * result + this.x1;
                result = 31 * result + this.y1;
                result = 31 * result + this.x2;
                result = 31 * result + this.y2;
            } else {
                result = 31 * result + this.x2;
                result = 31 * result + this.y2;
                result = 31 * result + this.x1;
                result = 31 * result + this.y1;
            }
        }
        result = 31 * result + this.player.getValue();
        return result;
    }


}
