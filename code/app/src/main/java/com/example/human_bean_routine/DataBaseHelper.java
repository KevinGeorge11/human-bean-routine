package com.example.human_bean_routine;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.text.method.SingleLineTransformationMethod;
import android.util.Log;

import androidx.annotation.Nullable;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;

// This file was created while referencing:
// "SQLite Database for Android" by freeCodeCamp.org - https://youtu.be/312RhjfetP8
// "Android SQLite Database with Multiple Tables" on androidhive.info - https://www.androidhive.info/2013/09/android-sqlite-database-with-multiple-tables/
// Date Formatting code - https://stackoverflow.com/questions/8654990/how-can-i-get-current-date-in-android

public class DataBaseHelper extends SQLiteOpenHelper {

    private static DataBaseHelper dbInstance;

    // Database Name
    private static final String DATABASE_NAME = "humanBeanRoutine.db";

    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Table Names
    public static final String TASKS_TABLE = "tasks";
    public static final String CATEGORIES_TABLE = "categories";
    public static final String PUZZLES_TABLE = "puzzles";
    public static final String PIECES_TABLE = "pieces";
    public static final String SINGLE_VALUES_TABLE = "single_values";

    // Common column names
    public static final String KEY_ID = "id";
    public static final String KEY_VALUE = "value";
    public static final String KEY_NAME = "name";
    public static final String KEY_COMPLETE = "complete";
    public static final String KEY_ACTIVE = "active";
    public static final String KEY_IMAGE_PATH = "image_path";

    // Column names of TASKS table
    public static final String TASK_DESCRIPTION = "description";
    public static final String TASK_CATEGORY_ID = "category_id";
    public static final String TASK_START_DATE = "start_date";
    public static final String TASK_START_TIME = "start_time";
    public static final String TASK_END_DATE = "end_date";
    public static final String TASK_END_TIME = "end_time";
    public static final String TASK_REPEAT = "repeat";
    public static final String TASK_REMINDER_DATE = "reminder_date";
    public static final String TASK_REMINDER_TIME = "reminder_time";
    public static final String TASK_COMPLETE = "complete";

    // Column names of PIECES table
    public static final String PIECE_X_COORD = "x_coord";
    public static final String PIECE_Y_COORD = "y_coord";
    public static final String PIECE_EDGE_LENGTH = "edge_length";
    public static final String PIECE_PUZZLE_ID = "puzzle_id";
    public static final String PIECE_STATUS = "status";
    public static final String PIECE_DATE_UNLOCKED = "date_unlocked";
    public static final String PIECE_TASKS_COMPLETED = "tasks_completed";
    public static final String PIECE_USER_MESSAGE = "user_message";

    // Values for SINGLE_VALUES table
    public static final String COMPLETED_TASKS =  "COMPLETED_TASKS";
    public static final String UNLOCKED_PIECES =  "unlocked_pieces";
    public static final String LAST_LAUNCH_DATE = "last_launch_date";

    // TASKS table create statement
    private static final String createTaskTableStatement =
            "CREATE TABLE " + TASKS_TABLE + " ( "
                    + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + KEY_NAME + " TEXT, "
                    + TASK_DESCRIPTION + " TEXT, "
                    + TASK_CATEGORY_ID + " INTEGER, "
                    + TASK_START_DATE + " TEXT, "
                    + TASK_START_TIME + " TEXT, "
                    + TASK_END_DATE + " TEXT, "
                    + TASK_END_TIME + " TEXT, "
                    + TASK_REPEAT + " TEXT, "
                    + TASK_REMINDER_DATE + " TEXT, "
                    + TASK_REMINDER_TIME + " TEXT, "
                    + TASK_COMPLETE + " BOOL )";

    // Category table create statement
    private static final String createCategoryTableStatement =
            "CREATE TABLE " + CATEGORIES_TABLE + " ( "
                    + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + KEY_NAME + " TEXT, "
                    + KEY_IMAGE_PATH + " TEXT, "
                    + KEY_ACTIVE + " BOOL )";

    // Puzzle table create statement
    private static final String createPuzzleTableStatement =
            "CREATE TABLE " + PUZZLES_TABLE + " ( "
                    + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + KEY_NAME + " TEXT, "
                    + KEY_ACTIVE + " BOOL, "
                    + KEY_IMAGE_PATH + " TEXT, "
                    + KEY_COMPLETE + " BOOL )";

    // Pieces table create statement
    private static final String createPiecesTableStatement =
            "CREATE TABLE " + PIECES_TABLE + " ( "
                    + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + PIECE_X_COORD + " INTEGER, "
                    + PIECE_Y_COORD + " INTEGER, "
                    + PIECE_EDGE_LENGTH + " INTEGER, "
                    + PIECE_PUZZLE_ID + " INTEGER, "
                    + PIECE_STATUS + " TEXT, "
                    + PIECE_DATE_UNLOCKED + " TEXT, "
                    + PIECE_TASKS_COMPLETED + " INTEGER, "
                    + PIECE_USER_MESSAGE + " TEXT )";

    private static final String createSingleValuesTableStatement =
            "CREATE TABLE " + SINGLE_VALUES_TABLE + " ( "
                    + KEY_ID + " TEXT PRIMARY KEY, "
                    + KEY_VALUE + " TEXT )";


    private DataBaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(createTaskTableStatement);
        db.execSQL(createCategoryTableStatement);
        db.execSQL(createPuzzleTableStatement);
        db.execSQL(createPiecesTableStatement);
        db.execSQL(createSingleValuesTableStatement);

        ContentValues cv = new ContentValues();

        // Temporary coding for default values
        cv.put(KEY_ID, COMPLETED_TASKS);
        cv.put(KEY_VALUE, 0);
        db.insert(SINGLE_VALUES_TABLE, null, cv);

        cv.put(KEY_ID, UNLOCKED_PIECES);
        cv.put(KEY_VALUE, 0);
        db.insert(SINGLE_VALUES_TABLE, null, cv);

        cv.put(KEY_ID, LAST_LAUNCH_DATE);
        Date date = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault());
        String formattedDate = df.format(date);
        cv.put(KEY_VALUE, formattedDate);
        db.insert(SINGLE_VALUES_TABLE, null, cv);

        cv.clear();
        cv.put(KEY_NAME, "lavender field");
        cv.put(KEY_ACTIVE, false);
        cv.put(KEY_IMAGE_PATH, "https://images.unsplash.com/photo-1499002238440-d264edd596ec?crop=entropy&cs=tinysrgb&fit=max&fm=jpg&ixid=MnwyNTExMzd8MHwxfGFsbHx8fHx8fHx8fDE2MjgyODg1NDk&ixlib=rb-1.2.1&q=80&w=1080");
        cv.put(KEY_COMPLETE, true);
        db.insert(PUZZLES_TABLE, null, cv);

        cv.clear();
        cv.put(KEY_NAME, "starry mountain");
        cv.put(KEY_ACTIVE, true);
        cv.put(KEY_IMAGE_PATH, "https://images.unsplash.com/photo-1536431311719-398b6704d4cc?crop=entropy&cs=tinysrgb&fit=max&fm=jpg&ixid=MnwyNTExMzd8MHwxfGFsbHx8fHx8fHx8fDE2MjgyODk1NzM&ixlib=rb-1.2.1&q=80&w=1080");
        cv.put(KEY_COMPLETE, false);
        db.insert(PUZZLES_TABLE, null, cv);

        // all pieces
        for(int r=1; r<=4; ++r){
            for(int c=1; c<=3; ++c){
                cv.clear();
                cv.put(PIECE_X_COORD, c);
                cv.put(PIECE_Y_COORD, r);
                cv.put(PIECE_EDGE_LENGTH,10);
                cv.put(PIECE_PUZZLE_ID, 1);
                cv.put(PIECE_STATUS, String.valueOf(PuzzlePiece.PieceStatus.REVEALED));
                cv.put(PIECE_DATE_UNLOCKED, "July "+r+", 2021");
                cv.put(PIECE_TASKS_COMPLETED, 5);
                cv.put(PIECE_USER_MESSAGE, "Great job me!");
                db.insert(PIECES_TABLE, null, cv);
            }
        }

        cv.clear();
        cv.put(PIECE_X_COORD, 1);
        cv.put(PIECE_Y_COORD, 1);
        cv.put(PIECE_EDGE_LENGTH, 10);
        cv.put(PIECE_PUZZLE_ID, 2);
        cv.put(PIECE_STATUS, String.valueOf(PuzzlePiece.PieceStatus.REVEALED));
        cv.put(PIECE_DATE_UNLOCKED, "August 11");
        cv.put(PIECE_TASKS_COMPLETED, 5);
        cv.put(PIECE_USER_MESSAGE, "Great job me!");
//        db.insert(PIECES_TABLE, null, cv);        Log.d("DataBaseHelper", "Initialized database");
        Log.d("DataBaseHelper", "Added piece " + db.insert(PIECES_TABLE, null, cv));

        cv.clear();
        cv.put(PIECE_X_COORD, 2);
        cv.put(PIECE_Y_COORD, 1);
        cv.put(PIECE_EDGE_LENGTH,10);
        cv.put(PIECE_PUZZLE_ID, 2);
        cv.put(PIECE_STATUS, String.valueOf(PuzzlePiece.PieceStatus.UNLOCKED));
        cv.put(PIECE_DATE_UNLOCKED, "August 11");
        cv.put(PIECE_TASKS_COMPLETED, 5);
        cv.put(PIECE_USER_MESSAGE, "Great job me!");
        db.insert(PIECES_TABLE, null, cv);

        cv.clear();
        cv.put(PIECE_X_COORD, 3);
        cv.put(PIECE_Y_COORD, 1);
        cv.put(PIECE_EDGE_LENGTH,10);
        cv.put(PIECE_PUZZLE_ID, 2);
        cv.put(PIECE_STATUS, String.valueOf(PuzzlePiece.PieceStatus.LOCKED));
        cv.put(PIECE_DATE_UNLOCKED, "August 11");
        cv.put(PIECE_TASKS_COMPLETED, 5);
        cv.put(PIECE_USER_MESSAGE, "Great job me!");
        db.insert(PIECES_TABLE, null, cv);

        for(int r=2; r<=4; ++r){
            for(int c=1; c<=3; ++c){
                cv.clear();
                cv.put(PIECE_X_COORD, c);
                cv.put(PIECE_Y_COORD, r);
                cv.put(PIECE_EDGE_LENGTH,10);
                cv.put(PIECE_PUZZLE_ID, 2);
                cv.put(PIECE_STATUS, String.valueOf(PuzzlePiece.PieceStatus.LOCKED));
                cv.put(PIECE_DATE_UNLOCKED, "August 12");
                cv.put(PIECE_TASKS_COMPLETED, 5);
                cv.put(PIECE_USER_MESSAGE, "Great job me!");
                db.insert(PIECES_TABLE, null, cv);
            }
        }

        Log.d("DataBaseHelper", "Initialized database");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
    }


    public static synchronized DataBaseHelper getDbInstance(Context context) {
        if (dbInstance == null) {
            dbInstance = new DataBaseHelper(context.getApplicationContext());
        }
        return dbInstance;
    }


    public int addTask(Task task) {
        ContentValues cv = new ContentValues();

        cv.put(KEY_NAME, task.getTaskName());
        cv.put(TASK_DESCRIPTION, task.getDescription());
        cv.put(TASK_CATEGORY_ID, task.getCategoryID());
        cv.put(TASK_START_DATE, task.getStartDate());
        cv.put(TASK_START_TIME, task.getStartTime());
        cv.put(TASK_END_DATE, task.getEndDate());
        cv.put(TASK_END_TIME, task.getEndTime());
        cv.put(TASK_REPEAT, task.getRepeat());
        cv.put(TASK_REMINDER_DATE, task.getReminderDate());
        cv.put(TASK_REMINDER_TIME, task.getReminderTime());
        cv.put(TASK_COMPLETE, task.getComplete());

        SQLiteDatabase db = this.getWritableDatabase();
        return (int) db.insert(TASKS_TABLE, null, cv);
    }

    public int deleteAllTasks() {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TASKS_TABLE,"1", null);
    }

    public int updateTask(Task task) {
        ContentValues cv = new ContentValues();

        cv.put(KEY_NAME, task.getTaskName());
        cv.put(TASK_DESCRIPTION, task.getDescription());
        cv.put(TASK_CATEGORY_ID, task.getCategoryID());
        cv.put(TASK_START_DATE, task.getStartDate());
        cv.put(TASK_START_TIME, task.getStartTime());
        cv.put(TASK_END_DATE, task.getEndDate());
        cv.put(TASK_END_TIME, task.getEndTime());
        cv.put(TASK_REPEAT, task.getRepeat());
        cv.put(TASK_REMINDER_DATE, task.getReminderDate());
        cv.put(TASK_REMINDER_TIME, task.getReminderTime());
        cv.put(TASK_COMPLETE, task.getComplete());

        SQLiteDatabase db = this.getWritableDatabase();

        return db.update(TASKS_TABLE, cv, KEY_ID + " = ?",
                new String[] { String.valueOf(task.getTaskId()) });
    }


    public int completeTask(Task task) {
        int update = updateTask(task);
        int tasks = getNumberOfCompletedTasks();
        int unlocked = getNumberOfUnlockedPieces();

        // Check if date changed and reset unlocked pieces
        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault());
        String formattedDate = df.format(c);
        if (!formattedDate.equals(getLastLaunchDate())) {
            if (unlocked > 0) {
                updateNumberOfUnlockedPieces(0);
            }
            updateLastLaunchDate(formattedDate);
        }

        // If task marked as complete
        if (task.getComplete()) {
            if (tasks == 4) {
                updateNumberOfCompletedTasks(0);
                if (unlocked < 3) {
                    updateNumberOfUnlockedPieces(unlocked + 1);
                }
            } else {
                updateNumberOfCompletedTasks(tasks + 1);
            }
        // If task marked as incomplete
        } else {
            if (tasks == 0) {
                updateNumberOfCompletedTasks(4);
                updateNumberOfUnlockedPieces(unlocked - 1);
            } else {
                updateNumberOfCompletedTasks(tasks - 1);
            }
        }
        return update;
    }


    private List<Task> retrieveTasks(String query, String selectionArgs) {
        List<Task> tasks = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String fullQuery = "SELECT * FROM " + TASKS_TABLE + query;

        Cursor cursor = db.rawQuery(fullQuery, null);

        if (cursor.moveToFirst()) {
            do {
                int taskID = cursor.getInt(cursor.getColumnIndex(KEY_ID));
                String name = cursor.getString(cursor.getColumnIndex(KEY_NAME));
                String description = cursor.getString(cursor.getColumnIndex(TASK_DESCRIPTION));
                int categoryID = cursor.getInt(cursor.getColumnIndex(TASK_CATEGORY_ID));
                String startDate  = cursor.getString(cursor.getColumnIndex(TASK_START_DATE));
                String startTime = cursor.getString(cursor.getColumnIndex(TASK_START_TIME));
                String endDate = cursor.getString(cursor.getColumnIndex(TASK_END_DATE));
                String endTime = cursor.getString(cursor.getColumnIndex(TASK_END_TIME));
                String repeat = cursor.getString(cursor.getColumnIndex(TASK_REPEAT));
                String reminderDate = cursor.getString(cursor.getColumnIndex(TASK_REMINDER_DATE));
                String reminderTime = cursor.getString(cursor.getColumnIndex(TASK_REMINDER_TIME));
                Boolean complete = cursor.getInt(cursor.getColumnIndex(TASK_COMPLETE)) == 1;
                String categoryName = getCategoryByID(categoryID).getName();
                Task t = new Task(taskID, name, description, categoryID, startDate, startTime,
                        endDate, endTime, repeat, reminderDate, reminderTime, complete, categoryName);
                tasks.add(t);
            } while (cursor.moveToNext());
        }
        cursor.close();

        return tasks;
    }


    public List<Task> getAllTasks() {
        return retrieveTasks("", "");
    }


    public List<Task> getTasks(int categoryID) {
        List<Task> tasks = new ArrayList<>();
        String query = "SELECT * FROM " + TASKS_TABLE + " WHERE " + "category_id" + " = ?";
        //     List<Task> retrieved = retrieveTasks(query, String.valueOf(taskID));
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, new String[] {String.valueOf(categoryID)});

        if (cursor.moveToFirst()) {
            do {
                int taskID = cursor.getInt(cursor.getColumnIndex(KEY_ID));
                String name = cursor.getString(cursor.getColumnIndex(KEY_NAME));
                String description = cursor.getString(cursor.getColumnIndex(TASK_DESCRIPTION));
                int categoryId = cursor.getInt(cursor.getColumnIndex(TASK_CATEGORY_ID));
                String startDate  = cursor.getString(cursor.getColumnIndex(TASK_START_DATE));
                String startTime = cursor.getString(cursor.getColumnIndex(TASK_START_TIME));
                String endDate = cursor.getString(cursor.getColumnIndex(TASK_END_DATE));
                String endTime = cursor.getString(cursor.getColumnIndex(TASK_END_TIME));
                String repeat = cursor.getString(cursor.getColumnIndex(TASK_REPEAT));
                String reminderDate = cursor.getString(cursor.getColumnIndex(TASK_REMINDER_DATE));
                String reminderTime = cursor.getString(cursor.getColumnIndex(TASK_REMINDER_TIME));
                Boolean complete = cursor.getInt(cursor.getColumnIndex(TASK_COMPLETE)) == 1;
                String categoryName = getCategoryByID(categoryID).getName();
                Task t = new Task(taskID, name, description, categoryId, startDate, startTime,
                        endDate, endTime, repeat, reminderDate, reminderTime, complete, categoryName);
                tasks.add(t);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return tasks;
    }

    public List<Task> getTasksbyDate(int categoryID, String startDate, boolean singleDay) {
        List<Task> tasks = new ArrayList<>();
        String eq = "=";
        if (!singleDay) {
            eq = ">";
        }
        String query = "SELECT * FROM tasks WHERE category_id = ? AND start_date " + eq + " ?";
     //   String query = "SELECT * FROM " + TASKS_TABLE + " WHERE " + "category_id" + " = ?";
        //     List<Task> retrieved = retrieveTasks(query, String.valueOf(taskID));
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, new String[] {String.valueOf(categoryID), String.valueOf(startDate)});

        if (cursor.moveToFirst()) {
            do {
                int taskID = cursor.getInt(cursor.getColumnIndex(KEY_ID));
                String name = cursor.getString(cursor.getColumnIndex(KEY_NAME));
                String description = cursor.getString(cursor.getColumnIndex(TASK_DESCRIPTION));
                int categoryId = cursor.getInt(cursor.getColumnIndex(TASK_CATEGORY_ID));
                String startTime = cursor.getString(cursor.getColumnIndex(TASK_START_TIME));
                String endDate = cursor.getString(cursor.getColumnIndex(TASK_END_DATE));
                String endTime = cursor.getString(cursor.getColumnIndex(TASK_END_TIME));
                String repeat = cursor.getString(cursor.getColumnIndex(TASK_REPEAT));
                String reminderDate = cursor.getString(cursor.getColumnIndex(TASK_REMINDER_DATE));
                String reminderTime = cursor.getString(cursor.getColumnIndex(TASK_REMINDER_TIME));
                Boolean complete = cursor.getInt(cursor.getColumnIndex(TASK_COMPLETE)) == 1;
                String categoryName = getCategoryByID(categoryID).getName();
                Task t = new Task(taskID, name, description, categoryId, startDate, startTime,
                        endDate, endTime, repeat, reminderDate, reminderTime, complete, categoryName);
                tasks.add(t);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return tasks;
    }


    public Task getTaskByID(int taskID) {
        String query = "SELECT * FROM " + TASKS_TABLE + " WHERE " + KEY_ID + " = ?";
        //     List<Task> retrieved = retrieveTasks(query, String.valueOf(taskID));
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, new String[] {String.valueOf(taskID)});

        if (cursor.moveToFirst()) {
            do {
                String name = cursor.getString(cursor.getColumnIndex(KEY_NAME));
                String description = cursor.getString(cursor.getColumnIndex(TASK_DESCRIPTION));
                int categoryID = cursor.getInt(cursor.getColumnIndex(TASK_CATEGORY_ID));
                String startDate = cursor.getString(cursor.getColumnIndex(TASK_START_DATE));
                String startTime = cursor.getString(cursor.getColumnIndex(TASK_START_TIME));
                String endDate = cursor.getString(cursor.getColumnIndex(TASK_END_DATE));
                String endTime = cursor.getString(cursor.getColumnIndex(TASK_END_TIME));
                String repeat = cursor.getString(cursor.getColumnIndex(TASK_REPEAT));
                String reminderDate = cursor.getString(cursor.getColumnIndex(TASK_REMINDER_DATE));
                String reminderTime = cursor.getString(cursor.getColumnIndex(TASK_REMINDER_TIME));
                Boolean complete = cursor.getInt(cursor.getColumnIndex(TASK_COMPLETE)) == 1;
                String categoryName = getCategoryByID(categoryID).getName();
                Task t = new Task(taskID, name, description, categoryID, startDate, startTime,
                        endDate, endTime, repeat, reminderDate, reminderTime, complete, categoryName);
                return t;
            } while (cursor.moveToNext());
        }
        return new Task("error", "error", -1, "", "",
                "", "", "", "", "", false, "");
    }


    public int deleteTask(int taskId) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TASKS_TABLE, KEY_ID + " = ?",
                new String[]{String.valueOf(taskId)});
    }


    public int addCategory(Category category) {
        ContentValues cv = new ContentValues();

        cv.put(KEY_NAME, category.getName());
        cv.put(KEY_IMAGE_PATH, category.getIconPath());
        cv.put(KEY_ACTIVE, category.getActive());

        SQLiteDatabase db = this.getWritableDatabase();
        return (int) db.insert(CATEGORIES_TABLE, null, cv);
    }


    public void setupCategories(List<Category> categories) {
        ContentValues cv = new ContentValues();
        SQLiteDatabase db = this.getWritableDatabase();

        for (Category c : categories) {
            cv.put(KEY_NAME, c.getName());
            cv.put(KEY_IMAGE_PATH, c.getIconPath());
            cv.put(KEY_ACTIVE, c.getActive());

            long insert = db.insert(CATEGORIES_TABLE, null, cv);

            // TODO generate default tasks
        }

        return;
    }


    public int updateCategory(Category category) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(KEY_NAME, category.getName());
        cv.put(KEY_IMAGE_PATH, category.getIconPath());
        cv.put(KEY_ACTIVE, category.getActive());

        // Returns the number of rows affected
        return db.update(TASKS_TABLE, cv, KEY_NAME + " = ?",
                new String[] { String.valueOf(category.getName()) });
    }


    public List<Category> getAllCategories() {
        List<Category> categories = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + CATEGORIES_TABLE;

        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                String name = cursor.getString(cursor.getColumnIndex(KEY_NAME));
                String iconPath = cursor.getString(cursor.getColumnIndex(KEY_IMAGE_PATH));
                Boolean active = cursor.getInt(cursor.getColumnIndex(KEY_ACTIVE)) == 1;

                Category c = new Category(name, iconPath, active);
                categories.add(c);
            } while (cursor.moveToNext());
        }
        cursor.close();

        return categories;
    }

    public List<Category> getActiveCategories() {
        List<Category> categories = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + CATEGORIES_TABLE + " WHERE " + KEY_ACTIVE + " = ?";

        Cursor cursor = db.rawQuery(query, new String [] {"1"});

        if (cursor.moveToFirst()) {
            do {
                String name = cursor.getString(cursor.getColumnIndex(KEY_NAME));
                String iconPath = cursor.getString(cursor.getColumnIndex(KEY_IMAGE_PATH));
                Boolean active = cursor.getInt(cursor.getColumnIndex(KEY_ACTIVE)) == 1;

                Category c = new Category(name, iconPath, active);
                categories.add(c);
            } while (cursor.moveToNext());
        }
        cursor.close();

        return categories;
    }

    public Category getCategoryByID(int categoryID) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + CATEGORIES_TABLE + " WHERE " + KEY_ID + " = ?";

        Cursor cursor = db.rawQuery(query, new String[] {String.valueOf(categoryID)});

        if (cursor.moveToFirst()) {
            int id = cursor.getInt(cursor.getColumnIndex(KEY_ID));
            String name = cursor.getString(cursor.getColumnIndex(KEY_NAME));
            Boolean active = cursor.getInt(cursor.getColumnIndex(KEY_ACTIVE)) == 1;
            String imagePath = cursor.getString(cursor.getColumnIndex(KEY_IMAGE_PATH));

            return new Category(id, name, imagePath, active);
        }
        return new Category(-1, "error", "", false);
    }

    public int getCategoryIdByName(String categoryName) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + CATEGORIES_TABLE + " WHERE " + KEY_NAME + " = ?";

        Cursor cursor = db.rawQuery(query, new String[] {String.valueOf(categoryName)});

        if (cursor.moveToFirst()) {
            String s = cursor.getString(cursor.getColumnIndex(KEY_ID));
            return Integer.parseInt(s);
        }
        return -1;
    }

    public int deleteCategory(String categoryName) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(CATEGORIES_TABLE, KEY_NAME + " = ?",
                new String[]{String.valueOf(categoryName)});
    }


    public int addPuzzle(Puzzle puzzle) {
        ContentValues cv = new ContentValues();

        cv.put(KEY_NAME, puzzle.getName());
        cv.put(KEY_ACTIVE, puzzle.getActive());
        cv.put(KEY_IMAGE_PATH, puzzle.getImagePath());
        cv.put(KEY_COMPLETE, puzzle.getComplete());

        SQLiteDatabase db = this.getWritableDatabase();

        long insert = db.insert(PUZZLES_TABLE, null, cv);

        for (int x = 1; x <= 3; x++) {
            for (int y = 1; y<= 4; y++) {
                cv.clear();
                cv.put(PIECE_X_COORD, x);
                cv.put(PIECE_Y_COORD, y);
                cv.put(PIECE_PUZZLE_ID, (int) insert);
                cv.put(PIECE_STATUS, String.valueOf(PuzzlePiece.PieceStatus.LOCKED));

//                cv.put(PIECE_EDGE_LENGTH,10);
//                cv.put(PIECE_DATE_UNLOCKED, "");
//                cv.put(PIECE_TASKS_COMPLETED, "");
//                cv.put(PIECE_USER_MESSAGE, "");

                db.insert(PIECES_TABLE, null, cv);
            }
        }

        return (int) insert;
    }


    public int updatePuzzle(Puzzle puzzle) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(KEY_ID, puzzle.getPuzzleID());
        cv.put(KEY_NAME, puzzle.getName());
        cv.put(KEY_ACTIVE, puzzle.getActive());
        cv.put(KEY_IMAGE_PATH, puzzle.getImagePath());
        cv.put(KEY_COMPLETE, puzzle.getComplete());

        // Returns the number of rows affected
        return db.update(PUZZLES_TABLE, cv, KEY_ID + " = ?",
                new String[] { String.valueOf(puzzle.getPuzzleID()) });
    }


    public List<Puzzle> getAllPuzzles() {
        List<Puzzle> puzzles = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + PUZZLES_TABLE;

        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                int puzzleID = cursor.getInt(cursor.getColumnIndex(KEY_ID));
                String name = cursor.getString(cursor.getColumnIndex(KEY_NAME));
                Boolean active = cursor.getInt(cursor.getColumnIndex(KEY_ACTIVE)) == 1;
                String imagePath = cursor.getString(cursor.getColumnIndex(KEY_IMAGE_PATH));
                Boolean complete = cursor.getInt(cursor.getColumnIndex(KEY_COMPLETE)) == 1;

                Puzzle p = new Puzzle(puzzleID, name, active, imagePath, complete);
                puzzles.add(p);
            } while (cursor.moveToNext());
        }
        cursor.close();

        return puzzles;
    }


    public List<Puzzle> getCompletedPuzzles() {
        List<Puzzle> puzzles = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + PUZZLES_TABLE + " WHERE " + KEY_COMPLETE + " = 1";

        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                int puzzleID = cursor.getInt(cursor.getColumnIndex(KEY_ID));
                String name = cursor.getString(cursor.getColumnIndex(KEY_NAME));
                Boolean active = cursor.getInt(cursor.getColumnIndex(KEY_ACTIVE)) == 1;
                String imagePath = cursor.getString(cursor.getColumnIndex(KEY_IMAGE_PATH));
                Boolean complete = cursor.getInt(cursor.getColumnIndex(KEY_COMPLETE)) == 1;

                Puzzle p = new Puzzle(puzzleID, name, active, imagePath, complete);
                puzzles.add(p);
            } while (cursor.moveToNext());
        }
        cursor.close();

        return puzzles;
    }


    public Puzzle getPuzzleByID(int puzzleID) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + PUZZLES_TABLE + " WHERE " + KEY_ID + " = ?";

        Cursor cursor = db.rawQuery(query, new String[] {String.valueOf(puzzleID)});

        if (cursor.moveToFirst()) {
            int id = cursor.getInt(cursor.getColumnIndex(KEY_ID));
            String name = cursor.getString(cursor.getColumnIndex(KEY_NAME));
            Boolean active = cursor.getInt(cursor.getColumnIndex(KEY_ACTIVE)) == 1;
            String imagePath = cursor.getString(cursor.getColumnIndex(KEY_IMAGE_PATH));
            Boolean complete = cursor.getInt(cursor.getColumnIndex(KEY_COMPLETE)) == 1;

            return new Puzzle(puzzleID, name, active, imagePath, complete);
        }
        return new Puzzle(-1, "error", false, "", false);
    }


    public Puzzle getActivePuzzle() {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + PUZZLES_TABLE + " WHERE " + KEY_ACTIVE + " = 1";

        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            int puzzleID = cursor.getInt(cursor.getColumnIndex(KEY_ID));
            String name = cursor.getString(cursor.getColumnIndex(KEY_NAME));
            Boolean active = cursor.getInt(cursor.getColumnIndex(KEY_ACTIVE)) == 1;
            String imagePath = cursor.getString(cursor.getColumnIndex(KEY_IMAGE_PATH));
            Boolean complete = cursor.getInt(cursor.getColumnIndex(KEY_COMPLETE)) == 1;

            return new Puzzle(puzzleID, name, active, imagePath, complete);
        }
        return new Puzzle(-1, "error", false, "", false);
    }


    public int deletePuzzle(int puzzleId) {
        SQLiteDatabase db = this.getWritableDatabase();

        List<PuzzlePiece> pieces = getPuzzlePieces(puzzleId);
        for (PuzzlePiece piece : pieces) {
            deletePiece(piece.getPieceID());
        }

        return db.delete(PUZZLES_TABLE, KEY_ID + " = ?",
                new String[]{String.valueOf(puzzleId)});
    }


    public int addPiece(PuzzlePiece piece) {
        ContentValues cv = new ContentValues();

        cv.put(PIECE_X_COORD, piece.getxCoord());
        cv.put(PIECE_Y_COORD, piece.getyCoord());
        cv.put(PIECE_EDGE_LENGTH, piece.getEdgeLength());
        cv.put(PIECE_PUZZLE_ID, piece.getPuzzleID());
        cv.put(PIECE_STATUS, piece.getStatus().name());
        cv.put(PIECE_DATE_UNLOCKED, piece.getDateUnlocked());
        cv.put(PIECE_TASKS_COMPLETED, piece.getTasksCompleted());
        cv.put(PIECE_USER_MESSAGE, piece.getUserMessage());

        SQLiteDatabase db = this.getWritableDatabase();

        return  (int) db.insert(PIECES_TABLE, null, cv);
    }


    public int updatePiece(PuzzlePiece piece) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(KEY_ID, piece.getPieceID());
        cv.put(PIECE_X_COORD, piece.getxCoord());
        cv.put(PIECE_Y_COORD, piece.getyCoord());
        cv.put(PIECE_EDGE_LENGTH,piece.getEdgeLength());
        cv.put(PIECE_PUZZLE_ID, piece.getPuzzleID());
        cv.put(PIECE_STATUS, piece.getStatus().name());
        cv.put(PIECE_DATE_UNLOCKED, piece.getDateUnlocked());
        cv.put(PIECE_TASKS_COMPLETED, piece.getTasksCompleted());
        cv.put(PIECE_USER_MESSAGE, piece.getUserMessage());

        // Returns the number of rows affected
        return db.update(PIECES_TABLE, cv, KEY_ID + " = ?",
                new String[] { String.valueOf(piece.getPieceID()) });
    }


    public List<PuzzlePiece> getPuzzlePieces(int puzzleID) {
        List<PuzzlePiece> pieces = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + PIECES_TABLE + " WHERE " + PIECE_PUZZLE_ID + " = ?";

        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(puzzleID)});

        if (cursor.moveToFirst()) {
            do {
                int pieceID = cursor.getInt(cursor.getColumnIndex(KEY_ID));
                int xCoord = cursor.getInt(cursor.getColumnIndex(PIECE_X_COORD));
                int yCoord = cursor.getInt(cursor.getColumnIndex(PIECE_Y_COORD));
                int edgeLength = cursor.getInt(cursor.getColumnIndex(PIECE_EDGE_LENGTH));
                int puzzID = cursor.getInt(cursor.getColumnIndex(PIECE_PUZZLE_ID));
                PuzzlePiece.PieceStatus status = PuzzlePiece.PieceStatus.valueOf(
                        cursor.getString(cursor.getColumnIndex(PIECE_STATUS)));
                String dateUnlocked = cursor.getString(cursor.getColumnIndex(PIECE_DATE_UNLOCKED));
                int tasksCompleted = cursor.getInt(cursor.getColumnIndex(PIECE_TASKS_COMPLETED));
                String userMessage = cursor.getString(cursor.getColumnIndex(PIECE_USER_MESSAGE));

                PuzzlePiece p = new PuzzlePiece(pieceID, xCoord, yCoord, edgeLength, puzzID,
                        status, dateUnlocked, tasksCompleted, userMessage);
                pieces.add(p);
            } while (cursor.moveToNext());
        }
        cursor.close();

        return pieces;
    }


    public int deletePiece(int pieceId) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(PIECES_TABLE, KEY_ID + " = ?",
                new String[]{String.valueOf(pieceId)});
    }


    public String getSingleValue(String key) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + SINGLE_VALUES_TABLE + " WHERE " + KEY_ID + " = ?";

        Cursor cursor = db.rawQuery(query, new String[] {key});

        String out = "";

        if (cursor.moveToFirst()) {
            out = cursor.getString(cursor.getColumnIndex(KEY_VALUE));
        }
        cursor.close();

        return out;
    }


    public int updateSingleValue(String key, String value) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(KEY_ID, key);
        cv.put(KEY_VALUE, value);
        return db.update(SINGLE_VALUES_TABLE, cv, KEY_ID + " = ?", new String[] { key });

    }


    public int getNumberOfCompletedTasks() {
        return Integer.parseInt(getSingleValue(COMPLETED_TASKS));
    }


    public int updateNumberOfCompletedTasks(int num) {
        return updateSingleValue(COMPLETED_TASKS, String.valueOf(num));
    }


    public int getNumberOfUnlockedPieces() {
        return Integer.parseInt(getSingleValue(UNLOCKED_PIECES));
    }


    public int updateNumberOfUnlockedPieces(int num) {
        return updateSingleValue(UNLOCKED_PIECES, String.valueOf(num));
    }

    public String getLastLaunchDate() {
        return getSingleValue(LAST_LAUNCH_DATE);
    }

    public int updateLastLaunchDate(String date) {
        return updateSingleValue(LAST_LAUNCH_DATE, date);
    }


    public void clearTasksTable() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DROP TABLE IF EXISTS " + TASKS_TABLE);
        db.execSQL(createTaskTableStatement);
    }


    public void clearCategoriesTable() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DROP TABLE IF EXISTS " + CATEGORIES_TABLE);
        db.execSQL(createCategoryTableStatement);
    }


    public void clearPiecesTable() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DROP TABLE IF EXISTS " + PIECES_TABLE);
        db.execSQL(createPiecesTableStatement);
    }


    public void clearPuzzlesTable() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DROP TABLE IF EXISTS " + PUZZLES_TABLE);
        db.execSQL(createPuzzleTableStatement);
    }

    public int getCurrentNumberOfTasks() {
        return getAllTasks().size();
    }
}