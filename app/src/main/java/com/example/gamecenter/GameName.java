package com.example.gamecenter;

public enum GameName {
    FLAPPYBIRDS("Juego de Flappy Bird"), GAME_2048("Juego de 2048");

    private String description;

    GameName(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

}
