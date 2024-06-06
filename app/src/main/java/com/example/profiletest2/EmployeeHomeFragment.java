package com.example.profiletest2;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

public class EmployeeHomeFragment extends Fragment {

    private LinearLayout noticeLayout;
    private DatabaseHelper databaseHelper;
    private SharedPreferences sharedPreferences;

    @SuppressLint("MissingInflatedId")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_employee_home, container, false);

        noticeLayout = view.findViewById(R.id.noticeLayout);

        databaseHelper = new DatabaseHelper(getContext());
        sharedPreferences = getActivity().getSharedPreferences("LoginPrefs", Context.MODE_PRIVATE);

        loadNoticeItems();

        return view;
    }

    private void loadNoticeItems() {
        Cursor cursor = databaseHelper.getNoticeByCompanyId(sharedPreferences.getString("uniqueId", ""));
        if (cursor != null) {
            int noticeIdIndex = cursor.getColumnIndexOrThrow("notice_id");
            int textIndex = cursor.getColumnIndexOrThrow("text");

            while (cursor.moveToNext()) {
                int noticeId = cursor.getInt(noticeIdIndex);
                String text = cursor.getString(textIndex);
                addNoticeTextView(noticeId, text);
            }
            cursor.close();
        }
    }

    private void addNoticeTextView(int noticeId, String text) {
        TextView textView = new TextView(getContext());
        textView.setText(text);
        textView.setPadding(16, 16, 16, 16);
        textView.setTag(noticeId);
        textView.setTextColor(ContextCompat.getColor(getContext(), android.R.color.black));
        noticeLayout.addView(textView);
    }
}
