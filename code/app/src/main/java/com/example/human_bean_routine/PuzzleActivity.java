package com.example.human_bean_routine;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
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
import com.squareup.picasso.Picasso;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import static java.util.Objects.nonNull;

public class PuzzleActivity extends AppCompatActivity {

    List<PuzzlePiece> pieces = new ArrayList<>();
    List<Button> overlay = new ArrayList<>();
    Puzzle currentPuzzle;
    EditText editText;
    LayoutInflater inflater;
    PopupWindow modal;
    RequestQueue queue;
    DataBaseHelper db;

    final static int ROWS = 4;
    final static int COLS = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_puzzle);
        queue = APISingleton.getInstance(this.getApplicationContext()).getRequestQueue();

        ImageView imageView = findViewById(R.id.ivPuzzle);
        db = DataBaseHelper.getDbInstance(this);
        inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);

        currentPuzzle = db.getActivePuzzle();
        if(currentPuzzle == null) getNewPuzzle();

        // prioritize previous puzzle request and replace current puzzle if not the same
        int puzzleRequest = getIntent().getIntExtra("puzzleID", currentPuzzle.getPuzzleID()); //getCurrentPuzzle();
        if (currentPuzzle.getPuzzleID() != puzzleRequest) {
            currentPuzzle = db.getPuzzleByID(puzzleRequest);
        }

        // replace puzzle image in imageView using the file path
        imageView.setVisibility(View.INVISIBLE);
        Picasso.get().load(currentPuzzle.getImagePath()).into(imageView);
        // new ImageTask(imageView).execute(currentPuzzle.getImagePath());

        // get puzzle pieces from the database, and create the 12 puzzle pieces if none exist
        pieces = db.getPuzzlePieces(currentPuzzle.getPuzzleID());
        checkUnlockedPieces();

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

    // create puzzle piece buttons when the window focus changes since imageView's
    // getHeight() and getWidth() require puzzle layout to be fully loaded
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        Log.d("PUZZLE ACTIVITY", "WINDOW FOCUS CHANGED");
        final ConstraintLayout layout = findViewById(R.id.clPuzzleLayout);
        ImageView imageView = findViewById(R.id.ivPuzzle);
        // check that puzzle buttons don't exist, imageview is loaded and there
        // are valid puzzle pieces to place on the board
        if(nonNull(imageView) && overlay.isEmpty() && !pieces.isEmpty()) {
            Log.d("PUZZLE ACTIVITY", "IMAGE VIEW IS NOT NULL");
            int tileHeight = imageView.getHeight() / ROWS;
            int tileWidth = imageView.getWidth() / COLS;

            for (int i = 0; i < pieces.size(); ++i) {
                final PuzzlePiece p = pieces.get(i);
                final int idx = i;
                int x = pieces.get(i).getxCoord() - 1;
                int y = pieces.get(i).getyCoord() - 1;

                float screenX = imageView.getX() + x * tileWidth;
                float screenY = imageView.getY() + y * tileHeight;

                Button button = generatePuzzleButton(this, pieces.get(i), tileHeight, tileWidth, screenX, screenY);
                button.setOnClickListener(v -> {
                    layout.setClickable(false);
                    if(pieces.get(idx).getStatus() != PuzzlePiece.PieceStatus.LOCKED) {
                        showPuzzleModal(p);
                    }
                });

                overlay.add(button);
                layout.addView(button);
            }
            imageView.setVisibility(View.VISIBLE);
        }
    }

    // create a button that represents a puzzle piece, all buttons have same size, but colors
    // change depending on PuzzlePiece status
    private static Button generatePuzzleButton(Context context, PuzzlePiece p, float height, float width, float x, float y) {
        Button button = new Button(context);
        button.setWidth((int)width);
        button.setHeight((int)height);
        button.setX(x);
        button.setY(y);
        GradientDrawable gd = new GradientDrawable();
        gd.setStroke(1, 0xFFFFFFFF);

        if(p.getStatus() == PuzzlePiece.PieceStatus.REVEALED) {
            gd.setColor(Color.TRANSPARENT);
        } else if(p.getStatus() == PuzzlePiece.PieceStatus.UNLOCKED) {
            gd.setColor(context.getResources().getColor(R.color.dark_green));
        } else {
            gd.setColor(Color.BLACK);
        }

        button.setBackground(gd);
        return button;
    }

    // showPuzzleModal populates the modal with information from the db that will hold either
    // old_puzzle_piece_modal or completed_puzzle_piece_modal depending on the type of puzzle
    // piece button clicked
    private void showPuzzleModal(PuzzlePiece p) {
        int layout = (p.getStatus() == PuzzlePiece.PieceStatus.REVEALED) ?
                R.layout.old_puzzle_piece_modal :
                R.layout.completed_puzzle_piece_modal;
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
            Button saveButton = modal.getContentView().findViewById(R.id.bSave);
            editText = modal.getContentView().findViewById(R.id.etNote);
            int idx = (p.getxCoord() + (3*(p.getyCoord()-1))) - 1;
            final Button puzzleButton = overlay.get(idx);

            saveButton.setOnClickListener(v -> {
                p.setStatus(PuzzlePiece.PieceStatus.REVEALED);
                GradientDrawable gd = new GradientDrawable();
                gd.setStroke(1, 0xFFFFFFFF);
                gd.setColor(Color.TRANSPARENT);
                puzzleButton.setBackground(gd);
                saveMessage(v, p);
                checkComplete();
            });
        }
    }

    // saveMessage takes the text from the etNote text box and updates the database
    // so that puzzle pieces will have an associated message
    private void saveMessage(View view, PuzzlePiece piece) {
        // set inputted text as the encouraging message but if user doesn't input a message,
        // choose a random one
        String message = editText.getText().toString();
        if(message.isEmpty()) {
            int min = 1;
            int max = 5;
            int randomNum = ThreadLocalRandom.current().nextInt(min, max + 1);
            String[] array = getResources().getStringArray(R.array.encouraging_messages);
            message = array[randomNum];
        }

        // Create current date string
        Calendar calendar = Calendar.getInstance();
        String month = new SimpleDateFormat("MMMM").format(new Date());
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int year = calendar.get(Calendar.YEAR);

        // update puzzle piece
        piece.setUserMessage(message);
        piece.setStatus(PuzzlePiece.PieceStatus.REVEALED);
        piece.setDateUnlocked(month+" "+day+", "+year);
        db.updatePiece(piece);

        close(view);
    }

    // closes modal, used in both xml file and PuzzleActivity
    public void close(View view) {
        this.modal.dismiss();
    }

    // checks whether all PuzzlePieces are REVEALED, will load in new puzzle to replace current one
    private void checkComplete() {
        for(int i=0; i<pieces.size(); ++i) {
            if(pieces.get(i).getStatus() != PuzzlePiece.PieceStatus.REVEALED) {
                return;
            }
        }

        // set puzzle as completed in db
        currentPuzzle.setComplete(true);
        currentPuzzle.setActive(false);
        db.updatePuzzle(currentPuzzle);

        //load new puzzle into db from web api
        getNewPuzzle();

        // WILL BE MOVED TO DATABASE HELPER
        // create new puzzle pieces corresponding to new puzzle to put in db
//        int id = currentPuzzle.getPuzzleID();
//        for(int r=1; r<ROWS+1; ++r) {
//            for(int c=1; c<COLS+1; ++c) {
//                PuzzlePiece piece = new PuzzlePiece(c, r, 1, id+1, PuzzlePiece.PieceStatus.LOCKED);
//                db.addPiece(piece);
//            }
//        }
        //refresh activity to load in new image and button overlay
//        finish();
//        overridePendingTransition( 0, 0);
//        startActivity(getIntent());
//        overridePendingTransition( 0, 0);
    }

    // grab a random photo from Unsplash web api using Volley and
    // create a new Puzzle object that is set to be the new current puzzle
    private void getNewPuzzle() {
        String url = "https://api.unsplash.com/photos/random?client_id=" +
                getString(R.string.unsplash_access_key)+"&topics=6sMVjTLSkeQ";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                response -> {
                    try {
                        // extract description and image path from response
                        JSONObject responseJSON = new JSONObject(response);
                        String description = responseJSON.getString("alt_description");
                        if(description.equals("")) description = "Nature";
                        String imagePath = responseJSON.getJSONObject("urls").getString("regular");

                        // add new puzzle into the database
                        Puzzle p = new Puzzle(description, true, imagePath, false);
                        db.addPuzzle(p);
                        currentPuzzle = db.getActivePuzzle();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }, error -> {
                    Log.d("VOLLEY_ERROR", "Failed to retrieve image from Unsplash");
                    Log.d("VOLLEY_ERROR", String.valueOf(error.networkResponse.statusCode));
                });

        APISingleton.getInstance(this).addToRequestQueue(stringRequest);
    }

    // check if pieces should be unlocked using db counter, then randomly select
    // puzzle pieces that are locked and update accordingly
    private void checkUnlockedPieces() {
        int numUnlocked = 7; //db.getNumberOfUnlockedPieces();

        // get the locked pieces so one can randomly be selected
        List<PuzzlePiece> lockedPieces = new ArrayList<>();
        for(int i=0; i<pieces.size(); ++i) {
            if(pieces.get(i).getStatus() == PuzzlePiece.PieceStatus.LOCKED) {
                lockedPieces.add(pieces.get(i));
            }
        }

        // limit revealing puzzle pieces to current 12 pieces in current puzzle
        while(0<numUnlocked && 0<lockedPieces.size()) {
            int random = new Random().nextInt(lockedPieces.size());
            PuzzlePiece revealPiece = lockedPieces.get(random);
            revealPiece.setStatus(PuzzlePiece.PieceStatus.UNLOCKED);
            db.updatePiece(revealPiece);
            numUnlocked -= 1;//
            lockedPieces.remove(random);
        }

        //db.updateNumberOfUnlockedPieces(numUnlocked);
        pieces = db.getPuzzlePieces(currentPuzzle.getPuzzleID());
    }
}
