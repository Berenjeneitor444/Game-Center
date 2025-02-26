package com.example.gamecenter;

import java.io.Serializable;

public class UserDTO implements Serializable {
    private final String userName;
    private final String passwd;

    public UserDTO(String userName, String passwd){
        this.userName = userName;
        this.passwd = passwd;
    }

    public String getUserName(){
        return userName;
    }

    public String getPasswd(){
        return passwd;
    }
}
