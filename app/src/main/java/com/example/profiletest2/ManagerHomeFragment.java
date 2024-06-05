package com.example.profiletest2;

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
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class ManagerHomeFragment extends Fragment {

    private EditText etNotice;
    private Button btnSaveNotice;
    private TextView tvNotice;
    private DatabaseHelper databaseHelper;
    private SharedPreferences sharedPreferences;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_manager_home, container, false);

        etNotice = view.findViewById(R.id.etNotice);
        btnSaveNotice = view.findViewById(R.id.btnSaveNotice);
        tvNotice = view.findViewById(R.id.tvNotice);

        databaseHelper = new DatabaseHelper(getContext());
        sharedPreferences = getActivity().getSharedPreferences("LoginPrefs", Context.MODE_PRIVATE);

        btnSaveNotice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveNotice();
                loadNoticeItems(); // Save 후에 공지사항을 다시 불러와서 화면에 표시
            }
        });

        loadNoticeItems(); // Fragment 시작 시 공지사항 불러오기

        return view;
    }

    private void saveNotice() {
        String noticeText = etNotice.getText().toString();
        String companyId = sharedPreferences.getString("uniqueId", "");
        if (!noticeText.isEmpty()) {
            boolean result = databaseHelper.updateNotice(noticeText, companyId);
            if (result) {
                Toast.makeText(getContext(), "공지사항이 저장되었습니다.", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getContext(), "공지사항 저장에 실패했습니다.", Toast.LENGTH_SHORT).show();
            }
        }
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
