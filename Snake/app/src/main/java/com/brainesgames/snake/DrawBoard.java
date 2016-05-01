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
    private GameActivity activity;
    private Canvas c;
    private int width,height,cwidth,cheight,acwidth,acheight;
    private Paint p;
    private Rect dest;
    char[] line1,gameoverLine,pauseLine;
    int  line2Type;

    static Bitmap vline,hline,apple;
    static boolean bitmapsInitialized=false;

    DrawBoard(GameActivity act,AsciiCanvas asciiCanvas){
        ac=asciiCanvas;
        acwidth=ac.getWidth();
        acheight=ac.getHeight();
        activity=act;
        line1=new char[28];
        gameoverLine=new char[27];
        pauseLine=new char[22];
        line2Type=NO_LINE2;
        width=0;
        height=0;
        p=new Paint();
        p.setColor(0xff00ff00);
        p.setTextAlign(Paint.Align.LEFT);
        p.setTypeface(Typeface.MONOSPACE);
        //p.setTextScaleX(2);
        dest=new Rect();
        initBitmaps();
        initLines();
    }

    void initLines(){
        line1[0]='S';
        line1[1]='C';
        line1[2]='O';
        line1[3]='R';
        line1[4]='E';
        line1[5]=':';
        line1[6]=' ';
        //used for score
        line1[7]=' ';
        line1[8]=' ';
        line1[9]=' ';
        //seperate score and highscore
        line1[10]=' ';
        line1[11]=' ';
        line1[12]=' ';
        line1[13]='H';
        line1[14]='I';
        line1[15]='G';
        line1[16]='H';
        line1[17]='S';
        line1[18]='C';
        line1[19]='O';
        line1[20]='R';
        line1[21]='E';
        line1[23]=':';
        line1[24]=' ';
        //used for highscore
        line1[25]=' ';
        line1[26]=' ';
        line1[27]=' ';

        gameoverLine[0]='G';
        gameoverLine[1]='A';
        gameoverLine[2]='M';
        gameoverLine[3]='E';
        gameoverLine[4]=' ';
        gameoverLine[5]='O';
        gameoverLine[6]='V';
        gameoverLine[7]='E';
        gameoverLine[8]='R';
        gameoverLine[9]='.';
        gameoverLine[10]=' ';
        gameoverLine[11]='T';
        gameoverLine[12]='A';
        gameoverLine[13]='P';
        gameoverLine[14]=' ';
        gameoverLine[15]='T';
        gameoverLine[16]='O';
        gameoverLine[17]=' ';
        gameoverLine[18]='T';
        gameoverLine[19]='R';
        gameoverLine[20]='Y';
        gameoverLine[21]=' ';
        gameoverLine[22]='A';
        gameoverLine[23]='G';
        gameoverLine[24]='A';
        gameoverLine[25]='I';
        gameoverLine[26]='N';

        pauseLine[0]='P';
        pauseLine[1]='A';
        pauseLine[2]='U';
        pauseLine[3]='S';
        pauseLine[4]='E';
        pauseLine[5]='D';
        pauseLine[6]='.';
        pauseLine[7]=' ';
        pauseLine[8]='T';
        pauseLine[9]='A';
        pauseLine[10]='P';
        pauseLine[11]=' ';
        pauseLine[12]='T';
        pauseLine[13]='O';
        pauseLine[14]=' ';
        pauseLine[15]='R';
        pauseLine[16]='E';
        pauseLine[17]='S';
        pauseLine[18]='T';
        pauseLine[19]='A';
        pauseLine[20]='R';
        pauseLine[21]='T';
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
        c = activity.surfaceHolder.lockCanvas();
        if(c!=null) {
            width = c.getWidth();
            height = c.getHeight();
            cwidth = width / ac.getWidth();
            cheight = height / (ac.getHeight()+2);
            p.setTextSize(cheight);
            drawBoard();
            activity.surfaceHolder.unlockCanvasAndPost(c);
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

        for(int x=0;x<acwidth;x++){
            for(int y=0;y<acheight;y++){
                switch(ac.get(x,y)){
                    case '|':
                        dest.set(x * cwidth, (y+2) * cheight, (x+1) * cwidth, (y+3) * cheight);
                        c.drawBitmap(vline,null,dest,null);
                        break;
                    case '_':
                        dest.set(x * cwidth, (y+2) * cheight, (x+1) * cwidth, (y+3) * cheight);
                        c.drawBitmap(hline,null,dest,null);
                        break;
                    case '\u03b4':
                        dest.set(x * cwidth, (y+2) * cheight, (x+1) * cwidth, (y+3) * cheight);
                        c.drawBitmap(apple,null,dest,null);
                        break;
                }
            }
        }
    }

}
