package com.brainesgames.snake;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

public class OptionsActivity extends AppCompatActivity {
    SharedPreferences optionPrefs,save;
    SharedPreferences.Editor optionEdit;
    RadioGroup speedGroup, colourGroup;
    CheckBox soundEnabled;
    EditText nameText;
    RadioButton brightButton,uglyButton,chillButton,sunriseButton,albinoButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_options);

        optionPrefs=getApplication().getSharedPreferences("options", MODE_PRIVATE);
        optionEdit=optionPrefs.edit();

        speedGroup= (RadioGroup)findViewById(R.id.speedGroup);

        //find previous set speed (normal default)
        String speedString=optionPrefs.getString("speed","n");
        int speedId;
        switch(speedString.charAt(0)){
            case 's':
                speedId=R.id.slowButton;
                break;
            case 'n':
                speedId=R.id.normalButton;
                break;
            case 'f':
                speedId=R.id.fastButton;
                break;
            case 'x':
                speedId=R.id.extremeButton;
                break;
            case 'd':
                speedId=R.id.dynamicButton;
                break;
            case 'r':
                speedId=R.id.randomButton;
                break;
            default:
                speedId=R.id.normalButton;
        }

        //check box of previous set speed
        speedGroup.check(speedId);

        speedGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                String saveSpeed;
                switch (checkedId) {
                    case R.id.slowButton:
                        saveSpeed = "s";
                        break;
                    case R.id.normalButton:
                        saveSpeed = "n";
                        break;
                    case R.id.fastButton:
                        saveSpeed = "f";
                        break;
                    case R.id.extremeButton:
                        saveSpeed = "x";
                        break;
                    case R.id.dynamicButton:
                        saveSpeed = "d";
                        break;
                    case R.id.randomButton:
                        saveSpeed = "r";
                        break;
                    default:
                        speedGroup.check(R.id.randomButton);
                        saveSpeed = "n";
                }

                optionEdit.putString("speed", saveSpeed);
                optionEdit.commit();
            }
        });

        int colourInt=optionPrefs.getInt("colour", 0xff00ff00);
        int colourId;
        switch(colourInt){
            case 0xff00aa00:
                colourId=R.id.originalButton;
                break;
            case 0xff00ff00:
                colourId=R.id.classicButton;
                break;
            case 0xffff0000:
                colourId=R.id.brightButton;
                break;
            case 0xff0000ff:
                colourId=R.id.uglyButton;
                break;
            case 0xfffcf914:
                colourId=R.id.chillButton;
                break;
            case 0xffe914fc:
                colourId=R.id.sunriseButton;
                break;
            case 0xffffffff:
                colourId=R.id.albinoButton;
                break;
            default:
                colourId=R.id.classicButton;
        }

        colourGroup = (RadioGroup)findViewById(R.id.colourGroup);
        brightButton=(RadioButton)findViewById(R.id.brightButton);
        uglyButton=(RadioButton)findViewById(R.id.uglyButton);
        chillButton=(RadioButton)findViewById(R.id.chillButton);
        sunriseButton=(RadioButton)findViewById(R.id.sunriseButton);
        albinoButton=(RadioButton)findViewById(R.id.albinoButton);
        colourGroup.check(colourId);

        colourGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                int saveColour;
                int saveComplementary = 0;
                switch (checkedId) {

                    //create normal theme with the darker green all the way through as DEFAULT and only one unlocked upon download
                    case R.id.originalButton://original Theme
                        saveComplementary = saveColour = 0xff00aa00;//dark green
                        break;
                    case R.id.blueButton:
                        saveComplementary = saveColour = 0xff0000dd;//light blue
                        break;
                    case R.id.redButton:
                        saveComplementary = saveColour = 0xffdd0000;//light red
                        break;
                    case R.id.classicButton://Classic Theme
                        saveColour = 0xff00ff00;//green
                        saveComplementary = 0xffff00ff;//magenta
                        break;
                    case R.id.brightButton://Bright Theme
                        saveColour = 0xff1caac9;//light blue
                        saveComplementary = 0xffc92b1c;//orange-red
                        break;
                    case R.id.uglyButton://Ugly Duckling
                        saveColour = 0xff8873BF;//purple
                        saveComplementary = 0xffBFAE73;//ugly yellow
                        break;
                    case R.id. chillButton://Chill Theme
                        saveColour = 0xffFC14FC;//bright purple
                        saveComplementary = 0xff1488FC;//blue
                        break;
                    case R.id.sunriseButton://Sunrise Theme
                        saveColour = 0xffFC141C;//orange
                        saveComplementary = 0xffFC8014;//red
                        break;
                    case R.id.albinoButton:
                        saveComplementary = saveColour = 0xffffffff;//white
                        break;
                    default:
                        colourGroup.check(R.id.originalButton);//check original theme box
                        saveComplementary = saveColour = 0xff00aa00;
                }

                optionEdit.putInt("colour", saveColour);
                optionEdit.putInt("complementary", saveComplementary);
                optionEdit.commit();
            }
        });


        soundEnabled=(CheckBox)findViewById(R.id.soundBox);
        soundEnabled.setChecked(optionPrefs.getBoolean("soundEnabled", true));
        soundEnabled.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                optionEdit.putBoolean("soundEnabled", isChecked);
                optionEdit.commit();
            }
        });

        nameText=(EditText)findViewById(R.id.nameText);
        optionPrefs=getApplication().getSharedPreferences("options", MODE_PRIVATE);
        nameText.setText(optionPrefs.getString("name","noname"));
    }
    public void startGame(View v){
        if(v.getId()==R.id.startButton){
            SharedPreferences.Editor saveEdit=save.edit();
            saveEdit.putBoolean("gameSaved", false);
            saveEdit.commit();
        }
        optionEdit=optionPrefs.edit();
        String name=nameText.getText().toString();
        if(name.length()>20)name=name.substring(0,20);
        optionEdit.putString("name",name);
        optionEdit.commit();
        startActivity(new Intent(this,GameActivity.class));
    }

    @Override
    public void onResume(){
        super.onResume();
        Log.d("OptionsActivity","OnResume");
        save=getApplication().getSharedPreferences("save", Activity.MODE_PRIVATE);
        if(save.getBoolean("gameSaved",false)){
            findViewById(R.id.resumeButton).setVisibility(View.VISIBLE);
        }
        else{
            findViewById(R.id.resumeButton).setVisibility(View.INVISIBLE);
        }

        SharedPreferences highscorePrefs=getApplication().getSharedPreferences("highscores", Activity.MODE_PRIVATE);

        brightButton.setEnabled(highscorePrefs.getInt("highs",-1)>=20);
        uglyButton.setEnabled(highscorePrefs.getInt("highn",-1)>=20);
        chillButton.setEnabled(highscorePrefs.getInt("highf",-1)>=20);
        sunriseButton.setEnabled(highscorePrefs.getInt("highd",-1)>=20);
        albinoButton.setEnabled(highscorePrefs.getInt("highx",-1)>=20);
        //brightButton.setEnabled(highscorePrefs.getInt("highr",-1)>=20);
    }
}
