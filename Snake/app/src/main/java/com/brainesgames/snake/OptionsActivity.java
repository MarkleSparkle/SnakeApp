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
import android.widget.RadioGroup;

public class OptionsActivity extends AppCompatActivity {
    SharedPreferences optionPrefs,save;
    SharedPreferences.Editor optionEdit;
    RadioGroup speedGroup, colourGroup;
    CheckBox soundEnabled;
    EditText nameText;

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
            case 0xff00ff00:
                colourId=R.id.greenButton;
                break;
            case 0xffff0000:
                colourId=R.id.redButton;
                break;
            case 0xff0000ff:
                colourId=R.id.blueButton;
                break;
            case 0xfffcf914:
                colourId=R.id.yellowButton;
                break;
            case 0xffe914fc:
                colourId=R.id.purpleButton;
                break;
            case 0xffffffff:
                colourId=R.id.whiteButton;
                break;
            default:
                colourId=R.id.greenButton;
        }

        colourGroup = (RadioGroup)findViewById(R.id.colourGroup);
        colourGroup.check(colourId);

        colourGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                int saveColour;
                switch (checkedId) {
                    case R.id.greenButton:
                        saveColour= 0xff00ff00;
                        break;
                    case R.id.redButton:
                        saveColour = 0xffff0000;
                        break;
                    case R.id.blueButton:
                        saveColour = 0xff0000ff;
                        break;
                    case R.id.yellowButton:
                        saveColour = 0xfffcf914;
                        break;
                    case R.id.purpleButton:
                        saveColour = 0xffe914fc;
                        break;
                    case R.id.whiteButton:
                        saveColour = 0xffffffff;
                        break;
                    default:
                        colourGroup.check(R.id.greenButton);
                        saveColour = 0xff00ff00;
                }

                optionEdit.putInt("colour", saveColour);
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
        optionPrefs=getApplication().getSharedPreferences("options", MODE_PRIVATE);
        nameText.setText(optionPrefs.getString("name","noname"));
    }
}
