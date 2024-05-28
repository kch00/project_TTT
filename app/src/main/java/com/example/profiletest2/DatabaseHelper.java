package com.example.profiletest2;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "users.db";
    private static final int DATABASE_VERSION = 1;

    private static final String USER_TABLE = "users";
    private static final String COL_1 = "ID";
    private static final String COL_2 = "USERNAME";
    private static final String COL_3 = "PASSWORD";
    private static final String COL_4 = "ROLE";
    private static final String COL_5 = "UNIQUE_ID";
    private static final String COL_6 = "COMPANY_NAME";

    private static final String CHECKINOUT_TABLE = "checkinout";
    private static final String CHECKINOUT_USER_ID = "USER_ID";
    private static final String CHECKINOUT_LOG = "LOG";

    private static final String PROFILE_TABLE = "profile";
    private static final String PROFILE_USER_ID = "USER_ID";
    private static final String PROFILE_PHOTO_URI = "PHOTO_URI";

    private static final String TODO_TABLE = "todo";
    private static final String TODO_USER_ID = "USER_ID";
    private static final String TODO_LIST = "LIST";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createUserTable = "CREATE TABLE " + USER_TABLE + " (" +
                COL_1 + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_2 + " TEXT, " +
                COL_3 + " TEXT, " +
                COL_4 + " TEXT, " +
                COL_5 + " TEXT, " +
                COL_6 + " TEXT)";

        String createCheckInOutTable = "CREATE TABLE " + CHECKINOUT_TABLE + " (" +
                CHECKINOUT_USER_ID + " INTEGER, " +
                CHECKINOUT_LOG + " TEXT, " +
                "FOREIGN KEY(" + CHECKINOUT_USER_ID + ") REFERENCES " + USER_TABLE + "(ID))";

        String createProfileTable = "CREATE TABLE " + PROFILE_TABLE + " (" +
                PROFILE_USER_ID + " INTEGER, " +
                PROFILE_PHOTO_URI + " TEXT, " +
                "FOREIGN KEY(" + PROFILE_USER_ID + ") REFERENCES " + USER_TABLE + "(ID))";

        String createTodoTable = "CREATE TABLE " + TODO_TABLE + " (" +
                TODO_USER_ID + " INTEGER, " +
                TODO_LIST + " TEXT, " +
                "FOREIGN KEY(" + TODO_USER_ID + ") REFERENCES " + USER_TABLE + "(ID))";

        db.execSQL(createUserTable);
        db.execSQL(createCheckInOutTable);
        db.execSQL(createProfileTable);
        db.execSQL(createTodoTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + USER_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + CHECKINOUT_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + PROFILE_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + TODO_TABLE);
        onCreate(db);
    }

    public boolean insertUser(String username, String password, String role, String uniqueId, String companyName) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_2, username);
        contentValues.put(COL_3, password);
        contentValues.put(COL_4, role);
        contentValues.put(COL_5, uniqueId);
        contentValues.put(COL_6, companyName);
        long result = db.insert(USER_TABLE, null, contentValues);
        return result != -1;
    }

    public boolean checkUser(String username, String password, String role) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + USER_TABLE + " WHERE USERNAME = ? AND PASSWORD = ? AND ROLE = ?", new String[]{username, password, role});
        boolean result = cursor.getCount() > 0;
        cursor.close();
        return result;
    }

    public int getUserId(String username) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT ID FROM " + USER_TABLE + " WHERE USERNAME = ?", new String[]{username});
        if (cursor != null && cursor.moveToFirst()) {
            int userId = cursor.getInt(0);
            cursor.close();
            return userId;
        }
        return -1;
    }

    public String[] getCompanyInfo(int userId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT UNIQUE_ID, COMPANY_NAME FROM " + USER_TABLE + " WHERE ID = ?", new String[]{String.valueOf(userId)});
        if (cursor != null && cursor.moveToFirst()) {
            String uniqueId = cursor.getString(0);
            String companyName = cursor.getString(1);
            cursor.close();
            return new String[]{uniqueId, companyName};
        }
        return new String[]{"", ""};
    }

    public void insertCheckInOutLog(int userId, String log) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(CHECKINOUT_USER_ID, userId);
        contentValues.put(CHECKINOUT_LOG, log);
        db.insert(CHECKINOUT_TABLE, null, contentValues);
    }

    public String getCheckInOutLogs(int userId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT LOG FROM " + CHECKINOUT_TABLE + " WHERE USER_ID = ?", new String[]{String.valueOf(userId)});
        StringBuilder logs = new StringBuilder();
        if (cursor != null && cursor.moveToFirst()) {
            do {
                logs.append(cursor.getString(0)).append("\n");
            } while (cursor.moveToNext());
            cursor.close();
        }
        return logs.toString();
    }

    public boolean insertProfilePhotoUri(int userId, String photoUri) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(PROFILE_USER_ID, userId);
        contentValues.put(PROFILE_PHOTO_URI, photoUri);
        long result = db.insertWithOnConflict(PROFILE_TABLE, null, contentValues, SQLiteDatabase.CONFLICT_REPLACE);
        return result != -1;
    }

    public String getProfilePhotoUri(int userId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT PHOTO_URI FROM " + PROFILE_TABLE + " WHERE USER_ID = ?", new String[]{String.valueOf(userId)});
        if (cursor != null && cursor.moveToFirst()) {
            String photoUri = cursor.getString(0);
            cursor.close();
            return photoUri;
        }
        return null;
    }

    public boolean insertTodoList(int userId, String todoList) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(TODO_USER_ID, userId);
        contentValues.put(TODO_LIST, todoList);
        long result = db.insertWithOnConflict(TODO_TABLE, null, contentValues, SQLiteDatabase.CONFLICT_REPLACE);
        return result != -1;
    }

    public String getTodoList(int userId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT LIST FROM " + TODO_TABLE + " WHERE USER_ID = ?", new String[]{String.valueOf(userId)});
        if (cursor != null && cursor.moveToFirst()) {
            String todoList = cursor.getString(0);
            cursor.close();
            return todoList;
        }
        return null;
    }
}
