package com.amoyzhp.boardgame.controller;

import com.amoyzhp.boardgame.dto.GeneralResponseDTO;
import com.amoyzhp.boardgame.dto.SixInRowGameInfoDTO;
import com.amoyzhp.boardgame.exception.CustomizeErrorCode;
import com.amoyzhp.boardgame.game.sixinrow.core.Action;
import com.amoyzhp.boardgame.game.sixinrow.core.GameState;
import com.amoyzhp.boardgame.game.sixinrow.constant.GameConst;
import com.amoyzhp.boardgame.game.sixinrow.enums.Player;
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
        Player requiredPlayer = Player.paraseValue(receivedDTO.getRequiredPlayer());
        LOG.debug("recived action " + receivedAction);
        LOG.debug("recived state " + receivedState);
        LOG.debug("required player" + requiredPlayer);
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
        Player requiredPlayer = Player.paraseValue(receivedDTO.getRequiredPlayer());
        GeneralResponseDTO responseDTO = sixInRowService.endGame(receivedAction, receivedState, requiredPlayer);
        return responseDTO;
    }

    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @RequestMapping(value = "/sixinrow/startgame", method = RequestMethod.GET)
    @ResponseBody
    public GeneralResponseDTO startGame(@RequestParam(value="requiredPlayer") int requiredPlayer){
        GeneralResponseDTO responseDTO = new GeneralResponseDTO();
        Player player = Player.paraseValue(requiredPlayer);
        if(player != Player.ILLEGAL){
            responseDTO = sixInRowService.initGame(player);
        } else {
            LOG.debug(" Illegal requiredPlayer value {}", player.getValue());
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

}
