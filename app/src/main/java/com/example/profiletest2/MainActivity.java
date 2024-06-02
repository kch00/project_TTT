package com.example.profiletest2;

import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    private TextView userInfoTextView;
    private EditText etNotice, etHandover;
    private LinearLayout attendanceLayout;
    private ScrollView scrollView;
    private SharedPreferences sharedPreferences;
    private boolean isOwner;
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        userInfoTextView = findViewById(R.id.userInfoTextView);
        etNotice = findViewById(R.id.etNotice);
        etHandover = findViewById(R.id.etHandover);
        scrollView = findViewById(R.id.scrollView);
        attendanceLayout = findViewById(R.id.attendanceLayout);
        sharedPreferences = getSharedPreferences("LoginPrefs", MODE_PRIVATE);
        databaseHelper = new DatabaseHelper(this);

        String name = sharedPreferences.getString("name", "Unknown");
        String companyName = sharedPreferences.getString("companyName", "Unknown");
        String role = sharedPreferences.getString("role", "Unknown");
        String companyId = sharedPreferences.getString("uniqueId", "Unknown");
        isOwner = role.equals("사장");

        userInfoTextView.setText(String.format("%s, %s, %s", name, companyName, role));

        etNotice.setText(sharedPreferences.getString("notice", ""));
        etHandover.setText(sharedPreferences.getString("handover", ""));

        if (isOwner) {
            etNotice.setEnabled(true);
            etNotice.setBackgroundColor(Color.WHITE);
            loadAttendanceRecords(companyId);
        }

        etHandover.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (isOwner) {
                    etHandover.setTextColor(Color.RED);
                } else {
                    etHandover.setTextColor(Color.BLACK);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("handover", s.toString());
                editor.apply();
            }
        });

        etNotice.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("notice", s.toString());
                editor.apply();
            }
        });

        BottomNavigationView navView = findViewById(R.id.bottom_navigation);
        navView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment selectedFragment = null;
                switch (item.getItemId()) {
                    case R.id.navigation_home:
                        selectedFragment = new HomeFragment();
                        break;
                    case R.id.navigation_checkinout:
                        selectedFragment = new CheckInOutFragment();
                        break;
                    case R.id.navigation_todo:
                        selectedFragment = new TodoFragment();
                        break;
                    case R.id.navigation_profile_edit:
                        selectedFragment = new ProfileEditFragment();
                        break;
                }
                if (selectedFragment != null) {
                    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.nav_host_fragment, selectedFragment);
                    transaction.commit();
                }
                return true;
            }
        });

        // 기본 프래그먼트 설정
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment, new HomeFragment()).commit();
        }
    }

    private void loadAttendanceRecords(String companyId) {
        Cursor cursor = databaseHelper.getAttendanceByCompanyId(companyId);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                String username = cursor.getString(cursor.getColumnIndex("username"));
                String time = cursor.getString(cursor.getColumnIndex("time"));
                String type = cursor.getString(cursor.getColumnIndex("type"));
                String record = username + " - " + time + " - " + type;

                TextView textView = new TextView(this);
                textView.setText(record);
                textView.setPadding(16, 16, 16, 16);
                attendanceLayout.addView(textView);
            }
            cursor.close();
        }
    }
}
