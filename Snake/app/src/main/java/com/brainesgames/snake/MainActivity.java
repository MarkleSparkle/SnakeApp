package com.brainesgames.snake;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.brainesgames.ascii.AsciiCanvas;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView title=(TextView) findViewById(R.id.title);
        AsciiCanvas ac=AsciiCanvas.load(getResources().openRawResource(R.raw.titleart));

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
    public void optionsMenu(View v) { startActivity(new Intent(this,OptionsActivity.class)); }


    /** Called when the user clicks the START button */
    public void startGame(View v){
        startActivity(new Intent(this,GameActivity.class));
    }
}


