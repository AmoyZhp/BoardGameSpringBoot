package com.amoyzhp.boardgame.exception;

/**
 * Gomoku gaming exception
 *
 * @Author: Tuseday Boy
 * @CreatedDate: 2020/03/09
 */
public class GomokuGameException extends RuntimeException {
    private String message;

    public GomokuGameException(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
