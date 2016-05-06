package com.brainesgames.snake;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.TypedValue;
import android.view.Display;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.RotateAnimation;
import android.widget.TextView;

import com.brainesgames.ascii.AsciiCanvas;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class MainActivity extends AppCompatActivity {

    String[] messages ={
            "try flipping your device horizontally while playing",
            "Plan in advance, drag your finger without releasing",
            "Hehehe",
            "Welcome lol",
            "＼(*^￢^*)／",
            "ヾ(｀д´*)ﾉ",
            "≧(´▽｀)≦",
            "（ノT＿T)ノ ＾┻━┻",
            "｡：ﾟ(｡ﾉω＼｡)ﾟ･｡",
            "♪(┌・。・)┌",
            "The power within me!",
            "do excuse us...",
            "DID he die of hot dog poisoning?",
            "Greedo shot first.",
            "How the turn tables...",
            "Excitement, HEH!",
            "I would literally die for pop punk.",
            "Tips are displayed on the main menu screen",
            "Microwave offset foot quiver",
            "Adventure, HEH!",
            "Noodles and baguettes go well together.",
            "Because ambient music.",
            "Typically, tips will be shown on the main menu.",
            "Guess and check is the best method",
            "My gosh, my accelerometer's been stolen!",
            "Why are firetrucks red?",
            "How much wood would a woodchuck chuck?",
            "Because the molecules in the fire truck scatter red light more than blue.",
            "Perforated.",
            "Han shot first.",
            "I trust you will do an okay job.",
            "Join the HeckDivers!",
            "I follow the teachings of Marx in my facial hair.",
            "CLICK HERE FOR A FREE TROJAN",
            "What IF you were here?",
            "If this statement is true, the previous statement is false.",
            "That's so contrapuntal.",
            "Hey, Louis.",
            "No True Scotsman would play this game.",
            "There's a squid in my soup!",
            "Vogel the Monkey!"
    };
    String[] colours ={
            "#FFE95C",
            "#00EDED",
            "#FF1919",
            "#D875FF",
            "#00AA00",
            "#FFAB24",
            "#8100EB"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        int index = (int) (messages.length*Math.random());
        int colourIndex = (int) (colours.length*Math.random());
        int rotationValue = (int) (25*Math.random());
        double negativeChance = (10*Math.random());
        if(negativeChance < 5){
            rotationValue = -1*(rotationValue);
        }


        TextView t = (TextView) findViewById(R.id.TextView01);
        t.setText(messages[index]);
        t.setTextColor(Color.parseColor(colours[colourIndex]));
        t.setRotation(rotationValue);

        TextView title = (TextView) findViewById(R.id.title);
        AsciiCanvas ac=AsciiCanvas.load(getResources().openRawResource(R.raw.titleart));

        //set title as big as possible
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int screenWidth = size.x;
        float maxFontSize=(float)screenWidth / ac.getWidth()*1.57f;
        title.setTextSize(TypedValue.COMPLEX_UNIT_PX, maxFontSize);
        //duplicate monospace setting because the xml doesn't work on some devices
        title.setTypeface(Typeface.MONOSPACE);

        if(ac==null){
            title.setText("Snake");
        }
        else{
            title.setText(ac.toString());
        }
    }

/*    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }*/

/*    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }*/
    /** Called when the user clicks the OPTIONS button */
    public void optionsMenu(View v) { startActivity(new Intent(this,HowToPlay.class)); }


    /** Called when the user clicks the START button */
    public void options(View v){
        startActivity(new Intent(this,OptionsActivity.class));
    }
}


