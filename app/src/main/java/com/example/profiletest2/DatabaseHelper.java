package com.example.profiletest2;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "profiletest.db";
    private static final int DATABASE_VERSION = 1;

    // 테이블 및 컬럼 정의
    private static final String TABLE_HANDOVER = "handover";
    private static final String COLUMN_HANDOVER_ID = "handover_id";
    private static final String COLUMN_USER_ID = "user_id";
    private static final String COLUMN_TEXT = "text";
    private static final String COLUMN_ROLE = "role";
    private static final String COLUMN_COMPANY_ID = "company_id";

    private static final String TABLE_NOTICE = "notice";
    private static final String COLUMN_NOTICE_ID = "notice_id";

    private static final String TABLE_USERS = "users";
    private static final String COLUMN_USERNAME = "username";
    private static final String COLUMN_PASSWORD = "password";
    private static final String COLUMN_UNIQUE_ID = "unique_id";
    private static final String COLUMN_COMPANY_NAME = "company_name";

    private static final String TABLE_TODO = "todo";
    private static final String COLUMN_TODO_ID = "todo_id";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // 테이블 생성 쿼리 작성
        String CREATE_HANDOVER_TABLE = "CREATE TABLE " + TABLE_HANDOVER + "("
                + COLUMN_HANDOVER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_USER_ID + " INTEGER,"
                + COLUMN_TEXT + " TEXT,"
                + COLUMN_ROLE + " TEXT,"
                + COLUMN_COMPANY_ID + " TEXT" + ")";
        db.execSQL(CREATE_HANDOVER_TABLE);

        String CREATE_NOTICE_TABLE = "CREATE TABLE " + TABLE_NOTICE + "("
                + COLUMN_NOTICE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_USER_ID + " INTEGER,"
                + COLUMN_TEXT + " TEXT,"
                + COLUMN_ROLE + " TEXT,"
                + COLUMN_COMPANY_ID + " TEXT" + ")";
        db.execSQL(CREATE_NOTICE_TABLE);

        String CREATE_USERS_TABLE = "CREATE TABLE " + TABLE_USERS + "("
                + COLUMN_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_USERNAME + " TEXT,"
                + COLUMN_PASSWORD + " TEXT,"
                + COLUMN_UNIQUE_ID + " TEXT,"
                + COLUMN_COMPANY_NAME + " TEXT,"
                + COLUMN_ROLE + " TEXT,"
                + COLUMN_COMPANY_ID + " TEXT" + ")";
        db.execSQL(CREATE_USERS_TABLE);

        String CREATE_TODO_TABLE = "CREATE TABLE " + TABLE_TODO + "("
                + COLUMN_TODO_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_USER_ID + " INTEGER,"
                + COLUMN_TEXT + " TEXT,"
                + COLUMN_ROLE + " TEXT,"
                + COLUMN_COMPANY_ID + " TEXT" + ")";
        db.execSQL(CREATE_TODO_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_HANDOVER);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NOTICE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TODO);
        onCreate(db);
    }

    // 인수인계 항목 추가 메서드
    public long addHandoverItem(int userId, String text, String role) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_USER_ID, userId);
        values.put(COLUMN_TEXT, text);
        values.put(COLUMN_ROLE, role);
        values.put(COLUMN_COMPANY_ID, getCompanyId(userId)); // 회사 ID 가져오기

        return db.insert(TABLE_HANDOVER, null, values);
    }

    // 인수인계 항목 삭제 메서드
    public boolean deleteHandoverItem(int handoverId) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_HANDOVER, COLUMN_HANDOVER_ID + " = ?", new String[]{String.valueOf(handoverId)}) > 0;
    }

    // 공지사항 항목 추가 메서드
    public long addNoticeItem(int userId, String text, String role) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_USER_ID, userId);
        values.put(COLUMN_TEXT, text);
        values.put(COLUMN_ROLE, role);
        values.put(COLUMN_COMPANY_ID, getCompanyId(userId)); // 회사 ID 가져오기

        return db.insert(TABLE_NOTICE, null, values);
    }

    // 공지사항 항목 삭제 메서드
    public boolean deleteNoticeItem(int noticeId) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_NOTICE, COLUMN_NOTICE_ID + " = ?", new String[]{String.valueOf(noticeId)}) > 0;
    }

    // 할일 항목 추가 메서드
    public long addTodoItem(int userId, String text, String role) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_USER_ID, userId);
        values.put(COLUMN_TEXT, text);
        values.put(COLUMN_ROLE, role);
        values.put(COLUMN_COMPANY_ID, getCompanyId(userId)); // 회사 ID 가져오기

        return db.insert(TABLE_TODO, null, values);
    }

    // 할일 항목 삭제 메서드
    public boolean deleteTodoItem(int todoId) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_TODO, COLUMN_TODO_ID + " = ?", new String[]{String.valueOf(todoId)}) > 0;
    }

    // 회사 ID로 할일 항목 가져오기 메서드
    public Cursor getTodoByCompanyId(String companyId) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query(TABLE_TODO, null, COLUMN_COMPANY_ID + " = ?", new String[]{companyId}, null, null, null);
    }

    // 회사 ID로 공지사항 항목 가져오기 메서드
    public Cursor getNoticeByCompanyId(String companyId) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query(TABLE_NOTICE, null, COLUMN_COMPANY_ID + " = ?", new String[]{companyId}, null, null, null);
    }

    // 회사 ID로 인수인계 항목 가져오기 메서드
    public Cursor getHandoverByCompanyId(String companyId) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query(TABLE_HANDOVER, null, COLUMN_COMPANY_ID + " = ?", new String[]{companyId}, null, null, null);
    }

    // 회사 ID 가져오기 메서드
    private String getCompanyId(int userId) {
        // 여기에서 userId를 이용하여 회사 ID를 가져오는 로직을 구현합니다.
        // 예시로, users 테이블이 있고 company_id 컬럼이 있다고 가정합니다.
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query("users", new String[]{"company_id"}, "user_id = ?", new String[]{String.valueOf(userId)}, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            String companyId = cursor.getString(cursor.getColumnIndexOrThrow("company_id"));
            cursor.close();
            return companyId;
        }
        return null;
    }

    // 사용자 삭제 메서드
    public boolean deleteUser(String username) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_USERS, COLUMN_USERNAME + " = ?", new String[]{username}) > 0;
    }
    // 모든 직원 정보를 가져오는 메서드 추가
    public Cursor getAllEmployees() {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_USERS + " WHERE " + COLUMN_ROLE + " = '직원'";
        return db.rawQuery(query, null);
    }
    // 공지사항 및 인수인계 로드 메서드 추가
    public Cursor getNoticeAndHandover(int userId) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT notice, handover FROM " + TABLE_USERS + " WHERE " + COLUMN_USER_ID + " = ?";
        return db.rawQuery(query, new String[]{String.valueOf(userId)});
    }


}
