package com.example.gamecenter.DTO;

import java.io.Serializable;

public class ScoreDTO implements Serializable {
    private final GameDTO game;
    private final UserDTO player;
    private final int points;
    private final int time;

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

}

