package com.amoyzhp.boardgame.controller;

import com.amoyzhp.boardgame.dto.GeneralResponseDTO;
import com.amoyzhp.boardgame.dto.gomoku.GomokuActionDTO;
import com.amoyzhp.boardgame.dto.gomoku.GomokuGameDTO;
import com.amoyzhp.boardgame.dto.gomoku.GomokuStateDTO;
import com.amoyzhp.boardgame.game.gomoku.core.GomokuAction;
import com.amoyzhp.boardgame.game.gomoku.core.GomokuState;
import com.amoyzhp.boardgame.game.model.core.Action;
import com.amoyzhp.boardgame.service.GomokuService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedList;
import java.util.List;

/**
 * controller of gomoku
 *
 * @Author: Tuseday Boy
 * @CreatedDate: 2020/03/26
 */
@Controller
public class GomokuController {
    private static final Logger LOG = LoggerFactory.getLogger(GomokuController.class);
    @Autowired
    private GomokuService service;

    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @RequestMapping(value = "/gomoku/getnextmove", method = RequestMethod.POST)
    @ResponseBody
    public GomokuGameDTO getNextAction(@RequestBody GomokuGameDTO receivedDTO){
        List<GomokuActionDTO>  historyActionsDTO = receivedDTO.getHistoryActionsDTO();
        int requiredPlayer = receivedDTO.getRequiredPlayer();
        GomokuStateDTO stateDTO = receivedDTO.getStateDTO();
        int timestep = receivedDTO.getTimestep();
        GomokuState state = GomokuState.valueOfDTO(stateDTO);

        LinkedList<Action> historyActions = new LinkedList<>();
        for(GomokuActionDTO action : historyActionsDTO){
            historyActions.addLast(GomokuAction.valueOfDTO(action));
        }
        GomokuGameDTO gameDTO = service.getNextAction(state, historyActions, requiredPlayer, timestep);
        return gameDTO;
    }

    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @RequestMapping(value = "/gomoku/startgame", method = RequestMethod.GET)
    @ResponseBody
    public GeneralResponseDTO startGame(@RequestParam(value="aiPlayer") int aiPlayer){

        GeneralResponseDTO responseDTO = new GeneralResponseDTO();
        responseDTO.setMessage("success");
        responseDTO.setCode(200);
        return responseDTO;
    }

    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @RequestMapping(value = "/gomoku/endgame", method = RequestMethod.POST)
    @ResponseBody
    public GeneralResponseDTO endGame(){
        GeneralResponseDTO responseDTO = new GeneralResponseDTO();
        responseDTO.setMessage("success");
        responseDTO.setCode(200);
        return responseDTO;
    }
}
