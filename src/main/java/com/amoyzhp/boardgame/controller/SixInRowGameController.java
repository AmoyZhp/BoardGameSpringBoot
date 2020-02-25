package com.amoyzhp.boardgame.controller;

import com.amoyzhp.boardgame.dto.GeneralResponseDTO;
import com.amoyzhp.boardgame.dto.SixInRowGameInfoDTO;
import com.amoyzhp.boardgame.exception.CustomizeErrorCode;
import com.amoyzhp.boardgame.game.sixinrow.Action;
import com.amoyzhp.boardgame.game.sixinrow.GameState;
import com.amoyzhp.boardgame.game.sixinrow.constant.GameConst;
import com.amoyzhp.boardgame.service.SixInRowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller
public class SixInRowGameController {
    @Autowired
    private SixInRowService sixInRowService;

    private static final Logger LOG = LoggerFactory.getLogger(SixInRowGameController.class);

    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @RequestMapping(value = "/sixinrow/getnextmove", method = RequestMethod.POST)
    @ResponseBody
    public SixInRowGameInfoDTO getNextMove(@RequestBody SixInRowGameInfoDTO receivedDTO){
        Action receivedAction = new Action(receivedDTO.getActionDTO());
        GameState receivedState = new GameState(receivedDTO.getGameStateDTO());
        int requiredPlayer = receivedDTO.getRequiredPlayer();
        SixInRowGameInfoDTO gameInfoDTO = sixInRowService.getNextAction(receivedAction, receivedState,
                requiredPlayer);
        return gameInfoDTO;
    }


    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @RequestMapping(value = "/sixinrow/endgame", method = RequestMethod.POST)
    @ResponseBody
    public GeneralResponseDTO endGame(@RequestBody SixInRowGameInfoDTO receivedDTO){
        Action receivedAction = new Action(receivedDTO.getActionDTO());
        GameState receivedState = new GameState(receivedDTO.getGameStateDTO());
        int requiredPlayer = receivedDTO.getRequiredPlayer();
        GeneralResponseDTO responseDTO = sixInRowService.endGame(receivedAction, receivedState, requiredPlayer);
        return responseDTO;
    }

    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @RequestMapping(value = "/sixinrow/startgame", method = RequestMethod.GET)
    @ResponseBody
    public GeneralResponseDTO startGame(@RequestParam(value="requiredPlayer") int requiredPlayer){
        GeneralResponseDTO responseDTO = new GeneralResponseDTO();
        if(isLegalPlayer(requiredPlayer)){
            responseDTO = sixInRowService.initGame(requiredPlayer);
        } else {
            LOG.debug(" Illegal requiredPlayer value {}", requiredPlayer);
            responseDTO = GeneralResponseDTO.getInstance(CustomizeErrorCode.DATA_ERROR);
        }
        return responseDTO;
    }

    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @RequestMapping(value = "/sixinrow/gethistorygame", method = RequestMethod.GET)
    @ResponseBody
    public SixInRowGameInfoDTO getHistoryGame(){
        return sixInRowService.getHistoryGame();
    }

    private boolean isLegalPlayer(int requiredPlayer){
        if(requiredPlayer == GameConst.BLACK || requiredPlayer == GameConst.WHITE){
            return true;
        } else {
            return false;
        }
    }
}
