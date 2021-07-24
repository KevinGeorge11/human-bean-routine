package com.example.human_bean_routine;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import java.io.File;
import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

public class PuzzleActivity extends AppCompatActivity {

    PuzzlePiece[] pieces = new PuzzlePiece[12];

    PopupWindow modal;
    int numPieces = 12;
    int rows = 4;
    int cols = 3;
    PuzzlePiece p1 = new PuzzlePiece(1, 1, 1, 10, 1, PuzzlePiece.PieceStatus.REVEALED, "August 12", 5, "Great job me!");
    PuzzlePiece p2 = new PuzzlePiece(2, 2, 1, 10, 1, PuzzlePiece.PieceStatus.UNLOCKED, "August 12", 5, "Great job me!");
    PuzzlePiece p3 = new PuzzlePiece(3, 3, 1, 10, 1, PuzzlePiece.PieceStatus.LOCKED, "August 12", 5, "Great job me!");
    PuzzlePiece p4 = new PuzzlePiece(4, 1, 2, 10, 1, PuzzlePiece.PieceStatus.LOCKED, "August 12", 5, "Great job me!");
    PuzzlePiece p5 = new PuzzlePiece(5, 2, 2, 10, 1, PuzzlePiece.PieceStatus.LOCKED, "August 12", 5, "Great job me!");
    PuzzlePiece p6 = new PuzzlePiece(6, 3, 2, 10, 1, PuzzlePiece.PieceStatus.LOCKED, "August 12", 5, "Great job me!");
    PuzzlePiece p7 = new PuzzlePiece(7, 1, 3, 10, 1, PuzzlePiece.PieceStatus.LOCKED, "August 12", 5, "Great job me!");
    PuzzlePiece p8 = new PuzzlePiece(8, 2, 3, 10, 1, PuzzlePiece.PieceStatus.LOCKED, "August 12", 5, "Great job me!");
    PuzzlePiece p9 = new PuzzlePiece(9, 3, 3, 10, 1, PuzzlePiece.PieceStatus.LOCKED, "August 12", 5, "Great job me!");
    PuzzlePiece p10 = new PuzzlePiece(10, 1, 4, 10, 1, PuzzlePiece.PieceStatus.LOCKED, "August 12", 5, "Great job me!");
    PuzzlePiece p11 = new PuzzlePiece(11, 2, 4, 10, 1, PuzzlePiece.PieceStatus.LOCKED, "August 12", 5, "Great job me!");
    PuzzlePiece p12 = new PuzzlePiece(12, 3, 4, 10, 1, PuzzlePiece.PieceStatus.LOCKED, "August 12", 5, "Great job me!");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_puzzle);

        final ConstraintLayout layout = findViewById(R.id.clPuzzleLayout);
        ImageView imageView = findViewById(R.id.ivPuzzle);

        Puzzle currentPuzzle = new Puzzle(1, "lavender", false, "lavender", true);

        // prioritize puzzleRequest and replace current puzzle if not the same
        int puzzleRequest = getIntent().getIntExtra("puzzleID", currentPuzzle.getPuzzleID()); //getCurrentPuzzle();
        if(currentPuzzle.getPuzzleID() != puzzleRequest) {
            currentPuzzle = new Puzzle(2, "mountain", false, "mountain_river", true);
            //getPuzzle(puzzleRequest);
        }

        // replace image in imageView using the file path
        int resID = getResources().getIdentifier(currentPuzzle.getImagePath(), "drawable", "com.example.human_bean_routine");
        imageView.setImageResource(resID);
        imageView.setVisibility(View.INVISIBLE);
//        pieces = getPuzzlePieces(currentPuzzle.getPuzzleID());

        pieces[0] = p1;
        pieces[1] = p2;
        pieces[2] = p3;
        pieces[3] = p4;
        pieces[4] = p5;
        pieces[5] = p6;
        pieces[6] = p7;
        pieces[7] = p8;
        pieces[8] = p9;
        pieces[9] = p10;
        pieces[10] = p11;
        pieces[11] = p12;

        Button previousPuzzles = findViewById(R.id.bPreviousPuzzles);
        previousPuzzles.setOnClickListener(v -> {
            Intent i = new Intent(getApplicationContext(), PreviousPuzzlesActivity.class);
            startActivity(i);
        });
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        // create puzzle piece buttons here since imageView's getHeight() and getWidth()
        // can't be used in onCreate
        final ConstraintLayout layout = findViewById(R.id.clPuzzleLayout);
        ImageView imageView = findViewById(R.id.ivPuzzle);
        Button[] overlay = new Button[12];

        int tileHeight = imageView.getHeight() / rows;
        int tileWidth = imageView.getWidth() / cols;

        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);

        for(int i=0; i<pieces.length; ++i) {
            int x = (int)pieces[i].getxCoord()-1;
            int y = (int)pieces[i].getyCoord()-1;
            Button button = new Button(this);
            button.setWidth(tileWidth);
            button.setHeight(tileHeight);
            button.setX(imageView.getX() + x*tileWidth);
            button.setY(imageView.getY() + y*tileHeight);

            if(pieces[i].getStatus() == PuzzlePiece.PieceStatus.UNLOCKED) {
                button.setBackgroundColor(getResources().getColor(R.color.dark_green));
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        View popupView = inflater.inflate(R.layout.completed_puzzle_piece_modal, null);
                        showPuzzleModal(popupView);
                    }
                });
            } else if(pieces[i].getStatus() == PuzzlePiece.PieceStatus.REVEALED) {
                button.setBackgroundColor(Color.TRANSPARENT);
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.d("PuzzleActivity", "clicked revealed piece");
                        View popupView = inflater.inflate(R.layout.old_puzzle_piece_modal, null);
                        showPuzzleModal(popupView);
                    }
                });
            } else {
                button.setBackgroundColor(Color.BLACK);
            }
            overlay[i] = button;
            layout.addView(button);
        }
        imageView.setVisibility(View.VISIBLE);
    }

    public void showPuzzleModal(View popupView) {
        int width = LinearLayout.LayoutParams.WRAP_CONTENT;
        int height = LinearLayout.LayoutParams.WRAP_CONTENT;
        boolean focusable = true; // lets taps outside the popup also dismiss it
        modal = new PopupWindow(popupView, width, height, focusable);
        modal.showAtLocation(findViewById(R.id.ivPuzzle), Gravity.CENTER, 0, 0);

        popupView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                modal.dismiss();
                return true;
            }
        });
    }

    // saveMessage takes the text from the etNote textbox and updates the database
    // so that puzzle pieces will have an associated message
    public void saveMessage(View view) {
        int xCoord = 1;
        int yCoord = 1;
        int edgeLength = 1;
        int puzzleID = 1;

        EditText editText = findViewById(R.id.etNote);
        String message = editText.getText().toString();

        if(message.matches("")) { // if user doesn't input a message, choose a random one
            int min = 1;
            int max = 6;
            int randomNum = ThreadLocalRandom.current().nextInt(min, max + 1);
            String[] array = getResources().getStringArray(R.array.encouraging_messages);
            message = array[randomNum];
        }
        
        PuzzlePiece newPiece = new PuzzlePiece(xCoord, yCoord, edgeLength,  puzzleID, PuzzlePiece.PieceStatus.REVEALED);

    }
}
