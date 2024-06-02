package com.example.profiletest2;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import android.database.Cursor;

public class ViewEmployeesActivity extends AppCompatActivity {

    private LinearLayout employeeProfilesLayout;
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_employees);

        employeeProfilesLayout = findViewById(R.id.employeeProfilesLayout);
        databaseHelper = new DatabaseHelper(this);

        loadEmployeeProfiles();
    }

    private void loadEmployeeProfiles() {
        Cursor cursor = databaseHelper.getAllEmployees();
        if (cursor != null) {
            while (cursor.moveToNext()) {
                @SuppressLint("Range") String name = cursor.getString(cursor.getColumnIndex("username"));
                @SuppressLint("Range") String role = cursor.getString(cursor.getColumnIndex("role"));
                String profileInfo = "이름: " + name + "\n역할: " + role;

                TextView textView = new TextView(this);
                textView.setText(profileInfo);
                textView.setPadding(16, 16, 16, 16);
                employeeProfilesLayout.addView(textView);
            }
            cursor.close();
        }
    }
}
