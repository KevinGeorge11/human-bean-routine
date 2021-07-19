package com.example.human_bean_routine;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

// This file was created while referencing:
// "SQLite Database for Android" by freeCodeCamp.org - https://youtu.be/312RhjfetP8
// "Android SQLite Database with Multiple Tables" on androidhive.info - https://www.androidhive.info/2013/09/android-sqlite-database-with-multiple-tables/

public class DataBaseHelper extends SQLiteOpenHelper {

    // Database Name
    public static final String DATABASE_NAME = "humanBeanRoutine.db";

    // Table Names
    public static final String TASKS_TABLE = "tasks";
    public static final String CATEGORIES_TABLE = "categories";
    public static final String PUZZLES_TABLE = "puzzles";
    public static final String PIECES_TABLE = "pieces";

    // Common column names
    public static final String KEY_ID = "id";
    public static final String KEY_NAME = "name";

    // Column names of TASKS table
    public static final String TASK_DESCRIPTION = "description";
    public static final String TASK_CATEGORY = "category";
    public static final String TASK_START_DATE = "start_date";
    public static final String TASK_START_TIME = "start_time";
    public static final String TASK_END_DATE = "end_date";
    public static final String TASK_END_TIME = "end_time";
    public static final String TASK_REPEAT = "repeat";
    public static final String TASK_REMINDER_DATE = "reminder_date";
    public static final String TASK_REMINDER_TIME = "reminder_time";
    public static final String TASK_COMPLETE = "complete";

    // Column names of CATEGORIES table
    public static final String CATEGORY_ICON_PATH = "icon_path";
    public static final String CATEGORY_ACTIVE = "active";

    // Column names of PUZZLE table
    public static final String PUZZLE_IMAGE = "image";
    public static final String PUZZLE_ACTIVE = "active";

    // Column names of PIECES table
    public static final String PIECE_STATUS = "status";
    public static final String PIECE_INFO = "info";
    public static final String PIECE_MESSAGE = "message";


    public DataBaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String createTaskTableStatement = "CREATE TABLE " + TASKS_TABLE + " ( "
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + KEY_NAME + " TEXT, "
                + TASK_COMPLETE + " BOOL, "
                + TASK_DESCRIPTION + " TEXT, "
                + TASK_CATEGORY + " TEXT, "
                + TASK_START_DATE + " TEXT, "
                + TASK_START_TIME + " TEXT, "
                + TASK_END_DATE + " TEXT, "
                + TASK_END_TIME + " TEXT )";
        db.execSQL(createTaskTableStatement);

        String createCategoryTableStatement = "CREATE TABLE " + CATEGORIES_TABLE + " ( "
                + KEY_NAME + " TEXT PRIMARY KEY, "
                + CATEGORY_ICON_PATH + " TEXT, "
                + CATEGORY_ACTIVE + " BOOL )";
        db.execSQL(createCategoryTableStatement);

        String createPuzzleTableStatement = "CREATE TABLE " + PUZZLES_TABLE + " ( "
                + KEY_NAME + " TEXT PRIMARY KEY, "
                + PUZZLE_IMAGE + " TEXT, "
                + PUZZLE_ACTIVE + " BOOL )";
        db.execSQL(createPuzzleTableStatement);

        String createPiecesTableStatement = "CREATE TABLE " + PIECES_TABLE + " ( "
                + KEY_ID + " INTEGER PRIMARY KEY, "
                + KEY_NAME + " TEXT , "
                + PIECE_STATUS + " TEXT , "
                + PIECE_INFO + " TEXT , "
                + PIECE_MESSAGE + " TEXT )";
        db.execSQL(createPiecesTableStatement);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
    }

    public boolean addTask(Task task) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        // TODO
        // How are we generating ID?
        // cv.put(KEY_ID, task.getTaskId());

        cv.put(KEY_NAME, task.getName());
        cv.put(TASK_DESCRIPTION, task.getDescription());
        cv.put(TASK_CATEGORY, task.getCategory());
        cv.put(TASK_START_DATE, task.getStartDate());
        cv.put(TASK_START_TIME, task.getStartTime());
        cv.put(TASK_END_DATE, task.getEndDate());
        cv.put(TASK_END_TIME, task.getEndTime());
        cv.put(TASK_REPEAT, task.getRepeat());
        cv.put(TASK_REMINDER_DATE, task.getReminderDate());
        cv.put(TASK_REMINDER_TIME, task.getReminderTime());
        cv.put(TASK_COMPLETE, task.getComplete());

        long insert = db.insert(TASKS_TABLE, null, cv);

        if (insert == -1) { // unsuccessful insertion
            return false;
        }
        return true;
    }

    public int updateTask(Task task) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(KEY_NAME, task.getName());
        cv.put(TASK_DESCRIPTION, task.getDescription());
        cv.put(TASK_CATEGORY, task.getCategory());
        cv.put(TASK_START_DATE, task.getStartDate());
        cv.put(TASK_START_TIME, task.getStartTime());
        cv.put(TASK_END_DATE, task.getEndDate());
        cv.put(TASK_END_TIME, task.getEndTime());
        cv.put(TASK_REPEAT, task.getRepeat());
        cv.put(TASK_REMINDER_DATE, task.getReminderDate());
        cv.put(TASK_REMINDER_TIME, task.getReminderTime());
        cv.put(TASK_COMPLETE, task.getComplete());

        String whereClause = KEY_ID + " = ?";
        int update = db.update(TASKS_TABLE, cv, whereClause, new String[] { String.valueOf(task.getTaskId()) });
        // update() returns the number of rows affected

        return update;
    }

    public List<Task> getAllTasks() {
        List<Task> tasks = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TASKS_TABLE;

        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                int taskID = cursor.getInt(cursor.getColumnIndex(KEY_ID));
                String name = cursor.getString(cursor.getColumnIndex(KEY_NAME));
                String description = cursor.getString(cursor.getColumnIndex(TASK_DESCRIPTION));
                String category = cursor.getString(cursor.getColumnIndex(TASK_CATEGORY));
                String startDate  = cursor.getString(cursor.getColumnIndex(TASK_START_DATE));
                String startTime = cursor.getString(cursor.getColumnIndex(TASK_START_TIME));
                String endDate = cursor.getString(cursor.getColumnIndex(TASK_END_DATE));
                String endTime = cursor.getString(cursor.getColumnIndex(TASK_END_TIME));
                String repeat = cursor.getString(cursor.getColumnIndex(TASK_REPEAT));
                String reminderDate = cursor.getString(cursor.getColumnIndex(TASK_REMINDER_DATE));
                String reminderTime = cursor.getString(cursor.getColumnIndex(TASK_REMINDER_TIME));
                Boolean complete = (cursor.getInt(cursor.getColumnIndex(TASK_COMPLETE)) == 1)? true : false;

                Task t = new Task(name, description, category, startDate, startTime, endDate, endTime, repeat, reminderDate, reminderTime, complete);
                t.setTaskId(taskID);
                tasks.add(t);
            } while (cursor.moveToNext());
        }

        return tasks;
    }

    public int deleteTask(int taskId) {
        SQLiteDatabase db = this.getWritableDatabase();

        int delete = db.delete(TASKS_TABLE, KEY_ID + " = ?", new String[]{String.valueOf(taskId)});

        return delete;
    }


    public boolean addCategory(Category category) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(KEY_NAME, category.getName());
        cv.put(CATEGORY_ICON_PATH, category.getIconPath());
        cv.put(CATEGORY_ACTIVE, category.getActive());

        long insert = db.insert(CATEGORIES_TABLE, null, cv);

        if (insert == -1) { // unsuccessful insertion
            return false;
        }
        return true;
    }

    public List<Category> getAllCategories() {
        List<Category> categories = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + CATEGORIES_TABLE;

        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                String name = cursor.getString(cursor.getColumnIndex(KEY_NAME));
                String iconPath = cursor.getString(cursor.getColumnIndex(CATEGORY_ICON_PATH));
                Boolean active = (cursor.getInt(cursor.getColumnIndex(CATEGORY_ACTIVE)) == 1)? true : false;

                Category c = new Category(name, iconPath, active);
                categories.add(c);
            } while (cursor.moveToNext());
        }

        return categories;
    }

    public int deleteCategory(String categoryName) {
        SQLiteDatabase db = this.getWritableDatabase();

        int delete = db.delete(CATEGORIES_TABLE, KEY_NAME + " = ?", new String[]{String.valueOf(categoryName)});


        return delete;
    }

    public int updateCategory(Category category) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(KEY_NAME, category.getName());
        cv.put(CATEGORY_ICON_PATH, category.getIconPath());
        cv.put(CATEGORY_ACTIVE, category.getActive());

        String whereClause = KEY_NAME + " = ?";
        int update = db.update(TASKS_TABLE, cv, whereClause, new String[] { String.valueOf(category.getName()) });
        // update() returns the number of rows affected

        return update;
    }

}
