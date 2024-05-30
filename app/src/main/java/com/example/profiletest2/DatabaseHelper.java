package com.example.profiletest2;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "CompanyDB";
    private static final int DATABASE_VERSION = 1;

    // 사용자 테이블
    private static final String TABLE_USERS = "users";
    private static final String COLUMN_USER_ID = "user_id";
    private static final String COLUMN_USERNAME = "username";
    private static final String COLUMN_PASSWORD = "password";
    private static final String COLUMN_ROLE = "role";
    private static final String COLUMN_COMPANY_ID = "company_id";

    // 출퇴근 기록 테이블
    private static final String TABLE_ATTENDANCE = "attendance";
    private static final String COLUMN_ATTENDANCE_ID = "attendance_id";
    private static final String COLUMN_ATTENDANCE_USER_ID = "user_id";
    private static final String COLUMN_ATTENDANCE_TIME = "time";
    private static final String COLUMN_ATTENDANCE_TYPE = "type";

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
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ATTENDANCE);
        onCreate(db);
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

    // 동일한 회사 ID를 가진 직원의 출퇴근 기록 조회
    public Cursor getAttendanceByCompanyId(String companyId) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT u.username, a.time, a.type FROM " + TABLE_ATTENDANCE + " a " +
                "INNER JOIN " + TABLE_USERS + " u ON a." + COLUMN_ATTENDANCE_USER_ID + " = u." + COLUMN_USER_ID +
                " WHERE u." + COLUMN_COMPANY_ID + " = ?";
        return db.rawQuery(query, new String[]{companyId});
    }
}
