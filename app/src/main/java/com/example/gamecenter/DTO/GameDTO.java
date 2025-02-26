package com.example.gamecenter.DTO;

import java.io.Serializable;

public class GameDTO implements Serializable {
    private String name;
    private String description;

    public GameDTO(String name, String description){
        this.name = name;
        this.description = description;
    }

    public String getName(){
        return name;
    }

    public String getDescription(){
        return description;
    }
}
