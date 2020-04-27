package com.amoyzhp.boardgame.service;

import com.amoyzhp.boardgame.dto.gomoku.GomokuActionDTO;
import com.amoyzhp.boardgame.dto.gomoku.GomokuGameDTO;
import com.amoyzhp.boardgame.dto.gomoku.GomokuStateDTO;
import com.amoyzhp.boardgame.game.gomoku.core.*;
import com.amoyzhp.boardgame.game.gomoku.enums.GomokuPlayer;
import com.amoyzhp.boardgame.game.model.core.Action;
import com.amoyzhp.boardgame.game.model.core.State;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.LinkedList;


/**
 * gomoku service
 *
 * @Author: Tuseday Boy
 * @CreatedDate: 2020/03/26
 */
@Service
public class GomokuService {
    private static final Logger LOG = LoggerFactory.getLogger(GomokuService.class);

    public GomokuGameDTO getNextAction(State state, LinkedList<Action> historyActions,
                                       int requiredPlayer, int timestep) {
        // 将从前端得到的数据用于初始化 agent 和 环境
        GomokuAgent agent = new GomokuAgent(GomokuPlayer.paraseValue(requiredPlayer));
        GomokuEnvironment env = new GomokuEnvironment();

        env.init(state, historyActions, timestep);

        // 获取下一个行动和状态
        Action action = agent.act(state,timestep);
        State nextState = env.step(action);

        // 回传行动和状态
        GomokuGameDTO gameDTO = new GomokuGameDTO();
        gameDTO.setLastActionDTO(GomokuActionDTO.valueOfAction(action));
        gameDTO.setStateDTO(GomokuStateDTO.valueOfState(nextState));
        return gameDTO;
    }
}
