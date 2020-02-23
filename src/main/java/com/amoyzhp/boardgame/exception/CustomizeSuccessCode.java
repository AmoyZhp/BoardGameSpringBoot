package com.amoyzhp.boardgame.exception;

public enum CustomizeSuccessCode implements ICustomizeCode {
    SUCCESS(2000,"success");
    private int code;
    private String message;
    CustomizeSuccessCode(int code, String message) {
        this.code = code;
        this.message = message;
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
