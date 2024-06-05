package com.example.profiletest2;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class ManagerAttendanceFragment extends Fragment {

    private LinearLayout attendanceLogLayout;
    private SharedPreferences sharedPreferences;
    private DatabaseHelper databaseHelper;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_manager_attendance, container, false);

        attendanceLogLayout = view.findViewById(R.id.attendanceLogLayout);
        sharedPreferences = getActivity().getSharedPreferences("LoginPrefs", Context.MODE_PRIVATE);
        databaseHelper = new DatabaseHelper(getContext());

        loadAttendanceLogs();

        return view;
    }

    private void loadAttendanceLogs() {
        String companyId = sharedPreferences.getString("uniqueId", "");
        Cursor cursor = databaseHelper.getAttendanceByCompanyId(companyId);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                String username = cursor.getString(cursor.getColumnIndexOrThrow("username"));
                String time = cursor.getString(cursor.getColumnIndexOrThrow("time"));
                String type = cursor.getString(cursor.getColumnIndexOrThrow("type"));
                addAttendanceLog(username, time, type);
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
