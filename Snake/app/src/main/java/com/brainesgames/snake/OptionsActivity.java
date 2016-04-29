package com.brainesgames.snake;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.RadioGroup;

public class OptionsActivity extends AppCompatActivity {
    SharedPreferences optionPrefs;
    SharedPreferences.Editor opEdit;
    RadioGroup speedGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_options);

        optionPrefs=getApplication().getSharedPreferences("options", MODE_PRIVATE);
        opEdit=optionPrefs.edit();

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
                String saveSpeed="";
                switch (checkedId){
                    case R.id.slowButton:
                        saveSpeed="s";
                        break;
                    case R.id.normalButton:
                        saveSpeed="n";
                        break;
                    case R.id.fastButton:
                        saveSpeed="f";
                        break;
                    case R.id.extremeButton:
                        saveSpeed="x";
                        break;
                    case R.id.dynamicButton:
                        saveSpeed="d";
                        break;
                    case R.id.randomButton:
                        saveSpeed="r";
                        break;
                    default:
                        speedGroup.check(R.id.randomButton);
                        saveSpeed="n";
                }

                opEdit.putString("speed",saveSpeed);
                opEdit.commit();
            }
        });
    }
    public void startGame(View v){
        startActivity(new Intent(this,GameActivity.class));
    }

}
