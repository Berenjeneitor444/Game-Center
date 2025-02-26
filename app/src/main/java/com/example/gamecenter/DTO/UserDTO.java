package com.example.gamecenter.DTO;

import java.io.Serializable;

public class UserDTO implements Serializable {
    private final String userName;
    private final String passwd;

    public UserDTO(String userName, String passwd){
        this.userName = userName;
        this.passwd = passwd;
    }
    // user only for show scores
    public UserDTO(String userName){
        this.userName = userName;
        this.passwd = null;
    }

    public String getUserName(){
        return userName;
    }

    public String getPasswd(){
        return passwd;
    }
}
