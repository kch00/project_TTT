package com.example.profiletest2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private Button btnCheckInOut, btnTodoList, btnEditProfile;
    private int userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 현재 사용자의 ID를 가져옵니다
        userId = getIntent().getIntExtra("USER_ID", -1);

        btnCheckInOut = findViewById(R.id.btnCheckInOut);
        btnTodoList = findViewById(R.id.btnTodoList);
        btnEditProfile = findViewById(R.id.btnEditProfile);

        btnCheckInOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, CheckInOutActivity.class);
                intent.putExtra("USER_ID", userId);
                startActivity(intent);
            }
        });

        btnTodoList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, TodoListActivity.class);
                intent.putExtra("USER_ID", userId);
                startActivity(intent);
            }
        });

        btnEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ProfileEditActivity.class);
                intent.putExtra("USER_ID", userId);
                startActivity(intent);
            }
        });
    }
}
