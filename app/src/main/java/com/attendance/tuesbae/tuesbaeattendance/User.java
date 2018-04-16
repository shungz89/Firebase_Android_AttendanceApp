package com.attendance.tuesbae.tuesbaeattendance;

import android.support.annotation.NonNull;

import java.util.Comparator;

/**
 * Created by 007 on 25-Nov-17.
 */
public class User implements Comparable<User>{
    private String ID;
    private String Name, Alt_Name;
    private String Status;
    private String Games_Attended;
    private GameSession Gamesession;

    public String getGames_Attended() {
        return Games_Attended;
    }

    public void setGames_Attended(String games_Attended) {
        Games_Attended = games_Attended;
    }

    public User(String name, String status) {
        this.Name = name;
        this.Status = status;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getName() {
        return this.Name;
    }

    public void setName(String name) {
        this.Name = name;
    }

    public String getAlt_Name() {
        return this.Alt_Name;
    }

    public void setAlt_Name(String alt_Name) {
        this.Alt_Name = alt_Name;
    }

    public String getStatus() {
        return this.Status;
    }

    public void setStatus(String status) {
        this.Status = status;
    }

//    public GameSession getGamesession() {
//        return Gamesession;
//    }
//
//    public void setGamesession(GameSession gamesession) {
//        Gamesession = gamesession;
//    }

    public static Comparator<User> UserNameComparator
            = new Comparator<User>() {

        public int compare(User user1, User user2) {

            String Name1 = user1.getName().toUpperCase();
            String Name2 = user2.getName().toUpperCase();

            //ascending order
            return Name1.compareTo(Name2);

            //descending order
            //return Name2.compareTo(Name1);
        }

    };


    @Override
    public int compareTo(@NonNull User o) {
        return 0;
    }
}
