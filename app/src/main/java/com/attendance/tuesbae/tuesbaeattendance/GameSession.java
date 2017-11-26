package com.attendance.tuesbae.tuesbaeattendance;

import java.util.Date;
import java.util.List;

/**
 * Created by 007 on 25-Nov-17.
 */
public class GameSession {

    Date date;
    User user;
    List <User> userList;


    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<User> getUserList() {
        return userList;
    }

    public void setUserList(List<User> userList) {
        this.userList = userList;
    }

    public void addUsertoList(User user){
        userList.add(user);
    }

    public void removeUserfromList(User user){
        userList.remove(user);
    }
}
