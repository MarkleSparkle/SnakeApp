package com.brainesgames.snake;

import android.content.SharedPreferences;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Toast;

import static com.brainesgames.snake.GameLoop.GAME_ON;
import static com.brainesgames.snake.GameLoop.GAME_READY;
import static com.brainesgames.snake.GameLoop.GAME_OVER;
import static com.brainesgames.snake.GameLoop.GAME_PAUSED;

public class GameActivity extends AppCompatActivity {
    int gameState;
    GameLoop game;
    Thread gameThread;
    long lastTap;
    float startX, startY, tapr2;

    float dpi;
    int screenWidth,screenHeight;

    SurfaceHolder surfaceHolder;
    //boolean surfaceAvailable,surfaceChanged;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        startX=startY=0;
        lastTap=0L;

        SurfaceView gameSV=(SurfaceView)findViewById(R.id.gameSV);
        surfaceHolder=gameSV.getHolder();
        surfaceHolder.addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                if(game!=null)game.drawGame();
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
                Log.d("GameActivity","sw: "+width);
                Log.d("GameActivity","sh: "+height);
            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {

            }
        });

        DisplayMetrics metrics = getResources().getDisplayMetrics();
        dpi=(metrics.xdpi+metrics.ydpi)/2;
        tapr2= 1000 * dpi / 320 * dpi / 320;
        Log.d("GameActivity","dpi: "+metrics.densityDpi);
        Log.d("GameActivity","xdpi: "+metrics.xdpi);
        Log.d("GameActivity","ydpi: "+metrics.ydpi);
        gameSV.setOnTouchListener(new View.OnTouchListener() {

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
                        int direction;

                        float endX = event.getX();
                        float endY = event.getY();
                        float dx = endX - startX;
                        float dy = endY - startY;
                        float r2=dx * dx + dy * dy;

                        //Log.d("GameActivity","r^2: "+r2);
                        //Log.d("GameActivity","tapr^2: "+tapr2);

                        if (r2 < tapr2) {//if there's a tap withing a 7x7 pixel area (adjusted for dpi), a pause is registered
                            //Log.d("GameActivity","TAP");
                            if (gameState == GAME_OVER) {//starts game on a tap
                                game.setState(GAME_READY);
                            } else if (gameState == GAME_ON) {
                                long thisTap = System.currentTimeMillis();
                                //Log.d("GameActivity","tapgap: "+(thisTap-lastTap));
                                //pause if double tap
                                if (thisTap - lastTap < 500) {
                                    game.setState(GAME_PAUSED);
                                }
                                //save the time of this tap
                                lastTap = thisTap;
                            } else {
                                game.setState(GAME_ON);
                            }

                        }
                        else {//otherwise a swipe is registered and continues the game
                            //Log.d("GameActivity","SWIPE");
                            if (gameState == GAME_ON || gameState == GAME_READY) {
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
                                if (gameState == GAME_READY) game.setState(GAME_ON);
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

    @Override
    protected void onPause(){

        super.onPause();
        if(gameThread!=null){
            gameThread.interrupt();
        }
    }

    @Override
    protected void onResume(){
        super.onResume();
        //start the game loop
        startGameLoop();
    }

    void startGameLoop(){
        //create new gameLoop
        game=new GameLoop(this);
        //start GameLoop thread
        gameThread=new Thread(game,"GameThread");
        gameThread.start();
    }
}
