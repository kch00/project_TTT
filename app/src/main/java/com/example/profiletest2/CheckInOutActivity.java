package com.example.profiletest2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class CheckInOutActivity extends AppCompatActivity {

    private boolean isCheckedIn = false;
    private TextView logTextView;
    private Button checkInOutButton;
    private ImageButton backToMainButton;
    private DatabaseHelper db;
    private int userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_in_out);

        db = new DatabaseHelper(this);

        // 현재 사용자의 ID를 가져옵니다
        userId = getIntent().getIntExtra("USER_ID", -1);

        logTextView = findViewById(R.id.logTextView);
        checkInOutButton = findViewById(R.id.checkInOutButton);
        backToMainButton = findViewById(R.id.backToMainButton);

        // 로드된 기록을 설정합니다
        loadLog();

        checkInOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isCheckedIn) {
                    checkInOutButton.setText("출근");
                    addLog("퇴근: ");
                } else {
                    checkInOutButton.setText("퇴근");
                    addLog("출근: ");
                }
                isCheckedIn = !isCheckedIn;
                saveLog(); // 출퇴근 상태와 로그를 저장합니다
            }
        });

        backToMainButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish(); // 현재 액티비티를 종료하여 메인 화면으로 돌아갑니다.
            }
        });
    }

    private void addLog(String prefix) {
        String currentTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.KOREA).format(new Date());
        String newLog = prefix + currentTime;
        String previousLogs = logTextView.getText().toString();
        logTextView.setText(previousLogs + "\n" + newLog);
    }

    private void saveLog() {
        db.insertCheckInOutLog(userId, logTextView.getText().toString());
    }

    private void loadLog() {
        String logs = db.getCheckInOutLogs(userId);
        logTextView.setText(logs);
    }
}
