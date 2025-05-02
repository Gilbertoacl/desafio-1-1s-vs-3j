package com.gilberto.amorim.DesafioCodecon.model.User;

public class LogUser {
    private String date;
    private String action;

    public LogUser() {}

    public LogUser(String date, String action) {
        this.date = date;
        this.action = action;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    @Override
    public String toString() {
        return "LogUser{" +
                "date='" + date + '\'' +
                ", action='" + action + '\'' +
                '}';
    }
}

