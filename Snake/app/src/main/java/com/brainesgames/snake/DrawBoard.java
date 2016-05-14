package com.brainesgames.snake;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.util.Log;
import android.view.SurfaceHolder;

import com.brainesgames.ascii.AsciiCanvas;

/**
 * Created by Orson Baines on 2016-04-29.
 */
public class DrawBoard{
    final static int NO_LINE2=0;
    final static int PAUSE_LINE2=1;
    final static int OVER_LINE2=2;
    final static int READY_LINE2=3;
    private AsciiCanvas ac;
    private SurfaceHolder sh;
    private Canvas c;
    private int width,height;
    private float cheight;
    private Paint psc,ptext;
    private char[] line1;
    private String gameoverLine,pauseLine,readyLine;
    private int  line2Type;
    
    private int colour;

    DrawBoard(SurfaceHolder surfaceHolder,AsciiCanvas asciiCanvas,int colour){
        ac=asciiCanvas;
        sh=surfaceHolder;
        line1=new char[28];
        gameoverLine=" GAME OVER. TAP TO TRY AGAIN";
        pauseLine=" PAUSED. SWIPE TO START";
        readyLine=" SWIPE TO START";
        line2Type=NO_LINE2;
        width=0;
        height=0;
        
        this.colour=colour;
        
        psc=new Paint();
        psc.setColor(colour);
        psc.setTextAlign(Paint.Align.LEFT);
        psc.setTypeface(Typeface.MONOSPACE);
        psc.setTextScaleX(1.66f);

        ptext=new Paint();
        ptext.setColor(colour);
        ptext.setTextAlign(Paint.Align.LEFT);
        ptext.setTypeface(Typeface.MONOSPACE);
        
        initLines();
    }

    void initLines(){
        line1[0]=' ';
        line1[1]='S';
        line1[2]='C';
        line1[3]='O';
        line1[4]='R';
        line1[5]='E';
        line1[6]=':';
        line1[7]=' ';
        //used for score
        line1[8]=' ';
        line1[9]=' ';
        line1[10]=' ';
        //seperate score and highscore
        line1[11]=' ';
        line1[12]=' ';
        line1[13]=' ';
        line1[14]='H';
        line1[15]='I';
        line1[16]='G';
        line1[17]='H';
        line1[18]='S';
        line1[19]='C';
        line1[20]='O';
        line1[21]='R';
        line1[22]='E';
        line1[23]=':';
        line1[24]=' ';
        //used for highscore
        line1[25]=' ';
        line1[26]=' ';
        line1[27]=' ';

    }

    void setScoreText(int score,int highscore){
        //set score text
        if(score<10){
            line1[8]=(char)('0'+score);
            line1[9]=' ';
            line1[10]=' ';
        }
        else if(score<100){
            line1[8]=(char)('0'+score/10);
            line1[9]=(char)('0'+score%10);
            line1[10]=' ';
        }
        else{
            line1[8]=(char)('0'+score/100);
            line1[9]=(char)('0'+score%100/10);
            line1[10]=(char)('0'+score%10);
        }
        //set highscore text
        if(highscore<10){
            line1[25]=(char)('0'+highscore);
            line1[26]=' ';
            line1[27]=' ';
        }
        else if(highscore<100){
            line1[25]=(char)('0'+highscore/10);
            line1[26]=(char)('0'+highscore%10);
            line1[27]=' ';
        }
        else{
            line1[25]=(char)('0'+highscore/100);
            line1[26]=(char)('0'+highscore%100/10);
            line1[27]=(char)('0'+highscore%10);
        }

    }

    void setLine2(int line2){
        switch (line2){
            case PAUSE_LINE2:
                line2Type=PAUSE_LINE2;
                break;
            case OVER_LINE2:
                line2Type=OVER_LINE2;
                break;
            case READY_LINE2:
                line2Type=READY_LINE2;
                break;
            default:
                line2Type=NO_LINE2;
        }

    }

    void draw(){
        c = sh.lockCanvas();
        if(c!=null) {
            width = c.getWidth();
            height = c.getHeight();
            float sizemaxx=width / ac.getWidth();
            float sizemaxy=height / (ac.getHeight()+4);
            //Log.d("GameLoop","sizemaxx: "+sizemaxx);
            //Log.d("GameLoop","sizemaxy: "+sizemaxy);
            float sizemax=Math.min(sizemaxx,sizemaxy);
            cheight = sizemax;
            psc.setTextSize(sizemax);
            ptext.setTextSize(sizemax*2);
            drawBoard();
            sh.unlockCanvasAndPost(c);
        }
        else{
            Log.w("DrawBoard","Unable to draw on gameSV");
        }
    }

    void drawBoard(){
        c.drawARGB(255, 0, 0, 0);
        c.drawText(line1,0,line1.length,0,cheight*2,ptext);
        switch (line2Type){
            case PAUSE_LINE2:
                c.drawText(pauseLine,0,cheight*4,ptext);
                break;
            case OVER_LINE2:
                c.drawText(gameoverLine,0,cheight*4,ptext);
                break;
            case READY_LINE2:
                c.drawText(readyLine,0,cheight*4,ptext);
                break;
        }

        ac.draw(c,cheight*5,0,cheight,psc);
    }

}
