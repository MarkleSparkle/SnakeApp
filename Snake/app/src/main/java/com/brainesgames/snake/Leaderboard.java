package com.brainesgames.snake;

import android.content.SharedPreferences;

/**
  Created by Orson Baines on 2016-05-06.
 */
public class Leaderboard {
    final int length=10;
    SharedPreferences prefs;
    SharedPreferences.Editor edit;

    Leaderboard(SharedPreferences prefs){
        this.prefs=prefs;
        edit=prefs.edit();
    }

    //test if score is high enough to make leaderboard, if so adds to leadreboard
    void addScore(int score,String name,String mode){
        for(int i=1;i<=length;i++){
            //if score belongs  at this position
            if(prefs.getInt("score"+i,-1)<score){
                //if positition is empty
                if(prefs.getInt("score"+i,-1)==-1){
                    edit.putInt("score" + i, score);
                    edit.putString("name" + i, name);
                    edit.putString("mode"+i,mode);
                    break;
                }
                else{
                    for(int j=length-1;j>=i;j--){
                        edit.putInt("score"+(j+1),prefs.getInt("score"+j,-1));
                        edit.putString("name"+(j+1),prefs.getString("name"+j,""));
                        edit.putString("mode"+(j+1),prefs.getString("mode"+j,""));
                    }
                    edit.putInt("score" + i, score);
                    edit.putString("name" + i, name);
                    edit.putString("mode"+i,mode);
                    break;
                }
            }
        }
        edit.commit();
    }

    int numEntries(){
        for(int i=1;i<=length;i++){
            //if score belongs  at this position
            if(prefs.getInt("score"+i,-1)==-1)return i-1;
        }
        return length;
    }

    int score(int i){
        return prefs.getInt("score"+i,-1);
    }

    String name(int i){
        return prefs.getString("name"+i,"");
    }

    String mode(int i){
        return prefs.getString("mode"+i,"");
    }


/*    String numberColumn(){
        String nums="#\n";
        for(int i=1;i<=length;i++){
            if(prefs.getInt("score"+i,-1)==-1)break;
            else{
                nums+=i+".\n";
            }
        }
        return nums;
    }

    String scoreColumn(){
        String scores="SCORE\n_____\n";
        for(int i=1;i<=length;i++){
            if(prefs.getInt("score"+i,-1)==-1)break;
            else{
                scores+=prefs.getInt("score"+i,-1)+"\n";
            }
        }
        return scores;
    }

    String nameColumn(){
        String names="NAME\n_____\n";
        for(int i=1;i<=length;i++){
            if(prefs.getInt("score"+i,-1)==-1)break;
            else{
                names+=prefs.getString("name" + i, "")+"\n";
            }
        }
        return names;
    }

    String modeColumn(){
        String modes="MODE\n_____\n";
        for(int i=1;i<=length;i++){
            if(prefs.getInt("score"+i,-1)==-1)break;
            else{
                modes+=prefs.getString("mode"+i,"")+"\n";
            }
        }
        return modes;
    }*/
}
