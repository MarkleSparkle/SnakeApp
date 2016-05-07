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
    private AsciiCanvas ac;
    private SurfaceHolder sh;
    private Canvas c;
    private int width,height;
    private float cheight;
    private Paint p;
    char[] line1,gameoverLine,pauseLine;
    int  line2Type;

    static Bitmap vline,hline,apple;
    static boolean bitmapsInitialized=false;

    DrawBoard(SurfaceHolder surfaceHolder,AsciiCanvas asciiCanvas){
        ac=asciiCanvas;
        sh=surfaceHolder;
        line1=new char[28];
        gameoverLine=new char[28];
        pauseLine=new char[24];
        line2Type=NO_LINE2;
        width=0;
        height=0;
        p=new Paint();
        p.setColor(0xff00ff00);
        p.setTextAlign(Paint.Align.LEFT);
        p.setTypeface(Typeface.MONOSPACE);
        p.setTextScaleX(1.7f);
        initBitmaps();
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

        gameoverLine[0]=' ';
        gameoverLine[1]='G';
        gameoverLine[2]='A';
        gameoverLine[3]='M';
        gameoverLine[4]='E';
        gameoverLine[5]=' ';
        gameoverLine[6]='O';
        gameoverLine[7]='V';
        gameoverLine[8]='E';
        gameoverLine[9]='R';
        gameoverLine[10]='.';
        gameoverLine[11]=' ';
        gameoverLine[12]='T';
        gameoverLine[13]='A';
        gameoverLine[14]='P';
        gameoverLine[15]=' ';
        gameoverLine[16]='T';
        gameoverLine[17]='O';
        gameoverLine[18]=' ';
        gameoverLine[19]='T';
        gameoverLine[20]='R';
        gameoverLine[21]='Y';
        gameoverLine[22]=' ';
        gameoverLine[23]='A';
        gameoverLine[24]='G';
        gameoverLine[25]='A';
        gameoverLine[26]='I';
        gameoverLine[27]='N';

        pauseLine[0]=' ';
        pauseLine[1]='P';
        pauseLine[2]='A';
        pauseLine[3]='U';
        pauseLine[4]='S';
        pauseLine[5]='E';
        pauseLine[6]='D';
        pauseLine[7]='.';
        pauseLine[8]=' ';
        pauseLine[9]='T';
        pauseLine[10]='A';
        pauseLine[11]='P';
        pauseLine[12]=' ';
        pauseLine[13]='T';
        pauseLine[14]='O';
        pauseLine[15]=' ';
        pauseLine[16]='R';
        pauseLine[17]='E';
        pauseLine[18]='S';
        pauseLine[19]='T';
        pauseLine[20]='A';
        pauseLine[22]='R';
        pauseLine[23]='T';
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
        if(line2==PAUSE_LINE2)line2Type=PAUSE_LINE2;
        else if(line2==OVER_LINE2)line2Type=OVER_LINE2;
        else line2Type=NO_LINE2;
    }

    private static void initBitmaps(){
        if(!bitmapsInitialized) {
            vline = Bitmap.createBitmap(16, 16, Bitmap.Config.ARGB_4444);
            hline = Bitmap.createBitmap(16, 16, Bitmap.Config.ARGB_4444);
            apple = Bitmap.createBitmap(16, 16, Bitmap.Config.ARGB_4444);
            Canvas vlinec = new Canvas(vline);
            Canvas hlinec = new Canvas(hline);
            Canvas applec = new Canvas(apple);
            Paint textPaint = new Paint();
            textPaint.setColor(0xff00ff00);
            textPaint.setTextSize(16);
            textPaint.setTextScaleX(2);
            textPaint.setTextAlign(Paint.Align.LEFT);
            vlinec.drawText("|", 0, 16, textPaint);
            hlinec.drawText("_", -2, 15, textPaint);
            applec.drawText("δ", 0, 16, textPaint);
            bitmapsInitialized=true;
        }
    }

    void draw(){
        c = sh.lockCanvas();
        if(c!=null) {
            width = c.getWidth();
            height = c.getHeight();
            float sizemaxx=width / ac.getWidth() * 0.97f;
            float sizemaxy=height / (ac.getHeight()+2) * 0.97f;
            Log.d("GameLoop","sizemaxx: "+sizemaxx);
            Log.d("GameLoop","sizemaxy: "+sizemaxy);
            float sizemax=Math.min(sizemaxx,sizemaxy);
            cheight = sizemax;
            p.setTextSize(sizemax);
            drawBoard();
            sh.unlockCanvasAndPost(c);
        }
        else{
            Log.w("DrawBoard","Unable to draw on gameSV");
        }
    }

    void drawBoard(){
        c.drawARGB(255, 0, 0, 0);
        c.drawText(line1,0,line1.length,0,cheight,p);
        switch (line2Type){
            case PAUSE_LINE2:
                c.drawText(pauseLine,0,pauseLine.length,0,cheight*2,p);
                break;
            case OVER_LINE2:
                c.drawText(gameoverLine,0,gameoverLine.length,0,cheight*2,p);
                break;
        }

        ac.draw(c,cheight*3,0,cheight,p);
    }

}
