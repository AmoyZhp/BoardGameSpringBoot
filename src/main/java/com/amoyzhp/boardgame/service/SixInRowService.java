package com.amoyzhp.boardgame.service;

import com.amoyzhp.boardgame.advice.CustomizeExceptionHandler;
import com.amoyzhp.boardgame.dto.*;
import com.amoyzhp.boardgame.exception.CustomizeErrorCode;
import com.amoyzhp.boardgame.exception.CustomizeSuccessCode;
import com.amoyzhp.boardgame.game.sixinrow.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class SixInRowService {
    private static final Logger LOG = LoggerFactory.getLogger(SixInRowService.class);
    private GameWrapper gameWrapper;

    public SixInRowGameInfoDTO getNextAction(Action receivedAction, GameState receivedState, int requiredPlayer){


        gameWrapper = new GameWrapper();
        gameWrapper.init();

        SixInRowEnv env = gameWrapper.getEnv();
        Agent agent = gameWrapper.getAgent();


        env.setGameState(receivedState);
        agent.setPlayer(requiredPlayer);

        GameState gameState = env.getGameState();

        Action action = agent.act(gameState);
        GameState nextGameState = env.step(action);

        SixInRowGameInfoDTO responseMessage = new SixInRowGameInfoDTO();
        responseMessage.setActionDTO(ActionDTO.fromRawAction(action));
        responseMessage.setGameStateDTO(GameStateDTO.fromRawGameState(nextGameState));
        return responseMessage;
    }

    public GeneralResponseDTO initGame(int player) {
        Agent agent = new Agent();
        agent.setPlayer(player);
        GeneralResponseDTO generalResponseDTO = GeneralResponseDTO.getInstance(CustomizeSuccessCode.SUCCESS);
        return generalResponseDTO;
    }

    public GeneralResponseDTO endGame(Action receivedAction, GameState receivedState, int requiredPlayer) {
        GeneralResponseDTO generalResponseDTO = new GeneralResponseDTO();
        if(receivedState.isTerminal()){
            if(receivedState.checkTerminal()){
                generalResponseDTO = GeneralResponseDTO.getInstance(CustomizeSuccessCode.SUCCESS);
                // 执行写文件操作
            } else {
                generalResponseDTO = GeneralResponseDTO.getInstance(CustomizeErrorCode.DATA_ERROR);
                generalResponseDTO.setMessage("前后端计算 terminal 状态不匹配");
            }
        } else {
            generalResponseDTO.setCode(401);
            generalResponseDTO.setMessage("only call for this api when end game");
        }
        return generalResponseDTO;
    }
}
