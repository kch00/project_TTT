package com.example.profiletest2;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "profiletest.db";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_USERS = "users";
    private static final String COLUMN_USER_ID = "user_id";
    private static final String COLUMN_USERNAME = "username";
    private static final String COLUMN_PASSWORD = "password";
    private static final String COLUMN_COMPANY_NAME = "company_name";
    private static final String COLUMN_UNIQUE_ID = "unique_id";
    private static final String COLUMN_ROLE = "role";

    private static final String TABLE_HANDOVER = "handover";
    private static final String COLUMN_HANDOVER_ID = "handover_id";
    private static final String COLUMN_HANDOVER_USER_ID = "user_id";
    private static final String COLUMN_HANDOVER_TEXT = "text";
    private static final String COLUMN_HANDOVER_ROLE = "role";
    private static final String COLUMN_HANDOVER_COMPANY_ID = "company_id";

    private static final String TABLE_NOTICE = "notice";
    private static final String COLUMN_NOTICE_ID = "notice_id";
    private static final String COLUMN_NOTICE_USER_ID = "user_id";
    private static final String COLUMN_NOTICE_TEXT = "text";
    private static final String COLUMN_NOTICE_ROLE = "role";
    private static final String COLUMN_NOTICE_COMPANY_ID = "company_id";

    private static final String TABLE_TODO = "todo";
    private static final String COLUMN_TODO_ID = "todo_id";
    private static final String COLUMN_TODO_USER_ID = "user_id";
    private static final String COLUMN_TODO_TEXT = "text";
    private static final String COLUMN_TODO_ROLE = "role";
    private static final String COLUMN_TODO_COMPANY_ID = "company_id";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_USERS_TABLE = "CREATE TABLE " + TABLE_USERS + "("
                + COLUMN_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_USERNAME + " TEXT,"
                + COLUMN_PASSWORD + " TEXT,"
                + COLUMN_COMPANY_NAME + " TEXT,"
                + COLUMN_UNIQUE_ID + " TEXT,"
                + COLUMN_ROLE + " TEXT" + ")";
        db.execSQL(CREATE_USERS_TABLE);

        String CREATE_HANDOVER_TABLE = "CREATE TABLE " + TABLE_HANDOVER + "("
                + COLUMN_HANDOVER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_HANDOVER_USER_ID + " INTEGER,"
                + COLUMN_HANDOVER_TEXT + " TEXT,"
                + COLUMN_HANDOVER_ROLE + " TEXT,"
                + COLUMN_HANDOVER_COMPANY_ID + " TEXT" + ")";
        db.execSQL(CREATE_HANDOVER_TABLE);

        String CREATE_NOTICE_TABLE = "CREATE TABLE " + TABLE_NOTICE + "("
                + COLUMN_NOTICE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_NOTICE_USER_ID + " INTEGER,"
                + COLUMN_NOTICE_TEXT + " TEXT,"
                + COLUMN_NOTICE_ROLE + " TEXT,"
                + COLUMN_NOTICE_COMPANY_ID + " TEXT" + ")";
        db.execSQL(CREATE_NOTICE_TABLE);

        String CREATE_TODO_TABLE = "CREATE TABLE " + TABLE_TODO + "("
                + COLUMN_TODO_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_TODO_USER_ID + " INTEGER,"
                + COLUMN_TODO_TEXT + " TEXT,"
                + COLUMN_TODO_ROLE + " TEXT,"
                + COLUMN_TODO_COMPANY_ID + " TEXT" + ")";
        db.execSQL(CREATE_TODO_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_HANDOVER);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NOTICE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TODO);
        onCreate(db);
    }

    public long addUser(String username, String password, String companyName, String uniqueId, String role) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_USERNAME, username);
        values.put(COLUMN_PASSWORD, password);
        values.put(COLUMN_COMPANY_NAME, companyName);
        values.put(COLUMN_UNIQUE_ID, uniqueId);
        values.put(COLUMN_ROLE, role);

        return db.insert(TABLE_USERS, null, values);
    }

    public boolean checkUser(String username, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_USERS + " WHERE " + COLUMN_USERNAME + " = ? AND " + COLUMN_PASSWORD + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{username, password});
        if (cursor != null && cursor.moveToFirst()) {
            cursor.close();
            return true;
        }
        return false;
    }

    public boolean deleteUser(String username) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_USERS, COLUMN_USERNAME + " = ?", new String[]{username}) > 0;
    }

    public long addHandoverItem(int userId, String text, String role) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_HANDOVER_USER_ID, userId);
        values.put(COLUMN_HANDOVER_TEXT, text);
        values.put(COLUMN_HANDOVER_ROLE, role);
        values.put(COLUMN_HANDOVER_COMPANY_ID, getCompanyId(userId));

        return db.insert(TABLE_HANDOVER, null, values);
    }

    public boolean deleteHandoverItem(int handoverId) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_HANDOVER, COLUMN_HANDOVER_ID + " = ?", new String[]{String.valueOf(handoverId)}) > 0;
    }

    public Cursor getHandoverByCompanyId(String companyId) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query(TABLE_HANDOVER, null, COLUMN_HANDOVER_COMPANY_ID + " = ?", new String[]{companyId}, null, null, null);
    }

    public long addNoticeItem(int userId, String text, String role) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NOTICE_USER_ID, userId);
        values.put(COLUMN_NOTICE_TEXT, text);
        values.put(COLUMN_NOTICE_ROLE, role);
        values.put(COLUMN_NOTICE_COMPANY_ID, getCompanyId(userId));

        return db.insert(TABLE_NOTICE, null, values);
    }

    public boolean deleteNoticeItem(int noticeId) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_NOTICE, COLUMN_NOTICE_ID + " = ?", new String[]{String.valueOf(noticeId)}) > 0;
    }

    public Cursor getNoticeByCompanyId(String companyId) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query(TABLE_NOTICE, null, COLUMN_NOTICE_COMPANY_ID + " = ?", new String[]{companyId}, null, null, null);
    }

    public long addTodoItem(int userId, String text, String role) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_TODO_USER_ID, userId);
        values.put(COLUMN_TODO_TEXT, text);
        values.put(COLUMN_TODO_ROLE, role);
        values.put(COLUMN_TODO_COMPANY_ID, getCompanyId(userId));

        return db.insert(TABLE_TODO, null, values);
    }

    public boolean deleteTodoItem(int todoId) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_TODO, COLUMN_TODO_ID + " = ?", new String[]{String.valueOf(todoId)}) > 0;
    }

    public Cursor getTodoByCompanyId(String companyId) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query(TABLE_TODO, null, COLUMN_TODO_COMPANY_ID + " = ?", new String[]{companyId}, null, null, null);
    }

    private String getCompanyId(int userId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_USERS, new String[]{COLUMN_UNIQUE_ID}, COLUMN_USER_ID + " = ?", new String[]{String.valueOf(userId)}, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            String companyId = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_UNIQUE_ID));
            cursor.close();
            return companyId;
        }
        return null;
    }

    public Cursor getAllEmployees() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query(TABLE_USERS, null, COLUMN_ROLE + " = ?", new String[]{"직원"}, null, null, null);
    }
}
