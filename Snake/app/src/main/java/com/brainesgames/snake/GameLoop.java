package com.brainesgames.snake;

import android.app.Activity;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.util.Log;
import android.util.TypedValue;

import com.brainesgames.ascii.AsciiCanvas;

import java.io.InputStream;
import java.util.ArrayList;

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

    static final int GAME_READY=0;
    static final int GAME_ON=1;
    static final int GAME_PAUSED=2;
    static final int GAME_OVER=3;

    final GameActivity activity;
    volatile int gameState;
    private boolean running;
    volatile int direction;

    private SnakeBoard board;
    private SnakeCanvas sc;
    private DrawBoard drawBoard;
    private long interval;
    private int highscore,score,speed;
    private MediaPlayer mediaPlayer;

    SharedPreferences savePrefs;

    SharedPreferences.Editor saveEdit;

    GameLoop(GameActivity activity){
        this.activity=activity;
        setState(GAME_READY);
        direction=3;
        savePrefs=activity.getApplication().getSharedPreferences("save", Activity.MODE_PRIVATE);
        saveEdit=savePrefs.edit();
    }

    @Override
    public void run() {
        Log.d("GameLoop","Beginning Game Loop");

        int musicStart=0;
        if(savePrefs.getBoolean("gameSaved", false)){
            Log.d("Game Loop","Loading Saved Game");
            board=loadSaved();
            if(board==null){
                Log.e("GameLoop","Error loading saved game. Creating new Game");
                board = new SnakeBoard();
                speed = getSpeed();
                interval=getInitialInterval();
            }
            else{
                Log.d("Game Loop","Loading Successful");
                speed=savePrefs.getInt("speed",SPEED_NORMAL);
                interval=savePrefs.getLong("interval",INTERVAL_NORMAL);
            }

            direction=savePrefs.getInt("direction",3);
            musicStart=savePrefs.getInt("musicStart",0);
        }
        else {
            board = new SnakeBoard();
            speed = getSpeed();
            interval=getInitialInterval();
        }
        //reset gameSaved to false
        saveEdit.putBoolean("gameSaved", false);
        saveEdit.commit();

        sc = new SnakeCanvas(board);
        highscore = activity.highscorePrefs.getInt("high" + modeStr(speed), 1);
        drawBoard = new DrawBoard(activity.surfaceHolder, sc.canvas);
        mediaPlayer = null;

        boolean soundEnabled;
        synchronized (activity){
            soundEnabled=activity.optionPrefs.getBoolean("soundEnabled",true);
        }
        if(soundEnabled)mediaPlayer=MediaPlayer.create(activity,R.raw.snaketheme);
        if(mediaPlayer!=null){
            mediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
                public boolean onError(MediaPlayer mp, int what, int extra) {
                    mediaPlayer.release();
                    mediaPlayer = null;
                    return true;
                }
            });
            mediaPlayer.setLooping(true);
        }

        running=true;
        boolean first=true;
        while(running) {
            score=1;
            if(!first){
                board.newBoard();
                interval=getInitialInterval();
                musicStart=0;
            }
            else{
                first=false;
            }
            drawBoard.setLine2(DrawBoard.NO_LINE2);
            drawBoard.setScoreText(score,highscore);
            drawGame();
            try {
                synchronized (this){
                    wait(1000);
                }
            } catch (InterruptedException e) {
                Log.d("GameLoop","Thread interrupted: terminating");
                running=false;
            }

            if(mediaPlayer!=null){
                mediaPlayer.seekTo(musicStart);
                mediaPlayer.start();
            }

            if(gameState!=GAME_ON)setState(GAME_ON);


            while (gameState==GAME_ON && running) {
                int scoreBefore = score;
                board.setDirection(direction);
                board.move();
                score = board.snake.size();
                if (score > highscore) highscore = score;
                drawBoard.setScoreText(score, highscore);
                if (board.done) {
                    drawBoard.setLine2(DrawBoard.OVER_LINE2);
                    drawGame();
                    setState(GAME_OVER);
                    pauseLoop();
                } else {
                    drawGame();

                    if (score > scoreBefore) {
                        if (speed == SPEED_RANDOM) {
                            //set and apply new interval
                            interval = SnakeBoard.r.nextInt(125) + 50;
                        } else if (speed == SPEED_DYNAMIC) {
                            interval=INTERVAL_NORMAL-2*score;
                            if(interval<30)interval=30;
                        }
                    }
                }

                try {
                    Thread.sleep(interval);
                } catch (InterruptedException e) {
                    Log.d("GameLoop", "Thread interrupted: terminating");
                    running=false;
                }

                if(gameState==GAME_PAUSED) {
                    drawPause();
                    pauseLoop();
                    if (mediaPlayer != null) mediaPlayer.start();
                    drawBoard.setLine2(DrawBoard.NO_LINE2);
                }
            }
        }

        if(gameState ==GAME_ON || gameState==GAME_PAUSED)saveGame();
        updateHigh();
        if(mediaPlayer!=null){
            mediaPlayer.stop();
            mediaPlayer.release();
        }
        Log.d("GameLoop", "Ending Game Loop");
    }

    void drawGame(){
        if(drawBoard!=null){
            sc.draw();
            drawBoard.draw();
        }
    }

    void drawPause(){
        drawBoard.setLine2(DrawBoard.PAUSE_LINE2);
        drawBoard.draw();
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

    static String stateString(int state){
        switch (state){
            case GAME_ON:
                return "GAME_ON";
            case GAME_PAUSED:
                return "GAME_PAUSED";
            case GAME_OVER:
                return "GAME_OVER";
            case GAME_READY:
                return "GAME_READY";
            default:
                return "INVALID_STATE";
        }
    }

    //finds modefrom optionPrefs
    int getSpeed(){
        //find mode from optionPrefs
        String speedString;
        synchronized (activity) {
            speedString= activity.optionPrefs.getString("speed", "n");
        }
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

    long getInitialInterval(){
        switch(speed){
            case SPEED_SLOW:return INTERVAL_SLOW;
            case SPEED_NORMAL:return INTERVAL_NORMAL;
            case SPEED_FAST:return INTERVAL_FAST;
            case SPEED_EXTREME:return INTERVAL_EXTREME;
            default:return INTERVAL_NORMAL;
        }
    }

    //updates and returns the new highscore
    void updateHigh(){
        synchronized(activity) {
            activity.highscoreEdit.putInt("high" + modeStr(speed), highscore);
            activity.highscoreEdit.commit();
        }
    }
    //set the gameState for both this and activity
    //called by either thread
    synchronized void setState(int state){
        Log.d("GameLoop","Setting State to: "+stateString(state));
        gameState=state;
        activity.gameState=state;
        if(state==GAME_OVER) updateHigh();
        if(state==GAME_ON || state==GAME_READY)notifyAll();
    }
    //called by this thread when state becomes either GAME_PAUSED or GAME_OVER
    synchronized void pauseLoop(){
        if (mediaPlayer != null) mediaPlayer.pause();
        try {
            wait();
        } catch (InterruptedException e) {
            Log.d("GameLoop", "Thread interrupted: terminating");
            running=false;
        }
    }

    //called by UI thread
    void setDirection(int dir){
        direction=dir;
    }

    @Override
    public void finalize(){
        if(mediaPlayer!=null)mediaPlayer.release();
        try {
            super.finalize();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }

    void saveGame(){
        synchronized (activity){
            Log.d("GameLoop", "starting save");
            if(mediaPlayer!=null)saveEdit.putInt("musicStart",mediaPlayer.getCurrentPosition());
            saveEdit.putBoolean("gameSaved", true);
            saveEdit.putInt("speed",speed);
            saveEdit.putInt("direction",direction);
            saveEdit.putLong("interval",interval);
            saveEdit.putInt("foodx", board.food.getX());
            saveEdit.putInt("foody", board.food.getY());
            saveEdit.putInt("snakesize", board.snake.size());
            for(int i=0;i<board.snake.size();i++){
                saveEdit.putInt("snakex"+i, board.snake.get(i).getX());
                saveEdit.putInt("snakey"+i, board.snake.get(i).getY());
            }
            saveEdit.commit();
            //Log.d("GameLoop",savePrefs.getAll().toString());
            Log.d("GameLoop", "ending save");
        }
    }

    SnakeBoard loadSaved(){
        SnakeBoard b=new SnakeBoard();
        b.food=new OrderedPair(savePrefs.getInt("foodx",0),savePrefs.getInt("foody",0));
        b.snake=new ArrayList<>();
        for(int i=0;i<savePrefs.getInt("snakesize",1);i++){
            OrderedPair op=new OrderedPair(savePrefs.getInt("snakex"+i,0),savePrefs.getInt("snakey"+i,0));
            if(!b.inSnake(op))b.snake.add(op);
            else return null;
        }
        //Log.d("GameLoop",b.snake.toString());
        return b;
    }
}
