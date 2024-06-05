package com.example.profiletest2;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class HomeFragment extends Fragment {

    private EditText etNotice, etHandover;
    private DatabaseHelper databaseHelper;
    private SharedPreferences sharedPreferences;
    private int userId;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        etNotice = view.findViewById(R.id.etNotice);
        etHandover = view.findViewById(R.id.etHandover);
        Button btnLogout = view.findViewById(R.id.btnLogout);

        databaseHelper = new DatabaseHelper(getContext());
        sharedPreferences = getActivity().getSharedPreferences("LoginPrefs", getContext().MODE_PRIVATE);
        userId = sharedPreferences.getInt("userId", -1);

        // 공지사항 및 인수인계 로드
        loadNoticeAndHandover();

        btnLogout.setOnClickListener(v -> {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.clear();
            editor.apply();
            Toast.makeText(getContext(), "로그아웃 되었습니다.", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(getContext(), LoginActivity.class));
            getActivity().finish();
        });

        return view;
    }

    private void loadNoticeAndHandover() {
        Cursor cursor = databaseHelper.getNoticeAndHandover(userId);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                String notice = cursor.getString(cursor.getColumnIndexOrThrow("notice"));
                String handover = cursor.getString(cursor.getColumnIndexOrThrow("handover"));
                etNotice.setText(notice);
                etHandover.setText(handover);
            }
            cursor.close();
        }
    }
}
