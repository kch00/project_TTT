package com.example.profiletest2;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "CompanyDB";
    private static final int DATABASE_VERSION = 2; // Incremented version number

    // 사용자 테이블
    private static final String TABLE_USERS = "사용자";
    private static final String COLUMN_USER_ID = "사용자_ID";
    private static final String COLUMN_USERNAME = "사용자이름";
    private static final String COLUMN_PASSWORD = "비밀번호";
    private static final String COLUMN_ROLE = "역할";
    private static final String COLUMN_COMPANY_ID = "회사_ID";

    // 출퇴근 기록 테이블
    private static final String TABLE_ATTENDANCE = "출퇴근기록";
    private static final String COLUMN_ATTENDANCE_ID = "출퇴근기록_ID";
    private static final String COLUMN_ATTENDANCE_USER_ID = "사용자_ID";
    private static final String COLUMN_ATTENDANCE_TIME = "시간";
    private static final String COLUMN_ATTENDANCE_TYPE = "유형";

    // TODO 리스트 테이블
    private static final String TABLE_TODO = "할일";
    private static final String COLUMN_TODO_ID = "할일_ID";
    private static final String COLUMN_TODO_USER_ID = "사용자_ID";
    private static final String COLUMN_TODO_TEXT = "내용";
    private static final String COLUMN_TODO_ROLE = "역할";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createUserTable = "CREATE TABLE " + TABLE_USERS + " (" +
                COLUMN_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_USERNAME + " TEXT, " +
                COLUMN_PASSWORD + " TEXT, " +
                COLUMN_ROLE + " TEXT, " +
                COLUMN_COMPANY_ID + " TEXT)";
        db.execSQL(createUserTable);

        String createAttendanceTable = "CREATE TABLE " + TABLE_ATTENDANCE + " (" +
                COLUMN_ATTENDANCE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_ATTENDANCE_USER_ID + " INTEGER, " +
                COLUMN_ATTENDANCE_TIME + " TEXT, " +
                COLUMN_ATTENDANCE_TYPE + " TEXT, " +
                "FOREIGN KEY(" + COLUMN_ATTENDANCE_USER_ID + ") REFERENCES " + TABLE_USERS + "(" + COLUMN_USER_ID + "))";
        db.execSQL(createAttendanceTable);

        String createTodoTable = "CREATE TABLE " + TABLE_TODO + " (" +
                COLUMN_TODO_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_TODO_USER_ID + " INTEGER, " +
                COLUMN_TODO_TEXT + " TEXT, " +
                COLUMN_TODO_ROLE + " TEXT, " +
                "FOREIGN KEY(" + COLUMN_TODO_USER_ID + ") REFERENCES " + TABLE_USERS + "(" + COLUMN_USER_ID + "))";
        db.execSQL(createTodoTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 2) {
            String createTodoTable = "CREATE TABLE " + TABLE_TODO + " (" +
                    COLUMN_TODO_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_TODO_USER_ID + " INTEGER, " +
                    COLUMN_TODO_TEXT + " TEXT, " +
                    COLUMN_TODO_ROLE + " TEXT, " +
                    "FOREIGN KEY(" + COLUMN_TODO_USER_ID + ") REFERENCES " + TABLE_USERS + "(" + COLUMN_USER_ID + "))";
            db.execSQL(createTodoTable);
        }
    }

    // 사용자 추가
    public long addUser(String username, String password, String role, String companyId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_USERNAME, username);
        values.put(COLUMN_PASSWORD, password);
        values.put(COLUMN_ROLE, role);
        values.put(COLUMN_COMPANY_ID, companyId);
        return db.insert(TABLE_USERS, null, values);
    }

    // 출퇴근 기록 추가
    public long addAttendance(int userId, String time, String type) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_ATTENDANCE_USER_ID, userId);
        values.put(COLUMN_ATTENDANCE_TIME, time);
        values.put(COLUMN_ATTENDANCE_TYPE, type);
        return db.insert(TABLE_ATTENDANCE, null, values);
    }

    // TODO 항목 추가
    public long addTodoItem(int userId, String text, String role) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_TODO_USER_ID, userId);
        values.put(COLUMN_TODO_TEXT, text);
        values.put(COLUMN_TODO_ROLE, role);
        return db.insert(TABLE_TODO, null, values);
    }

    // 동일한 회사 ID를 가진 직원의 출퇴근 기록 조회
    public Cursor getAttendanceByCompanyId(String companyId) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT u.username, a.time, a.type FROM " + TABLE_ATTENDANCE + " a " +
                "INNER JOIN " + TABLE_USERS + " u ON a." + COLUMN_ATTENDANCE_USER_ID + " = u." + COLUMN_USER_ID +
                " WHERE u." + COLUMN_COMPANY_ID + " = ?";
        return db.rawQuery(query, new String[]{companyId});
    }

    // 동일한 회사 ID를 가진 직원의 TODO 리스트 조회
    public Cursor getTodoByCompanyId(String companyId) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT u.username, t.text, t.role FROM " + TABLE_TODO + " t " +
                "INNER JOIN " + TABLE_USERS + " u ON t." + COLUMN_TODO_USER_ID + " = u." + COLUMN_USER_ID +
                " WHERE u." + COLUMN_COMPANY_ID + " = ?";
        return db.rawQuery(query, new String[]{companyId});
    }
    // 직원의 모든 프로필을 가져오는 메서드 추가
    public Cursor getAllEmployees() {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_USERS + " WHERE " + COLUMN_ROLE + " = '직원'";
        return db.rawQuery(query, null);
    }
    // TODO 항목 삭제 메서드 추가
    public boolean deleteTodoItem(int todoId) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_TODO, COLUMN_TODO_ID + " = ?", new String[]{String.valueOf(todoId)}) > 0;
    }
    // 계정 삭제 메서드 추가
    public boolean deleteUser(String username) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_USERS, COLUMN_USERNAME + " = ?", new String[]{username}) > 0;
    }

}
