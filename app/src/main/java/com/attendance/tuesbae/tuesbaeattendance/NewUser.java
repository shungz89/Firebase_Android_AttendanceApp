package com.attendance.tuesbae.tuesbaeattendance;

import com.google.firebase.database.PropertyName;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by 007 on 17-Apr-18.
 */

public class NewUser {
    String Name;

    @PropertyName("Attended Game")
    String Attended_Game;

    @PropertyName("Alt Name")
    String Alt_Name;


        public NewUser(String Name, String altName, String attendedGame) {
            this.Name = Name;
            this.Alt_Name = altName;
            this.Attended_Game = attendedGame;
        }


}
