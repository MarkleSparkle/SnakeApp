package com.brainesgames.snake;

import android.app.Activity;
import android.content.SharedPreferences;
import android.util.Log;
import android.util.TypedValue;

import com.brainesgames.ascii.AsciiCanvas;

import java.io.InputStream;

/**
 * Created by Orson Baines on 2016-04-27.
 */
public class GameLoop implements Runnable{
    static final long INTERVAL_SLOW=150;
    static final long INTERVAL_NORMAL=125;
    static final long INTERVAL_FAST=100;
    static final long INTERVAL_EXTREME=75;

    static final int SPEED_SLOW=0;
    static final int SPEED_NORMAL=1;
    static final int SPEED_FAST=2;
    static final int SPEED_EXTREME=3;
    static final int SPEED_DYNAMIC=4;
    static final int SPEED_RANDOM=5;

    final GameActivity activity;
    volatile boolean isPaused;
    volatile int direction;

    GameLoop(GameActivity activity){
        this.activity=activity;
        isPaused=false;
        direction=0;
    }

    @Override
    public void run() {
        Log.d("GameLoop","Beginning Game Loop");

        SnakeBoard board=new SnakeBoard();
        SnakeCanvas sc=new SnakeCanvas(board);
        AsciiCanvas canvas=new AsciiCanvas(sc.canvas.getWidth(),sc.canvas.getHeight()+2);
        int speed=getSpeed();
        long interval=getInitialInterval(speed);
        int highscore=activity.highscorePrefs.getInt("high"+modeStr(speed),1);

        final float limitSize=Math.min((float)activity.screenWidth/canvas.getWidth()*1.6f, (float) activity.screenHeight / canvas.getHeight() * 0.8f);

        Log.d("GameLoop","lswid: "+(float)activity.screenWidth/canvas.getWidth()*1.6f);
        Log.d("GameLoop", "lshei: " + (float) activity.screenHeight / canvas.getHeight() * 0.8f);
        Log.d("GameLoop", "ls" + limitSize);
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                activity.graphicsField.setTextSize(TypedValue.COMPLEX_UNIT_PX, limitSize);
            }
        });


        while(!board.done){
            if(!isPaused){
                int sizeBefore = board.snake.size();
                board.setDirection(direction);
                board.move();
                int sizeNow=board.snake.size();
                highscore=updateHigh(sizeNow,highscore,speed);

                if(board.done){
                    drawGame(sc,canvas,"GAME OVER.  TAP TO TRY AGAIN","FINAL SCORE: " + sizeNow + "   HIGHSCORE: " + highscore);
                }
                else{
                    drawGame(sc,canvas,"SCORE: " + sizeNow + "   HIGHSCORE: " + highscore,"");

                    if (sizeNow > sizeBefore) {
                        if (speed == SPEED_RANDOM) {
                            //set and apply new interval
                            interval = SnakeBoard.r.nextInt(125) + 50;
                        } else if (speed == SPEED_DYNAMIC) {
                            if (sizeNow % 5 == 0 && interval > 35) {
                                interval -= 10;
                            }
                        }
                    }
                }
            }
            else{
                drawPause(canvas);
                try {
                    synchronized (this) {
                        wait();
                    }
                } catch (InterruptedException e) {
                    Log.d("GameLoop", "Thread interrupted");
                }
            }
            try {
                Thread.sleep(interval);
            } catch (InterruptedException e) {
                Log.d("GameLoop","Thread interrupted");
            }
        }

        activity.gameEnd();
    }

    void drawGame(SnakeCanvas sc,AsciiCanvas canvas,String line0,String line1){
        //clear the first two line of canvas (rest will be overridden by sc)
        canvas.drawHLine(0, 0, canvas.getWidth(), ' ');
        canvas.drawHLine(0, 1, canvas.getWidth(), ' ');
        sc.draw();
        canvas.drawString(0, 0, line0);
        canvas.drawString(0, 1, line1);
        canvas.copy(sc.canvas, 0, 2, true);

        final String graphicsStr=canvas.toString();
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                activity.graphicsField.setText(graphicsStr);
            }
        });
    }

    void drawPause(AsciiCanvas canvas){
        canvas.drawHLine(0, 1, canvas.getWidth(), ' ');
        canvas.drawString(0, 1, "PAUSED. TAP TO RESTART");
        final String graphicsStr=canvas.toString();
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                activity.graphicsField.setText(graphicsStr);
            }
        });
    }

    static String modeStr(int speed){
        switch (speed){
            case SPEED_SLOW:
                return "s";
            case SPEED_NORMAL:
                return "n";
            case SPEED_FAST:
                return "f";
            case SPEED_EXTREME:
                return "x";
            case SPEED_DYNAMIC:
                return "d";
            case SPEED_RANDOM:
                return "r";
            default:
                return "n";
        }
    }

    //finds modefrom optionPrefs
    int getSpeed(){
        //find mode from optionPrefs
        String speedString=activity.optionPrefs.getString("speed","n");
        switch(speedString.charAt(0)){
            case 's':
                return SPEED_SLOW;
            case 'n':
                return SPEED_NORMAL;
            case 'f':
                return SPEED_FAST;
            case 'x':
                return SPEED_EXTREME;
            case 'd':
                return SPEED_DYNAMIC;
            case 'r':
                return SPEED_RANDOM;
            default:
                return SPEED_NORMAL;
        }
    }

    long getInitialInterval(int speed){
        switch(speed){
            case SPEED_SLOW:return INTERVAL_SLOW;
            case SPEED_NORMAL:return INTERVAL_NORMAL;
            case SPEED_FAST:return INTERVAL_FAST;
            case SPEED_EXTREME:return INTERVAL_EXTREME;
            default:return INTERVAL_NORMAL;
        }
    }

    //updates and retruns the new highscore
    int updateHigh(int score,int highscore,int speed){
        if(score>highscore){
            highscore=score;
            synchronized(activity) {
                activity.highscoreEdit.putInt("high" + modeStr(speed), highscore);
                activity.highscoreEdit.commit();
            }
        }
        return highscore;
    }

    //called by UI thread
    void setDirection(int dir){
        direction=dir;
    }

    //called by UI thread
    synchronized void setPause(boolean p){
        isPaused = p;
        notifyAll();
    }
}
