package com.brainesgames.snake;

import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class LeaderboardActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_leaderboard);

        Leaderboard leaderboard = new Leaderboard(getApplication().getSharedPreferences("leaderboard", MODE_PRIVATE));
        int numEntries=leaderboard.numEntries();
        TableLayout leaderTable = (TableLayout) findViewById(R.id.leaderTable);
        TableRow[] rows = new TableRow[numEntries];
        for(int i=0;i<rows.length;i++){
            rows[i]=new TableRow(this);
            rows[i].setGravity(Gravity.CENTER_HORIZONTAL);
            leaderTable.addView(rows[i]);
        }

        Log.d("MainActivity", "numEntries: " + numEntries);
        TextView[][] leaderTexts = new TextView[numEntries][4];
        for (int i = 0; i < leaderTexts.length; i++) {
            for (int j = 0; j < 4; j++) {
                leaderTexts[i][j] = new TextView(this);
                leaderTexts[i][j].setTextColor(0xff00e100);
                leaderTexts[i][j].setPadding(getResources().getDimensionPixelSize(R.dimen.leader_padding), 0, getResources().getDimensionPixelSize(R.dimen.leader_padding), 0);
                leaderTexts[i][j].setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimensionPixelSize(R.dimen.leader_text_size));
                leaderTexts[i][j].setTypeface(Typeface.MONOSPACE);
                leaderTexts[i][j].setGravity(Gravity.LEFT);
                switch (j) {
                    case 0:
                        leaderTexts[i][j].setText((i+1) + ".");
                        break;
                    case 1:
                        leaderTexts[i][j].setText(leaderboard.score(i+1));
                        break;
                    case 2:
                        leaderTexts[i][j].setText(leaderboard.name(i+1));
                        break;
                    case 3:
                        leaderTexts[i][j].setText(leaderboard.mode(i+1));
                        break;
                }
                rows[i].addView(leaderTexts[i][j]);
            }
        }
    }

    public void backToMain(View v){finish();}
}
