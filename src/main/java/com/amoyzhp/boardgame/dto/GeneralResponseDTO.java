package com.amoyzhp.boardgame.dto;

import com.amoyzhp.boardgame.exception.CustomizeErrorCode;
import com.amoyzhp.boardgame.exception.CustomizeException;
import com.amoyzhp.boardgame.exception.CustomizeSuccessCode;
import lombok.Data;

@Data
public class GeneralResponseDTO {
    private int code;
    private String message;
    public GeneralResponseDTO(){
        this.code = 0;
        this.message = "";
    }
    public GeneralResponseDTO(int code, String message){
        this.code = code;
        this.message = message;
    }

    public static GeneralResponseDTO getInstance(CustomizeErrorCode errorCode){
        return new GeneralResponseDTO(errorCode.getCode(), errorCode.getMessage());
    }

    public static GeneralResponseDTO getInstance(CustomizeSuccessCode successCode){
        return new GeneralResponseDTO(successCode.getCode(), successCode.getMessage());
    }

    public static GeneralResponseDTO getInstance(CustomizeException ex){
        return new GeneralResponseDTO(ex.getCode(), ex.getMessage());
    }
}
