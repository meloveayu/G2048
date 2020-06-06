package com.example.g2048;

import androidx.annotation.NonNull;

public class GameOverException extends RuntimeException {
    public GameOverException(String message){
        super(message);
    }

    @NonNull
    @Override
    public String toString() {
        return "游戏结束："+super.toString();
    }
}
