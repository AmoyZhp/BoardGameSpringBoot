package com.amoyzhp.boardgame.service;

import com.amoyzhp.boardgame.dto.*;
import com.amoyzhp.boardgame.exception.CustomizeErrorCode;
import com.amoyzhp.boardgame.exception.CustomizeSuccessCode;
import com.amoyzhp.boardgame.game.sixinrow.*;
import com.amoyzhp.boardgame.game.sixinrow.core.Action;
import com.amoyzhp.boardgame.game.sixinrow.core.Agent;
import com.amoyzhp.boardgame.game.sixinrow.core.GameState;
import com.amoyzhp.boardgame.game.sixinrow.core.Environment;
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

        Environment env = gameWrapper.getEnv();
        Agent agent = gameWrapper.getAgent();

        env.setGameState(receivedState);
        agent.setPlayer(requiredPlayer);

        GameState gameState = env.getGameState();

        Action action = agent.act(gameState);
        GameState nextGameState = env.step(action);

        SixInRowGameInfoDTO responseMessage = new SixInRowGameInfoDTO();
        responseMessage.setActionDTO(new ActionDTO(action));
        responseMessage.setGameStateDTO(new GameStateDTO(nextGameState));
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
                gameWrapper = new GameWrapper();
                gameWrapper.init();

                Environment env = gameWrapper.getEnv();
                Agent agent = gameWrapper.getAgent();
                env.setGameState(receivedState);
                agent.setPlayer(requiredPlayer);
                GameUtil.writeGameHistoryToXml(gameWrapper,"test.xml");
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

    public SixInRowGameInfoDTO getHistoryGame() {
        SixInRowGameInfoDTO gameInfoDTO = new SixInRowGameInfoDTO();
        GameWrapper wrapper = GameUtil.readGameHistoryXml("test.xml");
        GameState state = wrapper.getEnv().getGameState();
        gameInfoDTO.setGameStateDTO(new GameStateDTO(state));
        return gameInfoDTO;
    }
}
