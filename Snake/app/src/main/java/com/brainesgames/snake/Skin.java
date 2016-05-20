package com.brainesgames.snake;

import android.content.SharedPreferences;
import android.widget.RadioButton;

/**
 Created by Orson "orsahnn" Baines on 19/05/2016.
 */
public class Skin {
    SharedPreferences highscorePrefs;
    RadioButton button;
    String speedStr;
    int minimum;

    Skin(SharedPreferences highscorePrefs, RadioButton button,String speedStr,int minimum){
        this.button=button;
        this.speedStr=speedStr;
        this.minimum=minimum;
        this.highscorePrefs=highscorePrefs;
    }

    //disables any buttons that have not been unlocked
    void disableLocked(){
        button.setEnabled(highscorePrefs.getInt("high"+speedStr,-1)>=minimum);
    }

    boolean isLocked(){
        return highscorePrefs.getInt("high"+speedStr,-1)<minimum;
    }

    String lockMessage(){
        return "You need to get at least "+minimum+" on "+speedStr+" to unlock this skin";
    }
}
