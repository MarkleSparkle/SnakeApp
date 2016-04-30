package com.brainesgames.snake;

import android.util.Log;

/**
 * Created by Orson Baines on 2016-04-29.
 */
public class DrawBoard implements Runnable{
    String text;
    GameActivity activity;

    DrawBoard( GameActivity act,String txt){
        text=txt;
        activity=act;
    }

    void setText(String txt){
        text=txt;
    }

    @Override
    public void run() {
        activity.graphicsField.setText(text);
    }
}
