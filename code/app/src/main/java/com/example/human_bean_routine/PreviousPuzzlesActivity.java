package com.example.human_bean_routine;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;

public class PreviousPuzzlesActivity extends AppCompatActivity {

    Puzzle puzzle1 = new Puzzle(1, "lavender", false, "res/drawable/lavender.jpg", true);
    Puzzle puzzle2 = new Puzzle(2, "mountain", false, "res/drawable/mountain_river.jpg", true);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_previous_puzzles);

        // create dynamic list of buttons
        Puzzle[] puzzles = new Puzzle[2];
        puzzles[0] = puzzle1;
        puzzles[1] = puzzle2;
        LinearLayout ll = (LinearLayout)findViewById(R.id.llPuzzleList);
        LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);

        for(int i=0; i<puzzles.length; ++i) {
            String text = puzzles[i].getName();
            Button button = new Button(this);
            button.setText(text);
            ll.addView(button, lp);
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