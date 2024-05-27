package com.example.profiletest2;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class CheckInOutActivity extends AppCompatActivity {

    private boolean isCheckedIn = false;
    private TextView logTextView;
    private Button checkInOutButton;
    private Button backToMainButton;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_in_out);

        logTextView = findViewById(R.id.logTextView);
        checkInOutButton = findViewById(R.id.checkInOutButton);
        backToMainButton = findViewById(R.id.backToMainButton);

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
}
