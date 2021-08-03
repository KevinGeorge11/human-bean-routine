package com.example.human_bean_routine;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
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
import android.widget.TextView;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.TimeZone;
import java.util.concurrent.ThreadLocalRandom;

import javax.net.ssl.HttpsURLConnection;

import static java.util.Objects.nonNull;

public class PuzzleActivity extends AppCompatActivity {

    List<PuzzlePiece> pieces = new ArrayList<PuzzlePiece>();
    Button[] overlay = new Button[12];
    Puzzle currentPuzzle;
    EditText editText;
    LayoutInflater inflater;
    PopupWindow modal;

    final static int ROWS = 4;
    final static int COLS = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_puzzle);

        ImageView imageView = findViewById(R.id.ivPuzzle);
        DataBaseHelper db = DataBaseHelper.getDbInstance(this);
        inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);

        currentPuzzle = db.getActivePuzzle();

        // prioritize previous puzzle request and replace current puzzle if not the same
        int puzzleRequest = getIntent().getIntExtra("puzzleID", currentPuzzle.getPuzzleID()); //getCurrentPuzzle();
        if (currentPuzzle.getPuzzleID() != puzzleRequest) {
            currentPuzzle = db.getPuzzleByID(puzzleRequest);
        }

        // replace puzzle image in imageView using the file path
        int resID = getResources().getIdentifier(currentPuzzle.getImagePath(), "drawable", "com.example.human_bean_routine");
        imageView.setImageResource(resID);
        imageView.setVisibility(View.INVISIBLE);

        // get puzzle pieces from the database, and create the 12 puzzle pieces if none exist
        pieces = db.getPuzzlePieces(currentPuzzle.getPuzzleID());
        if(pieces.isEmpty()) {
            int id = currentPuzzle.getPuzzleID();
            for(int r=1; r<ROWS; ++r) {
                for(int c=1; c<COLS; ++c) {
                    PuzzlePiece p = new PuzzlePiece(c, r, 10, id, PuzzlePiece.PieceStatus.LOCKED);
                    db.addPiece(p);
                }
            }
        }

        // set number of tasks until next puzzle piece in circular progress bar
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
    public void onWindowFocusChanged(boolean hasFocus) {
        // create puzzle piece buttons when the window focus changes since imageView's
        // getHeight() and getWidth() require puzzle layout to be fully loaded
        final ConstraintLayout layout = findViewById(R.id.clPuzzleLayout);
        ImageView imageView = findViewById(R.id.ivPuzzle);
        if(nonNull(imageView)) {
            int tileHeight = imageView.getHeight() / ROWS;
            int tileWidth = imageView.getWidth() / COLS;

            for (int i = 0; i < pieces.size(); ++i) {
                final PuzzlePiece p = pieces.get(i);
                final int idx = i;
                int x = pieces.get(i).getxCoord() - 1;
                int y = pieces.get(i).getyCoord() - 1;

                GradientDrawable gd = new GradientDrawable();
                gd.setStroke(1, 0xFFFFFFFF);

                Button button = new Button(this);
                button.setWidth(tileWidth);
                button.setHeight(tileHeight);
                button.setX(imageView.getX() + x * tileWidth);
                button.setY(imageView.getY() + y * tileHeight);

                if (pieces.get(i).getStatus() == PuzzlePiece.PieceStatus.UNLOCKED) {
                    gd.setColor(getResources().getColor(R.color.dark_green));
                    button.setBackground(gd);
                    button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            layout.setClickable(false);
                            onClickPuzzle(p, idx);
                        }
                    });
                } else if (pieces.get(i).getStatus() == PuzzlePiece.PieceStatus.REVEALED) {
                    gd.setColor(Color.TRANSPARENT);
                    button.setBackground(gd);
                    button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            layout.setClickable(false);
                            onClickPuzzle(p, idx);
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

    // the onClick function for each button
    public void onClickPuzzle(PuzzlePiece p, int i) {
        if(pieces.get(i).getStatus() == PuzzlePiece.PieceStatus.UNLOCKED) {
            GradientDrawable gd = new GradientDrawable();
            gd.setColor(Color.TRANSPARENT);
            overlay[i].setBackground(gd);
        }

        if(pieces.get(i).getStatus() != PuzzlePiece.PieceStatus.LOCKED) {
            showPuzzleModal(p);
        }
    }

    // showPuzzleModal populates the modal with information from the db that will hold either
    // old_puzzle_piece_modal or completed_puzzle_piece_modal depending on the type of puzzle
    // piece button clicked
    public void showPuzzleModal(PuzzlePiece p) {
        int layout = (p.getStatus() == PuzzlePiece.PieceStatus.REVEALED) ? R.layout.old_puzzle_piece_modal : R.layout.completed_puzzle_piece_modal;
        View popupView = inflater.inflate(layout, null);

        int width = LinearLayout.LayoutParams.WRAP_CONTENT;
        int height = LinearLayout.LayoutParams.WRAP_CONTENT;
        modal = new PopupWindow(popupView, width, height, true);
        modal.setBackgroundDrawable(null);
        modal.showAtLocation(findViewById(R.id.ivPuzzle), Gravity.CENTER, 0, 0);
        modal.setOutsideTouchable(false);

        if(p.getStatus() == PuzzlePiece.PieceStatus.REVEALED) {
            TextView encouragingMessage = modal.getContentView().findViewById(R.id.tvMessage);
            encouragingMessage.setText(p.getUserMessage());
            TextView reason = modal.getContentView().findViewById(R.id.tvReason);
            reason.setText(p.generateUnlockMessage());
            TextView date = modal.getContentView().findViewById(R.id.tvDateCompleted);
            date.setText(p.getDateUnlocked());
        } else {
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
        DataBaseHelper db = DataBaseHelper.getDbInstance(this);

        // set inputted text as the encouraging message but if user doesn't input a message, choose a random one
        String message = editText.getText().toString();
        if(message.isEmpty()) {
            int min = 1;
            int max = 5;
            int randomNum = ThreadLocalRandom.current().nextInt(min, max + 1);
            String[] array = getResources().getStringArray(R.array.encouraging_messages);
            message = array[randomNum];
        }
        close(view);

        Calendar calendar = Calendar.getInstance();
        String month = new SimpleDateFormat("MMMM").format(calendar.get(Calendar.MONTH));
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int year = calendar.get(Calendar.YEAR);

        piece.setUserMessage(message);
        piece.setStatus(PuzzlePiece.PieceStatus.REVEALED);
        piece.setDateUnlocked(month+" "+day+", "+year);
        db.updatePiece(piece);
    }

    // closes modal, used in both xml file and PuzzleActivity
    public void close(View view) {
        this.modal.dismiss();
    }

    public boolean checkComplete(View v) {
        //check if each puzzle piece is revealed
        for(int i=0; i<pieces.size(); ++i) {
            if(pieces.get(i).getStatus() != PuzzlePiece.PieceStatus.REVEALED) {
                return false;
            }
        }

        // set puzzle as completed in db
        currentPuzzle.setComplete(true);
        DataBaseHelper db = DataBaseHelper.getDbInstance(this);
        db.updatePuzzle(currentPuzzle);

        //refresh activity to load in new image and button overlay
        finish();
        startActivity(getIntent());
        return true;
    }
}
