package com.example.profiletest2;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    private EditText etUsername, etPassword;
    private Button btnLogin, btnRegister;
    private DatabaseHelper databaseHelper;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        btnRegister = findViewById(R.id.btnRegister);

        databaseHelper = new DatabaseHelper(this);
        sharedPreferences = getSharedPreferences("LoginPrefs", MODE_PRIVATE);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = etUsername.getText().toString();
                String password = etPassword.getText().toString();

                if (databaseHelper.checkUser(username, password)) {
                    Cursor cursor = databaseHelper.getUser(username, password);
                    if (cursor != null && cursor.moveToFirst()) {
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("username", cursor.getString(cursor.getColumnIndexOrThrow("username")));
                        editor.putString("companyName", cursor.getString(cursor.getColumnIndexOrThrow("company_name")));
                        editor.putString("role", cursor.getString(cursor.getColumnIndexOrThrow("role")));
                        editor.putInt("userId", cursor.getInt(cursor.getColumnIndexOrThrow("user_id")));
                        editor.putString("uniqueId", cursor.getString(cursor.getColumnIndexOrThrow("unique_id")));
                        editor.apply();
                        cursor.close();
                    }

                    Toast.makeText(LoginActivity.this, "로그인 성공", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish(); // 로그인 후 로그인 액티비티를 종료
                } else {
                    Toast.makeText(LoginActivity.this, "잘못된 사용자 이름 또는 비밀번호입니다.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
    }
}
