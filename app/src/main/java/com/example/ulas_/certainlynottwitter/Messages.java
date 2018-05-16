package com.example.ulas_.certainlynottwitter;

import java.util.Date;

public class Messages {
    private int avatarIndex;
    private String username;
    private String message;
    private int ID;
    private String date;

    public Messages(int avatar,String username,String message,int id,String date){
        this.avatarIndex = avatar;
        this.username = username;
        this.message = message;
        this.ID = id;
        this.date = date;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public int getAvatarIndex() {
        return avatarIndex;
    }

    public void setAvatarIndex(int avatarIndex) {
        this.avatarIndex = avatarIndex;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
