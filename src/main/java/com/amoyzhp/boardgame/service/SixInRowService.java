package com.amoyzhp.boardgame.service;

import com.amoyzhp.boardgame.dto.*;
import com.amoyzhp.boardgame.exception.CustomizeSuccessCode;
import com.amoyzhp.boardgame.game.sixinrow.Action;
import com.amoyzhp.boardgame.game.sixinrow.Agent;
import com.amoyzhp.boardgame.game.sixinrow.GameState;
import com.amoyzhp.boardgame.game.sixinrow.SixInRowEnv;
import com.amoyzhp.boardgame.game.sixinrow.constant.GameConst;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

@Service
public class SixInRowService {

    public SixInRowGameInfoDTO getNextAction(Action receivedAction, GameState receivedState, int requiredPlayer){
        SixInRowEnv env = new SixInRowEnv();
        env.setGameState(receivedState);
        Agent agent = new Agent();
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

    public GeneralResponseDTO endGame(SixInRowGameInfoDTO receivedDTO) {
        GeneralResponseDTO generalResponseDTO = new GeneralResponseDTO();
        if(receivedDTO.getGameStateDTO().isTerminal() == true){
            Action receivedAction = Action.fromActionDTO(receivedDTO.getActionDTO());
            GameState receivedState = GameState.fromGameStateDTO(receivedDTO.getGameStateDTO());
            if(receivedState.isTerminal()){
                generalResponseDTO.setCode(200);
                generalResponseDTO.setMessage("success");
            } else {
                generalResponseDTO.setCode(400);
                generalResponseDTO.setMessage("it is not terminal situation");
            }
        } else {
            generalResponseDTO.setCode(401);
            generalResponseDTO.setMessage("only call for this api when end game");
        }
        return generalResponseDTO;
    }
}
