package com.example.gamecenter;

import java.io.Serializable;

public class ScoreDTO implements Serializable {
    private GameDTO game;
    private UserDTO player;
    private int points;
    private int time;

    public ScoreDTO(GameDTO game, UserDTO player, int points, int time) {
        this.game = game;
        this.player = player;
        this.points = points;
        this.time = time;
    }

    public GameDTO getGame() { return game; }
    public UserDTO getPlayer() { return player; }
    public int getPoints() { return points; }

    public int getTime() {
        return time;
    }
    public void setTime(int time) {
        this.time = time;
    }
    public void setPoints(int points) {
        this.points = points;
    }
    public void setGame(GameDTO game) {
        this.game = game;
    }
    public void setPlayer(UserDTO player) {
        this.player = player;
    }

}

