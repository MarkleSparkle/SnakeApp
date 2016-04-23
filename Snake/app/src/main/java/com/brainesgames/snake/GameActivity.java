package com.brainesgames.snake;

import android.graphics.Point;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;
import android.util.Log;

import com.brainesgames.ascii.AsciiCanvas;

import java.util.Timer;
import java.util.TimerTask;

public class GameActivity extends AppCompatActivity {
    SnakeBoard board;
    SnakeCanvas sc;
    AsciiCanvas canvas;
    int score,highscore;
    int direction;
    boolean gameOn;
    Timer timer;
    TimerTask gameLoop;
    TextView graphicsField;
    float startX, startY;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        board=new SnakeBoard();
        sc=new SnakeCanvas(board);
        canvas=new AsciiCanvas(sc.canvas.getWidth(),sc.canvas.getHeight()+2);
        score=1;
        highscore=1;
        direction=0;
        gameOn=false;

        sc.draw();
        canvas.drawHLine(0, 0, canvas.getWidth(), ' ');
        canvas.copy(sc.canvas, 0, 2, true);
        canvas.drawString(0, 0, "SCORE: " + score + "   HIGHSCORE: " + highscore);
        canvas.drawString(0, 1, "TAP TO START");

        graphicsField=(TextView)findViewById(R.id.graphicsField);
        graphicsField.setText(canvas.toString());

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int screenWidth = size.x;
        int screenHeight = size.y;
        Log.d("Game activity","sw"+screenWidth);
        Log.d("Game activity","sh"+screenHeight);

        //*****may need to change because height of a character is larger than width
        int limitSize=Math.min(screenWidth / canvas.getWidth(), screenHeight/canvas.getHeight());
        Log.d("Game activity", "ls" + limitSize);
        graphicsField.setTextSize(TypedValue.COMPLEX_UNIT_PX, limitSize * 1.5f);

        graphicsField.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                //Log.d("GameActivity","Touch event received");
                //Log.d("GameActivity","action: "+event.getAction());
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        //Log.d("GameActivity","ACTION_DOWN");
                        if (!gameOn) {
                            startGame();
                        }
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

                        //Log.d("GameActivity","dx: "+dx);
                        //Log.d("GameActivity","dx: "+dx);

                        if(Math.abs(dy)>Math.abs(dx)){
                            if(dy>=0){
                                direction=2;
                            }
                            else{
                                direction=3;
                            }
                        }
                        else{
                            if(dx>=0){
                                direction=0;
                            }
                            else{
                                direction =1;
                            }
                        }

                        if(!board.verify(direction))direction=directionBefore;
                        break;
                    case MotionEvent.ACTION_MOVE:
                        //Log.d("GameActivity","ACTION_MOVE");
                        break;
                }
                return true;
            }
        });

    }

    void startGame(){
        //create new game
        newGame();
        timer =new Timer();
        //create new timer
        gameLoop=new TimerTask(){
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    public void run() {
                        if (gameOn){
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
                                canvas.drawString(0, 0, "SCORE: " + score + "   HIGHSCORE: " + highscore);
                                canvas.copy(sc.canvas, 0, 2, true);
                            }
                            graphicsField.setText(canvas.toString());
                        }
                    }
                });
            }

        };
        //start the timer
        timer.scheduleAtFixedRate(gameLoop,125,125);
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
        if(score>highscore)highscore=score;
    }
}
