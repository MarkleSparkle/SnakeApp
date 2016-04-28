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
    boolean gameOn;
    boolean gamePaused;
    GameLoop game;
    long lastTap;
    TextView graphicsField;
    float startX, startY;
    SharedPreferences highscorePrefs,optionPrefs;
    SharedPreferences.Editor highscoreEdit,optionEdit;
    int dpi;
    int screenWidth,screenHeight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        highscorePrefs=getApplication().getSharedPreferences("highscores", MODE_PRIVATE);
        highscoreEdit=highscorePrefs.edit();
        optionPrefs=getApplication().getSharedPreferences("options", MODE_PRIVATE);
        optionEdit=optionPrefs.edit();

        startX=startY=0;
        gameOn=false;
        gamePaused=false;
        lastTap=0L;

        graphicsField=(TextView)findViewById(R.id.graphicsField);

        DisplayMetrics metrics = getResources().getDisplayMetrics();
        dpi=metrics.densityDpi;
        Log.d("GameActivity","dpi: "+dpi);

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        screenWidth = size.x;
        screenHeight = size.y;
        Log.d("GameActivity","sw"+screenWidth);
        Log.d("GameActivity", "sh" + screenHeight);

        InputStream is= getResources().openRawResource(R.raw.taptostart);
        AsciiCanvas tapToStart=AsciiCanvas.load(is);
        if(tapToStart==null) {
            graphicsField.setText("Tap to Start");
        }
        else{
            graphicsField.setText(tapToStart.toString());
        }

        graphicsField.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {//listens for the direction of the users swipe in game
                //Log.d("GameActivity","Touch event received");
                //Log.d("GameActivity","action: "+event.getAction());
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        //Log.d("GameActivity","ACTION_DOWN");
                        startX = event.getX();
                        startY = event.getY();
                        break;
                    case MotionEvent.ACTION_UP:

                        //Log.d("GameActivity","ACTION_UP");
                        int direction=0;

                        float endX = event.getX();
                        float endY = event.getY();
                        float dx = endX - startX;
                        float dy = endY - startY;

                        if (dx * dx + dy * dy < (60 * ((float) dpi / 320) * ((float) dpi / 320))) {//if there's a tap withing a 7x7 pixel area (adjusted for dpi), a pause is registered
                            if (!gameOn) {//starts game on a tap
                                startGame();
                            } else if (!gamePaused) {
                                long thisTap = System.currentTimeMillis();
                                //pause if double tap
                                if (thisTap - lastTap < 400) {
                                    gamePaused = true;
                                    game.setPause(true);
                                    //canvas.drawString(0, 1, "PAUSED. TAP TO UNPAUSE");
                                    //graphicsField.setText(canvas.toString());
                                }
                                //save the time of this tap
                                lastTap = thisTap;
                            } else {
                                gamePaused = false;
                                game.setPause(false);
                            }

                        }
                        //Log.d("GameActivity","dx: "+dx);
                        //Log.d("GameActivity","dx: "+dx);
                        else {//otherwise a swipe is registered and continues the game
                            if(gameOn) {
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

                                game.setDirection(direction);
                            }
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

    void startGame(){
        gameOn=true;
        gamePaused=false;
        //create new gameLoop
        game=new GameLoop(this);
        //start GameLoop thread
        new Thread(game).start();
    }

    void gameEnd(){
        gameOn=false;
    }
}
