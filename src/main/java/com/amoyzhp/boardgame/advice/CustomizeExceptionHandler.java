package com.amoyzhp.boardgame.advice;

import com.amoyzhp.boardgame.dto.GeneralResponseDTO;
import com.amoyzhp.boardgame.exception.CustomizeErrorCode;
import com.amoyzhp.boardgame.exception.CustomizeException;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

@ControllerAdvice
public class CustomizeExceptionHandler {
    @ExceptionHandler(Exception.class)
    GeneralResponseDTO handle(HttpServletRequest request, HttpServletResponse response, Throwable ex, Model model){
        GeneralResponseDTO generalResponseDTO = new GeneralResponseDTO();
        if(ex instanceof CustomizeException){
            generalResponseDTO =  GeneralResponseDTO.getInstance((CustomizeException)ex);
        } else {
            generalResponseDTO =  GeneralResponseDTO.getInstance(CustomizeErrorCode.SYS_ERROR);
        }
        response.setStatus(500);
        return generalResponseDTO;
    }
}
