package com.example.human_bean_routine;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import static java.util.Objects.nonNull;

public class PuzzleActivity extends AppCompatActivity {

    List<PuzzlePiece> pieces = new ArrayList<PuzzlePiece>();
    Puzzle currentPuzzle;
    EditText editText;

    PopupWindow modal;
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

//        final ConstraintLayout layout = findViewById(R.id.clPuzzleLayout);
        ImageView imageView = findViewById(R.id.ivPuzzle);
//        RequestQueue queue = Volley.newRequestQueue(this);

        DataBaseHelper db = DataBaseHelper.getDbInstance(this);

        currentPuzzle = new Puzzle(1, "lavender", false, "lavender", true);

        // prioritize puzzleRequest and replace current puzzle if not the same
        int puzzleRequest = getIntent().getIntExtra("puzzleID", currentPuzzle.getPuzzleID()); //getCurrentPuzzle();
        if (currentPuzzle.getPuzzleID() != puzzleRequest) {
            currentPuzzle = db.getPuzzleByID(puzzleRequest);
            // ^ There is also db.getActivePuzzle()
        }

        // replace image in imageView using the file path
        int resID = getResources().getIdentifier(currentPuzzle.getImagePath(), "drawable", "com.example.human_bean_routine");
        imageView.setImageResource(resID);
        imageView.setVisibility(View.INVISIBLE);
        pieces = db.getPuzzlePieces(currentPuzzle.getPuzzleID());

        int currentTasks = db.getCurrentNumberOfTasks();
        TextView currentCompletedTasks = findViewById(R.id.tvNumCurrentTasks);
        ProgressBar progress = findViewById(R.id.pbNumCurrentTasks);
        progress.setProgress(20*currentTasks);
        currentCompletedTasks.setText(String.valueOf(currentTasks));

        Button previousPuzzles = findViewById(R.id.bPreviousPuzzles);
        previousPuzzles.setOnClickListener(v -> {
            Intent i = new Intent(getApplicationContext(), PreviousPuzzlesActivity.class);
            startActivity(i);
        });
    }

    @Override
//    public void onAttachedToWindow() {
    public void onWindowFocusChanged(boolean hasFocus) {
        Log.d("PuzzleActivity", "WINDOW FOCUS CHANGED");
        // create puzzle piece buttons here since imageView's getHeight() and getWidth()
        // can't be used in onCreate
        final ConstraintLayout layout = findViewById(R.id.clPuzzleLayout);
        ImageView imageView = findViewById(R.id.ivPuzzle);
        if(nonNull(imageView)) {
            Button[] overlay = new Button[12];

            int tileHeight = imageView.getHeight() / rows;
            int tileWidth = imageView.getWidth() / cols;

            for (int i = 0; i < pieces.size(); ++i) {
                int x = (int) pieces.get(i).getxCoord() - 1;
                int y = (int) pieces.get(i).getyCoord() - 1;

                GradientDrawable gd = new GradientDrawable();
                gd.setStroke(1, 0xFFFFFFFF);

                Button button = new Button(this);
                button.setWidth(tileWidth);
                button.setHeight(tileHeight);
                button.setX(imageView.getX() + x * tileWidth);
                button.setY(imageView.getY() + y * tileHeight);

                if (pieces.get(i).getStatus() == PuzzlePiece.PieceStatus.UNLOCKED) {
                    final PuzzlePiece p = pieces.get(i);
                    final int idx = i;
                    gd.setColor(getResources().getColor(R.color.dark_green));
                    button.setBackground(gd);
                    button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            showPuzzleModal(p);
                            gd.setColor(Color.TRANSPARENT);
                            overlay[idx].setBackground(gd);
                        }
                    });
                } else if (pieces.get(i).getStatus() == PuzzlePiece.PieceStatus.REVEALED) {
                    final PuzzlePiece p = pieces.get(i);
                    gd.setColor(Color.TRANSPARENT);
                    button.setBackground(gd);
                    button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            showPuzzleModal(p);
                        }
                    });
                } else {
                    gd.setColor(Color.BLACK);
                    button.setBackground(gd);
                }
                overlay[i] = button;
                layout.addView(button);
            }
            imageView.setVisibility(View.VISIBLE);
        }
    }

    public void showPuzzleModal(PuzzlePiece p) {
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        int layout = (p.getStatus() == PuzzlePiece.PieceStatus.REVEALED) ? R.layout.old_puzzle_piece_modal : R.layout.completed_puzzle_piece_modal;
        View popupView = inflater.inflate(layout, null);

        int width = LinearLayout.LayoutParams.WRAP_CONTENT;
        int height = LinearLayout.LayoutParams.WRAP_CONTENT;
        boolean focusable = true;//(p.getStatus() == PuzzlePiece.PieceStatus.REVEALED) ? true : false; // lets taps outside the popup also dismiss it
        modal = new PopupWindow(popupView, width, height, focusable);
        modal.setBackgroundDrawable(null);
        modal.showAtLocation(findViewById(R.id.ivPuzzle), Gravity.CENTER, 0, 0);
        modal.setOutsideTouchable(false);

        if(p.getStatus() == PuzzlePiece.PieceStatus.REVEALED) {
            TextView encouragingMessage = (TextView) modal.getContentView().findViewById(R.id.tvMessage);
            encouragingMessage.setText(p.getUserMessage());
            TextView reason = (TextView) modal.getContentView().findViewById(R.id.tvReason);
            reason.setText(p.generateUnlockMessage());
            TextView date = (TextView) modal.getContentView().findViewById(R.id.tvDateCompleted);
            date.setText(p.getDateUnlocked());
        } else {
//            modal.setFocusable(false);
            p.setStatus(PuzzlePiece.PieceStatus.REVEALED);
            Button b = modal.getContentView().findViewById(R.id.bSave);
            editText = modal.getContentView().findViewById(R.id.etNote);
            b.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    saveMessage(v, p);
                    checkComplete(v);
                }
            });
        }
    }

    // saveMessage takes the text from the etNote textbox and updates the database
    // so that puzzle pieces will have an associated message
    public void saveMessage(View view, PuzzlePiece piece) {
        int xCoord = 1;
        int yCoord = 1;
        int edgeLength = 1;
        int puzzleID = 2;
        DataBaseHelper db = DataBaseHelper.getDbInstance(this);

        String message = editText.getText().toString();
        if(message.isEmpty()) { // if user doesn't input a message, choose a random one
            int min = 1;
            int max = 5;
            int randomNum = ThreadLocalRandom.current().nextInt(min, max + 1);
            String[] array = getResources().getStringArray(R.array.encouraging_messages);
            message = array[randomNum];
        }
        close(view);

        PuzzlePiece newPiece = new PuzzlePiece(xCoord, yCoord, edgeLength,  puzzleID, PuzzlePiece.PieceStatus.REVEALED);
        piece.setUserMessage(message);
        db.updatePiece(newPiece);
    }

    public void close(View view) {
        this.modal.dismiss();
    }

    public boolean checkComplete(View v) {
        for(int i=0; i<pieces.size(); ++i) {
            if(pieces.get(i).getStatus() != PuzzlePiece.PieceStatus.REVEALED) {
                return false;
            }
        }
        currentPuzzle.setComplete(true);
        DataBaseHelper db = DataBaseHelper.getDbInstance(this);
        db.updatePuzzle(currentPuzzle);
        return true;
    }
}
