package com.example.human_bean_routine;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DataBaseHelper extends SQLiteOpenHelper {

    public static final String TASKS = "TASKS";
    public static final String TASK_ID = "ID";
    public static final String TASK_COMPLETE = "COMPLETE";
    public static final String TASK_NAME = "NAME";
    public static final String TASK_DESCRIPTION = "DESCRIPTION";
    public static final String TASK_CATEGORY = "CATEGORY";
    public static final String TASK_START = "START";
    public static final String TASK_END = "END";

    public static final String CATEGORIES = "CATEGORIES";
    public static final String CATEGORY_NAME = "CATEGORY_NAME";
    public static final String CATEGORY_ICON = "CATEGORY_ICON";
    public static final String CATEGORY_ACTIVE = "CATEGORY_ACTIVE";

    public DataBaseHelper(@Nullable Context context) {
        super(context, "hbr.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String createTaskTableStatement = "CREATE TABLE " + TASKS + " ( "
                + TASK_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + TASK_COMPLETE + " BOOL, "
                + TASK_NAME + " TEXT, "
                + TASK_DESCRIPTION + " TEXT, "
                + TASK_CATEGORY + " TEXT, "
                + TASK_START + " DATETIME, "
                + TASK_END + " DATETIME )";
        db.execSQL(createTaskTableStatement);

        String createCategoryTableStatement = "CREATE TABLE " + CATEGORIES + " ( "
                + CATEGORY_NAME + " PRIMARY KEY, "
                + CATEGORY_ICON + " TEXT, "
                + CATEGORY_ACTIVE + " BOOL )";
        db.execSQL(createCategoryTableStatement);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
    }

    public boolean addTask() {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        // TODO
        // cv.put(COLUMN, VALUE);
        // long insert = db.insert(TABLE, null, cv);
        // insert is -1 if failed
        return true;
    }

    public boolean addCategory() {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        // TODO
        // cv.put(COLUMN, VALUE);
        // long insert = db.insert(TABLE, null, cv);
        // insert is -1 if failed
        return true;
    }

}
