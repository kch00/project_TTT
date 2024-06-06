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
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

public class ManagerHomeFragment extends Fragment {

    private EditText etNoticeText;
    private Button btnAddNotice;
    private LinearLayout noticeLayout;
    private DatabaseHelper databaseHelper;
    private int userId;
    private SharedPreferences sharedPreferences;

    @SuppressLint("MissingInflatedId")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_manager_home, container, false);

        etNoticeText = view.findViewById(R.id.etNoticeText);
        btnAddNotice = view.findViewById(R.id.btnAddNotice);
        noticeLayout = view.findViewById(R.id.noticeLayout);

        databaseHelper = new DatabaseHelper(getContext());
        sharedPreferences = getActivity().getSharedPreferences("LoginPrefs", Context.MODE_PRIVATE);
        userId = sharedPreferences.getInt("userId", -1);

        btnAddNotice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addNoticeItem();
            }
        });

        loadNoticeItems();

        return view;
    }

    private void addNoticeItem() {
        String text = etNoticeText.getText().toString();
        if (!text.isEmpty()) {
            long result = databaseHelper.addNoticeItem(userId, text, "사장");
            if (result != -1) {
                int noticeId = (int) result;
                addNoticeTextView(noticeId, text, "사장");
                etNoticeText.setText("");
                Toast.makeText(getContext(), "공지사항이 추가되었습니다.", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getContext(), "공지사항 추가에 실패했습니다.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void addNoticeTextView(int noticeId, String text, String role) {
        TextView textView = new TextView(getContext());
        textView.setText(text);
        textView.setPadding(16, 16, 16, 16);
        textView.setTag(noticeId);
        textView.setTextColor(ContextCompat.getColor(getContext(), android.R.color.black));
        textView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                int id = (int) v.getTag();
                if (databaseHelper.deleteNoticeItem(id)) {
                    noticeLayout.removeView(v);
                    Toast.makeText(getContext(), "공지사항이 삭제되었습니다.", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(), "공지사항 삭제에 실패했습니다.", Toast.LENGTH_SHORT).show();
                }
                return true;
            }
        });
        noticeLayout.addView(textView);
    }

    private void loadNoticeItems() {
        Cursor cursor = databaseHelper.getNoticeByCompanyId(sharedPreferences.getString("uniqueId", ""));
        if (cursor != null) {
            int noticeIdIndex = cursor.getColumnIndexOrThrow("notice_id");
            int textIndex = cursor.getColumnIndexOrThrow("text");

            while (cursor.moveToNext()) {
                int noticeId = cursor.getInt(noticeIdIndex);
                String text = cursor.getString(textIndex);
                addNoticeTextView(noticeId, text, "사장");
            }
            cursor.close();
        }
    }
}
