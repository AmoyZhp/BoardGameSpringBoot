package com.amoyzhp.boardgame.exception;

public enum CustomizeErrorCode implements ICustomizeCode {

    SYS_ERROR(5000,"Server System error"),
    DATA_ERROR(5001,"data error");
    private String message;
    private int code;

    CustomizeErrorCode(int code, String message){
        this.message = message;
        this.code = code;
    }
    @Override
    public String getMessage() {
        return this.message;
    }
    @Override
    public int getCode(){
        return this.code;
    }

}