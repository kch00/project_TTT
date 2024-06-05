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
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

public class NoticeFragment extends Fragment {

    private EditText etNoticeText;
    private Button btnAddNotice;
    private LinearLayout noticeLayout;
    private ScrollView scrollView;
    private DatabaseHelper databaseHelper;
    private int userId;
    private boolean isOwner;
    private SharedPreferences sharedPreferences;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notice, container, false);

        etNoticeText = view.findViewById(R.id.etNoticeText);
        btnAddNotice = view.findViewById(R.id.btnAddNotice);
        noticeLayout = view.findViewById(R.id.noticeLayout);
        scrollView = view.findViewById(R.id.scrollView);

        databaseHelper = new DatabaseHelper(getContext());
        sharedPreferences = getActivity().getSharedPreferences("LoginPrefs", Context.MODE_PRIVATE);
        userId = sharedPreferences.getInt("userId", -1);
        isOwner = "사장".equals(sharedPreferences.getString("role", "Unknown"));

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
            long result = databaseHelper.addNoticeItem(userId, text, isOwner ? "사장" : "직원");
            if (result != -1) {
                int noticeId = (int) result;
                addNoticeTextView(noticeId, text, isOwner);
                etNoticeText.setText("");
                Toast.makeText(getContext(), "공지사항 항목이 추가되었습니다.", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getContext(), "공지사항 항목 추가에 실패했습니다.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void addNoticeTextView(int noticeId, String text, boolean isOwner) {
        TextView textView = new TextView(getContext());
        textView.setText(text);
        textView.setPadding(16, 16, 16, 16);
        textView.setTag(noticeId);
        if (isOwner) {
            textView.setTextColor(ContextCompat.getColor(getContext(), android.R.color.holo_red_dark));
        } else {
            textView.setTextColor(ContextCompat.getColor(getContext(), android.R.color.black));
        }
        textView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                int id = (int) v.getTag();
                if (databaseHelper.deleteNoticeItem(id)) {
                    noticeLayout.removeView(v);
                    Toast.makeText(getContext(), "공지사항 항목이 삭제되었습니다.", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(), "공지사항 항목 삭제에 실패했습니다.", Toast.LENGTH_SHORT).show();
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
            int roleIndex = cursor.getColumnIndexOrThrow("role");

            while (cursor.moveToNext()) {
                int noticeId = cursor.getInt(noticeIdIndex);
                String text = cursor.getString(textIndex);
                String role = cursor.getString(roleIndex);
                boolean isOwner = "사장".equals(role);
                addNoticeTextView(noticeId, text, isOwner);
            }
            cursor.close();
        }
    }
}
