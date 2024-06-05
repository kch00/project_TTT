package com.example.profiletest2;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class EmployeeHomeFragment extends Fragment {

    private TextView tvNotice;
    private DatabaseHelper databaseHelper;
    private SharedPreferences sharedPreferences;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_employee_home, container, false);

        tvNotice = view.findViewById(R.id.tvNotice);
        databaseHelper = new DatabaseHelper(getContext());
        sharedPreferences = getActivity().getSharedPreferences("LoginPrefs", Context.MODE_PRIVATE);

        loadNoticeItems(); // Fragment 시작 시 공지사항 불러오기

        return view;
    }

    private void loadNoticeItems() {
        String companyId = sharedPreferences.getString("uniqueId", "");
        Cursor cursor = databaseHelper.getNoticeByCompanyId(companyId);
        if (cursor != null && cursor.moveToFirst()) {
            String text = cursor.getString(cursor.getColumnIndexOrThrow("text"));
            tvNotice.setText(text);
            cursor.close();
        }
    }
}
