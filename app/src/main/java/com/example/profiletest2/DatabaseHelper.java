package com.example.profiletest2;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "profiletest.db";
    private static final int DATABASE_VERSION = 2;

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

    private static final String TABLE_ATTENDANCE = "attendance";
    private static final String COLUMN_ATTENDANCE_ID = "attendance_id";
    private static final String COLUMN_TIME = "time";
    private static final String COLUMN_TYPE = "type";

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

        String CREATE_ATTENDANCE_TABLE = "CREATE TABLE " + TABLE_ATTENDANCE + "("
                + COLUMN_ATTENDANCE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_USER_ID + " INTEGER,"
                + COLUMN_TIME + " TEXT,"
                + COLUMN_TYPE + " TEXT,"
                + COLUMN_COMPANY_ID + " TEXT" + ")";
        db.execSQL(CREATE_ATTENDANCE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_HANDOVER);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NOTICE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TODO);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ATTENDANCE);
        onCreate(db);
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

    // 공지사항 항목 수정 메서드
    // Add or update the following method in your DatabaseHelper class
    public boolean updateNotice(String text, String companyId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_TEXT, text);
        long result = db.update(TABLE_NOTICE, values, COLUMN_COMPANY_ID + " = ?", new String[]{companyId});
        return result != -1;
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

    // 공지사항 저장 메서드
    public boolean saveNotice(String notice, int userId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_TEXT, notice);
        long result = db.update(TABLE_NOTICE, values, COLUMN_USER_ID + " = ?", new String[]{String.valueOf(userId)});
        return result != -1;
    }

    // 공지사항 불러오기 메서드
    public String getNotice(int userId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NOTICE, new String[]{COLUMN_TEXT}, COLUMN_USER_ID + " = ?", new String[]{String.valueOf(userId)}, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            String notice = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TEXT));
            cursor.close();
            return notice;
        }
        return "";
    }

    // 사용자 추가 메서드
    public long addUser(String username, String password, String companyName, String uniqueId, String role, String companyId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_USERNAME, username);
        values.put(COLUMN_PASSWORD, password);
        values.put(COLUMN_COMPANY_NAME, companyName);
        values.put(COLUMN_UNIQUE_ID, uniqueId);
        values.put(COLUMN_ROLE, role);
        values.put(COLUMN_COMPANY_ID, companyId);

        return db.insert(TABLE_USERS, null, values);
    }

    // 사용자 확인 메서드
    public boolean checkUser(String username, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_USERS, null, COLUMN_USERNAME + " = ? AND " + COLUMN_PASSWORD + " = ?",
                new String[]{username, password}, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            cursor.close();
            return true;
        }
        return false;
    }

    // 사용자 정보 가져오는 메서드
    public Cursor getUser(String username, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM users WHERE username = ? AND password = ?";
        return db.rawQuery(query, new String[]{username, password});
    }

    // 모든 직원 정보를 가져오는 메서드 추가
    public Cursor getAllEmployees() {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_USERS + " WHERE " + COLUMN_ROLE + " = '직원'";
        return db.rawQuery(query, null);
    }

    // 출근/퇴근 로그 추가 메서드
    public long addAttendanceLog(int userId, String type) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_USER_ID, userId);
        values.put(COLUMN_TIME, System.currentTimeMillis());
        values.put(COLUMN_TYPE, type);
        values.put(COLUMN_COMPANY_ID, getCompanyId(userId));

        return db.insert(TABLE_ATTENDANCE, null, values);  // 반환 타입을 long으로 변경
    }

    // 회사 ID로 출근/퇴근 로그 가져오기 메서드
    public Cursor getAttendanceByCompanyId(String companyId) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT u.username, a.time, a.type FROM " + TABLE_ATTENDANCE + " a INNER JOIN " + TABLE_USERS + " u ON a." + COLUMN_USER_ID + " = u." + COLUMN_USER_ID + " WHERE u." + COLUMN_COMPANY_ID + " = ?";
        return db.rawQuery(query, new String[]{companyId});
    }

    // 직원 개인의 출근/퇴근 로그 가져오기 메서드
    public Cursor getAttendanceByUserId(int userId) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query(TABLE_ATTENDANCE, null, COLUMN_USER_ID + " = ?", new String[]{String.valueOf(userId)}, null, null, null);
    }

    // 사용자 ID로 사용자 이름 가져오기 메서드
    public String getUsernameById(int userId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_USERS, new String[]{COLUMN_USERNAME}, COLUMN_USER_ID + " = ?", new String[]{String.valueOf(userId)}, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            String username = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_USERNAME));
            cursor.close();
            return username;
        }
        return null;
    }
}
