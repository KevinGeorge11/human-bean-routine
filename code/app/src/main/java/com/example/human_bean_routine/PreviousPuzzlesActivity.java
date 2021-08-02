package com.example.human_bean_routine;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;

import java.util.ArrayList;
import java.util.List;


public class PreviousPuzzlesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_previous_puzzles);

        // create dynamic list of buttons
        DataBaseHelper db = DataBaseHelper.getDbInstance(this);
        List<Puzzle> puzzles = db.getCompletedPuzzles();

        LinearLayout ll = (LinearLayout)findViewById(R.id.llPuzzleList);
        LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);

        // create a button for each completed puzzle
        for(int i=0; i<puzzles.size(); ++i) {
            String text = puzzles.get(i).getName();
            Button button = new Button(this);
            button.setText(text);
            button.setBackgroundColor(Color.WHITE);
            lp.setMargins(30,30,30,0);
            ll.addView(button, lp);
            final int id = i;
            // clicking will open the puzzle page with the old puzzle
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getApplicationContext(),PuzzleActivity.class);
                    intent.putExtra("puzzleID", puzzles.get(id).getPuzzleID());
                    startActivity(intent);
                }
            });
        }

        // set navigation back to the current puzzle page
        Button currentPuzzleButton = findViewById(R.id.bCurrentPuzzle);
        currentPuzzleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(),PuzzleActivity.class);
                startActivity(i);
            }
        });
    }
}