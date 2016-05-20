package com.brainesgames.snake;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

public class OptionsActivity extends AppCompatActivity {
    SharedPreferences optionPrefs,highscorePrefs,save;
    SharedPreferences.Editor optionEdit;
    RadioGroup speedGroup, colourGroup;
    CheckBox soundEnabled;
    EditText nameText;
    Skin[] skins;
    Context ctx;
    int lastCheckedId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_options);
        ctx=this;
        optionPrefs=getApplication().getSharedPreferences("options", MODE_PRIVATE);
        optionEdit=optionPrefs.edit();
        highscorePrefs=getApplication().getSharedPreferences("highscores", Activity.MODE_PRIVATE);

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
            case 0xff0000dd:
                colourId=R.id.blueButton;
                break;
            case 0xffdd0000:
                colourId=R.id.redButton;
                break;
            case 0xffC0B500:
                colourId=R.id.mustardButton;
                break;
            case 0xffC64C00:
                colourId=R.id.orangeRedButton;
                break;
            case 0xff1DAE75:
                colourId=R.id.tealButton;
                break;
            case 0xffFF89E8:
                colourId=R.id.hotPinkButton;
                break;
            case 0xffFCD66F:
                colourId=R.id.blehButton;
                break;
            case 0xffF9E800:
                colourId=R.id.royaltyButton;
                break;
            case 0xff153E36:
                colourId=R.id.nightSkyButton;
                break;
            case 0xff26B51D:
                colourId=R.id.greenerGrassButton;
                break;
            case 0xff33BCEE:
                colourId=R.id.babyBelugaButton;
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
                colourId=R.id.originalButton;
        }

        colourGroup = (RadioGroup)findViewById(R.id.colourGroup);
        colourGroup.check(colourId);
        lastCheckedId=colourId;
        skins=new Skin[18];
        skins[0]=new Skin(highscorePrefs,(RadioButton)findViewById(R.id.originalButton),"s",-1);
        skins[1]=new Skin(highscorePrefs,(RadioButton)findViewById(R.id.blueButton),"s",-1);
        skins[2]=new Skin(highscorePrefs,(RadioButton)findViewById(R.id.redButton),"s",-1);
        skins[3]=new Skin(highscorePrefs,(RadioButton)findViewById(R.id.brightButton),"s",40);
        skins[4]=new Skin(highscorePrefs,(RadioButton)findViewById(R.id.uglyButton),"n",40);
        skins[5]=new Skin(highscorePrefs,(RadioButton)findViewById(R.id.chillButton),"f",40);
        skins[6]=new Skin(highscorePrefs,(RadioButton)findViewById(R.id.sunriseButton),"x",40);
        skins[7]=new Skin(highscorePrefs,(RadioButton)findViewById(R.id.albinoButton),"d",40);
        skins[8]=new Skin(highscorePrefs,(RadioButton)findViewById(R.id.classicButton),"r",40);
        skins[9]=new Skin(highscorePrefs,(RadioButton)findViewById(R.id.mustardButton),"s",1);
        skins[10]=new Skin(highscorePrefs,(RadioButton)findViewById(R.id.orangeRedButton),"n",1);
        skins[11]=new Skin(highscorePrefs,(RadioButton)findViewById(R.id.tealButton),"f",1);
        skins[12]=new Skin(highscorePrefs,(RadioButton)findViewById(R.id.hotPinkButton),"x",1);
        skins[13]=new Skin(highscorePrefs,(RadioButton)findViewById(R.id.blehButton),"d",1);
        skins[14]=new Skin(highscorePrefs,(RadioButton)findViewById(R.id.greenerGrassButton),"r",1);
        skins[15]=new Skin(highscorePrefs,(RadioButton)findViewById(R.id.babyBelugaButton),"s",20);
        skins[16]=new Skin(highscorePrefs,(RadioButton)findViewById(R.id.nightSkyButton),"n",20);
        skins[17]=new Skin(highscorePrefs,(RadioButton)findViewById(R.id.royaltyButton),"f",20);

        colourGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                Skin skin=getSkin(checkedId);
                if(skin!=null && skin.isLocked()){
                    Skin lastSkin=getSkin(lastCheckedId);
                    if(lastSkin==null || lastSkin.isLocked())group.check(skins[0].button.getId());
                    else group.check(lastCheckedId);

                    Toast.makeText(ctx,skin.lockMessage(),Toast.LENGTH_LONG).show();
                    return;
                }
                int saveColour,saveComplementary;
                switch (checkedId) {

                    //create normal theme with the darker green all the way through as DEFAULT and only one unlocked upon download
                    case R.id.originalButton://original Theme
                        saveComplementary = saveColour = 0xff00aa00;//dark green
                        break;
                    case R.id.blueButton://blue Theme
                        saveComplementary = saveColour = 0xff0000dd;//light blue
                        break;
                    case R.id.redButton://red Theme
                        saveComplementary = saveColour = 0xffdd0000;//light red
                        break;
                    case R.id.mustardButton://Mustard Theme
                        saveComplementary = saveColour = 0xffC0B500;
                        break;
                    case R.id.orangeRedButton://Orange-Red Theme
                        saveComplementary = saveColour = 0xffC64C00;
                        break;
                    case R.id.tealButton://Teal Deal Theme
                        saveComplementary = saveColour = 0xff1DAE75;
                        break;
                    case R.id.hotPinkButton://Hot Pink Theme
                        saveComplementary = saveColour = 0xffFF89E8;
                        break;
                    case R.id.blehButton://bleh Theme
                        saveComplementary = 0xffFCD66F;
                        saveColour = 0xffFCD66F;
                        break;
                    case R.id.royaltyButton://Royalty Theme
                        saveComplementary = 0xff9307B5;
                        saveColour = 0xffF9E800;
                        break;
                    case R.id.nightSkyButton://Night Sky Theme
                        saveComplementary = 0xff9E12E7;
                        saveColour = 0xff153E36;
                        break;
                    case R.id.greenerGrassButton://Greener Grass Theme
                        saveComplementary = 0xff227830;
                        saveColour = 0xff26B51D;
                        break;
                    case R.id.babyBelugaButton://Baby Beluga Theme
                        saveComplementary = 0xffFF55FF;
                        saveColour = 0xff33BCEE;
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
                lastCheckedId=checkedId;
            }
        });
            //easy achieved schemes are coloured FFF30A
            //medium  --  0A91FF
            //hard    --  FF780A
            //super   --  F30AFF

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

        highscorePrefs=getApplication().getSharedPreferences("highscores", Activity.MODE_PRIVATE);

        for(int i=0;i<skins.length;i++){
            skins[i].highscorePrefs=highscorePrefs;
            //skins[i].disableLocked();
        }
    }

    Skin getSkin(int id){
        for(int i=0;i<skins.length;i++){
            if(id==skins[i].button.getId())return skins[i];
        }
        return null;
    }
}
