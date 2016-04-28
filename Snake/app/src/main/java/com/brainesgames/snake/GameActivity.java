package com.brainesgames.snake;

import android.content.SharedPreferences;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;
import com.brainesgames.ascii.AsciiCanvas;
import java.io.InputStream;
import java.util.Timer;
import java.util.TimerTask;

public class GameActivity extends AppCompatActivity {
    static final long INTERVAL_SLOW=150;
    static final long INTERVAL_NORMAL=125;
    static final long INTERVAL_FAST=100;
    static final long INTERVAL_EXTREME=75;

    static final int MODE_SLOW=0;
    static final int MODE_NORMAL=1;
    static final int MODE_FAST=2;
    static final int MODE_EXTREME=3;
    static final int MODE_DYNAMIC=4;
    static final int MODE_RANDOM=5;

    SnakeBoard board;
    SnakeCanvas sc;
    AsciiCanvas canvas;
    int score,highscore;
    int gameMode;
    long interval;
    int direction;
    boolean gameOn;
    boolean gamePaused;
    long lastTap;
    Timer timer;
    TextView graphicsField;
    float startX, startY;
    SharedPreferences highscorePrefs,optionPrefs;
    SharedPreferences.Editor highscoreEdit,optionEdit;
    int dpi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        highscorePrefs=getApplication().getSharedPreferences("highscores", MODE_PRIVATE);
        highscoreEdit=highscorePrefs.edit();
        optionPrefs=getApplication().getSharedPreferences("options", MODE_PRIVATE);
        optionEdit=optionPrefs.edit();

        initMode();

        board=new SnakeBoard();
        sc=new SnakeCanvas(board);
        canvas=new AsciiCanvas(sc.canvas.getWidth(),sc.canvas.getHeight()+2);
        score=1;
        highscore=highscorePrefs.getInt("high"+modeStr(),1);
        direction=0;
        startX=startY=0;
        gameOn=false;
        gamePaused=false;
        lastTap=0L;

        InputStream is= getResources().openRawResource(R.raw.taptostart);
        AsciiCanvas tapToStart=AsciiCanvas.load(is);
        sc.draw();
        canvas.drawHLine(0, 0, canvas.getWidth(), ' ');
        canvas.drawString(0, 0, "HIGHSCORE: " + highscore);
        if(tapToStart==null) {
            canvas.drawString(canvas.getWidth() / 2 - 6, canvas.getHeight() / 2, "TAP TO START");
        }
        else{
            canvas.copy(tapToStart,(canvas.getWidth()-tapToStart.getWidth())/2,(canvas.getHeight()-tapToStart.getHeight())/2,true);
        }

        graphicsField=(TextView)findViewById(R.id.graphicsField);
        graphicsField.setText(canvas.toString());

        DisplayMetrics metrics = getResources().getDisplayMetrics();
        dpi=metrics.densityDpi;
        Log.d("Game activity","dpi: "+dpi);

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int screenWidth = size.x;
        int screenHeight = size.y;
        Log.d("Game activity","sw"+screenWidth);
        Log.d("Game activity","sh"+screenHeight);

        //*****may need to change because height of a character is larger than width
        float limitSize=Math.min((float)screenWidth / canvas.getWidth(), (float)screenHeight/canvas.getHeight()*0.49f);

        Log.d("GameActivity","lswid: "+(float)screenWidth / canvas.getWidth());
        Log.d("GameActivity","lshei: "+(float)screenHeight/canvas.getHeight()*0.49f);

        Log.d("GameActivity", "ls" + limitSize);
        graphicsField.setTextSize(TypedValue.COMPLEX_UNIT_PX, limitSize * 1.67f);

        graphicsField.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {//listens for the direction of the users swipe in game
                //Log.d("GameActivity","Touch event received");
                //Log.d("GameActivity","action: "+event.getAction());
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        //Log.d("GameActivity","ACTION_DOWN");
                        startX=event.getX();
                        startY=event.getY();
                        break;
                    case MotionEvent.ACTION_UP:

                        //Log.d("GameActivity","ACTION_UP");
                        int directionBefore=direction;

                        float endX=event.getX();
                        float endY=event.getY();
                        float dx=endX-startX;
                        float dy=endY-startY;

                        if(dx*dx+dy*dy<(60*((float)dpi/320)*((float)dpi/320))){//if there's a tap withing a 7x7 pixel area (adjusted for dpi), a pause is registered
                            if (!gameOn) {//starts game on a tap
                                initMode();
                                startGame();
                            }
                            else if(!gamePaused){
                                long thisTap=System.currentTimeMillis();
                                //pause if double tap
                                if(thisTap-lastTap<400) {
                                    endTimer();
                                    gamePaused = true;
                                    canvas.drawString(0, 1, "PAUSED. TAP TO UNPAUSE");
                                    graphicsField.setText(canvas.toString());
                                }
                                //save the time of this tap
                                lastTap=thisTap;
                            }
                            else{
                                gamePaused = false;
                                newTimer();
                            }

                        }
                        //Log.d("GameActivity","dx: "+dx);
                        //Log.d("GameActivity","dx: "+dx);
                        else {//otherwise a swipe is registered and continues the game
                            if (Math.abs(dy) > Math.abs(dx)) {
                                if (dy >= 0) {
                                    direction = 2;
                                } else {
                                    direction = 3;
                                }
                            } else {
                                if (dx >= 0) {
                                    direction = 0;
                                } else {
                                    direction = 1;
                                }
                            }

                            if (!board.verify(direction)) direction = directionBefore;
                        }
                        break;
                    case MotionEvent.ACTION_MOVE:
                        //Log.d("GameActivity","ACTION_MOVE");
                        break;
                }
                return true;
            }
        });

    }

    void startGame(){//called after START button is pressed on menu
        //create new game
        newGame();
        newTimer();
    }

    void nextLoop(){
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    public void run() {
                        if (gameOn) {
                            int sizeBefore = board.snake.size();
                            board.move(direction);
                            setScore(board.snake.size());
                            if (board.done) {
                                gameOn = false;
                                canvas.drawHLine(0, 0, canvas.getWidth(), ' ');
                                canvas.drawHLine(0, 1, canvas.getWidth(), ' ');
                                canvas.drawString(0, 0, "GAME OVER.  TAP TO TRY AGAIN");
                                canvas.drawString(0, 1, "FINAL SCORE: " + score + "   HIGHSCORE: " + highscore);
                                endTimer();

                            } else {
                                sc.draw();
                                canvas.drawHLine(0, 0, canvas.getWidth(), ' ');
                                canvas.drawHLine(0, 1, canvas.getWidth(), ' ');
                                canvas.drawString(0, 0, "SCORE: " + score + "   HIGHSCORE: " + highscore);
                                canvas.copy(sc.canvas, 0, 2, true);


                                //snake has just eaten an apple
                                if (board.snake.size() > sizeBefore) {
                                    if (gameMode == MODE_RANDOM) {
                                        //set and apply new interval
                                        interval = SnakeBoard.r.nextInt(125) + 50;
                                    } else if (gameMode == MODE_DYNAMIC) {
                                        if (board.snake.size() % 5 == 0 && interval > 30) {
                                            interval -= 10;
                                        }
                                    }
                                }

                                nextLoop();
                            }
                            graphicsField.setText(canvas.toString());
                        }
                    }
                });
            }

        }, interval);
    }

    void newTimer(){
        //new timer
        timer =new Timer();
        //start the loop
        nextLoop();
    }

    //end the timer at end of game
    void endTimer(){
        timer.cancel();
        timer.purge();
    }

    //create new board
    void newGame(){
        gameOn = true;
        board.newBoard();
        sc.draw();
        canvas.drawHLine(0, 0, canvas.getWidth(), ' ');
        canvas.drawHLine(0, 1, canvas.getWidth(), ' ');
        canvas.drawString(0, 0, "SCORE: " + score + "   HIGHSCORE: " + highscore);
        canvas.copy(sc.canvas, 0, 2, true);
        graphicsField.setText(canvas.toString());
    }

    void setScore(int s){
        score=s;
        if(score>highscore){
            highscore=score;
            highscoreEdit.putInt("high"+modeStr(),highscore);
            highscoreEdit.commit();
        }
    }

    static String modeStr(int mode){
        switch (mode){
            case MODE_SLOW:
                return "s";
            case MODE_NORMAL:
                return "n";
            case MODE_FAST:
                return "f";
            case MODE_EXTREME:
                return "x";
            case MODE_DYNAMIC:
                return "d";
            case MODE_RANDOM:
                return "r";
            default:
                return "n";
        }
    }

    String modeStr(){
        return modeStr(gameMode);
    }

    //initializes mode and interval from optionPrefs
    void initMode(){
        //find mode from optionPrefs
        String speedString=optionPrefs.getString("speed","n");
        switch(speedString.charAt(0)){
            case 's':
                gameMode=MODE_SLOW;
                interval=INTERVAL_SLOW;
                break;
            case 'n':
                gameMode=MODE_NORMAL;
                interval=INTERVAL_NORMAL;
                break;
            case 'f':
                gameMode=MODE_FAST;
                interval=INTERVAL_FAST;
                break;
            case 'x':
                gameMode=MODE_EXTREME;
                interval=INTERVAL_EXTREME;
                break;
            case 'd':
                gameMode=MODE_DYNAMIC;
                interval=INTERVAL_NORMAL;
                break;
            case 'r':
                gameMode=MODE_RANDOM;
                interval=INTERVAL_NORMAL;
                break;
            default:
                gameMode=MODE_NORMAL;
                interval=INTERVAL_NORMAL;
        }
    }
}
