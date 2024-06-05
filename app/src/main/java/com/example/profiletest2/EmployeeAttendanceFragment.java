package com.example.profiletest2;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class EmployeeAttendanceFragment extends Fragment {

    private LinearLayout attendanceLogLayout;
    private Button btnCheckInOut;
    private boolean isCheckedIn = false;
    private SharedPreferences sharedPreferences;
    private DatabaseHelper databaseHelper;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_employee_attendance, container, false);

        attendanceLogLayout = view.findViewById(R.id.attendanceLogLayout);
        btnCheckInOut = view.findViewById(R.id.btnCheckInOut);
        sharedPreferences = getActivity().getSharedPreferences("LoginPrefs", Context.MODE_PRIVATE);
        databaseHelper = new DatabaseHelper(getContext());

        loadAttendanceLogs();

        btnCheckInOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleCheckInOut();
            }
        });

        return view;
    }

    private void handleCheckInOut() {
        int userId = sharedPreferences.getInt("userId", -1);
        String type = isCheckedIn ? "퇴근" : "출근";
        long result = databaseHelper.addAttendanceLog(userId, type);
        if (result != -1) {
            isCheckedIn = !isCheckedIn;
            btnCheckInOut.setText(isCheckedIn ? "퇴근" : "출근");
            addAttendanceLog(sharedPreferences.getString("username", "Unknown"), String.valueOf(System.currentTimeMillis()), type);
        }
    }

    private void loadAttendanceLogs() {
        int userId = sharedPreferences.getInt("userId", -1);
        Cursor cursor = databaseHelper.getAttendanceByUserId(userId);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                String time = cursor.getString(cursor.getColumnIndexOrThrow("time"));
                String type = cursor.getString(cursor.getColumnIndexOrThrow("type"));
                addAttendanceLog(sharedPreferences.getString("username", "Unknown"), time, type);
            }
            cursor.close();
        }
    }

    private void addAttendanceLog(String username, String time, String type) {
        TextView textView = new TextView(getContext());
        textView.setText(username + ": " + type + " at " + time);
        attendanceLogLayout.addView(textView);
    }
}
