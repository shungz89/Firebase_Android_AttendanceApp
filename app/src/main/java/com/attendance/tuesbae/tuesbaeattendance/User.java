package com.attendance.tuesbae.tuesbaeattendance;

/**
 * Created by 007 on 25-Nov-17.
 */
public class User {
    private double id;
    private String name, alt_name;
    private String status;
    private GameSession gamesession;

    public double getId() {
        return id;
    }

    public void setId(double id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAlt_name() {
        return alt_name;
    }

    public void setAlt_name(String alt_name) {
        this.alt_name = alt_name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public GameSession getGamesession() {
        return gamesession;
    }

    public void setGamesession(GameSession gamesession) {
        this.gamesession = gamesession;
    }
}
