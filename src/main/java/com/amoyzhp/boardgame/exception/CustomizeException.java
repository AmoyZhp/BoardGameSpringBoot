package com.amoyzhp.boardgame.exception;

public class CustomizeException extends RuntimeException {
    private String message;
    private int code;
    public CustomizeException(ICustomizeCode errorCode){
        this.message = errorCode.getMessage();
        this.code = errorCode.getCode();
    }
    public CustomizeException(int code, String message){
        this.code = code;
        this.message = message;
    }


    @Override
    public String getMessage() {
        return this.message;
    }

    public int getCode(){return this.code;}
}
