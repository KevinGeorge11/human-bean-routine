package com.example.human_bean_routine;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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

//    ArrayList<Bitmap> pieces;
    PuzzlePiece[] pieces = new PuzzlePiece[12];
    PopupWindow modal;
    int numPieces = 12;
    int rows = 4;
    int cols = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_puzzle);

        final ConstraintLayout layout = findViewById(R.id.clPuzzleLayout);
        ImageView imageView = findViewById(R.id.ivPuzzle);

        Puzzle currentPuzzle = new Puzzle(1, "lavender", false, "lavender", true);
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);

        // prioritize puzzleRequest and replace current puzzle if not the same
        int puzzleRequest = getIntent().getIntExtra("puzzleID", currentPuzzle.getPuzzleID()); //getCurrentPuzzle();
        if(currentPuzzle.getPuzzleID() != puzzleRequest) {
            currentPuzzle = new Puzzle(2, "mountain", false, "mountain_river", true);
            //getPuzzle(puzzleRequest);
        }

        // replace image in imageView using the file path
        int resID = getResources().getIdentifier(currentPuzzle.getImagePath(), "drawable", "com.example.human_bean_routine");
        imageView.setImageResource(resID);

        int tileHeight = imageView.getHeight() / rows;
        int tileWidth = imageView.getWidth() / cols;
//        pieces = getPuzzlePieces(currentPuzzle.getPuzzleID());


        Button previousPuzzles = findViewById(R.id.bPreviousPuzzles);
        previousPuzzles.setOnClickListener(v -> {
            Intent i = new Intent(getApplicationContext(), PreviousPuzzlesActivity.class);
            startActivity(i);
        });
    }

    private ArrayList<Bitmap> splitImage() {
        ImageView imageView = findViewById(R.id.ivPuzzle);
        ArrayList<Bitmap> pieces = new ArrayList<>(numPieces);

        // Get the bitmap of the source image
        BitmapDrawable drawable = (BitmapDrawable) imageView.getDrawable();
        Bitmap bitmap = drawable.getBitmap();

        // Calculate the with and height of the pieces
        int pieceWidth = bitmap.getWidth()/cols;
        int pieceHeight = bitmap.getHeight()/rows;

        // Create each bitmap piece and add it to the resulting array
        int y = 0;
        for (int row = 0; row < rows; row++) {
            int x = 0;
            for (int col = 0; col < cols; col++) {
//                int x = pieceWidth*row;
//                int y = pieceHeight*col;
                pieces.add(Bitmap.createBitmap(bitmap, x, y, pieceWidth, pieceHeight));

                x += pieceWidth;
            }
            y += pieceHeight;
        }

        return pieces;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float touchX = event.getX();
        float touchY = event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:

        }

        return true;
    }

    public void generateBlackTiles() {

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
    public void saveMessage() {
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
