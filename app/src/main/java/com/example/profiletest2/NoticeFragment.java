package com.example.profiletest2;

import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

public class NoticeFragment extends Fragment {

    private EditText etNoticeText;
    private Button btnAddNotice;
    private LinearLayout noticeLayout;
    private DatabaseHelper databaseHelper;
    private SharedPreferences sharedPreferences;
    private boolean isOwner;
    private int userId;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notice, container, false);

        etNoticeText = view.findViewById(R.id.etNoticeText);
        btnAddNotice = view.findViewById(R.id.btnAddNotice);
        noticeLayout = view.findViewById(R.id.noticeLayout);

        databaseHelper = new DatabaseHelper(getContext());
        sharedPreferences = getActivity().getSharedPreferences("LoginPrefs", getContext().MODE_PRIVATE);

        String role = sharedPreferences.getString("role", "Unknown");
        String companyId = sharedPreferences.getString("uniqueId", "Unknown");
        userId = sharedPreferences.getInt("userId", -1);
        isOwner = role.equals("사장");

        btnAddNotice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addNoticeItem();
            }
        });

        loadNoticeItems(companyId);

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
                Toast.makeText(getContext(), "공지사항이 추가되었습니다.", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getContext(), "공지사항 추가에 실패했습니다.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void addNoticeTextView(int noticeId, String text, boolean isOwner) {
        TextView textView = new TextView(getContext());
        textView.setText(text);
        textView.setPadding(16, 16, 16, 16);
        textView.setTag(noticeId); // noticeId를 태그로 설정
        if (isOwner) {
            textView.setTextColor(ContextCompat.getColor(getContext(), android.R.color.holo_red_dark));
        } else {
            textView.setTextColor(ContextCompat.getColor(getContext(), android.R.color.black));
        }
        textView.setOnLongClickListener(new View.OnLongClickListener() { // 길게 누르면 삭제
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

    private void loadNoticeItems(String companyId) {
        Cursor cursor = databaseHelper.getNoticeByCompanyId(companyId);
        if (cursor != null) {
            int noticeIdIndex = cursor.getColumnIndexOrThrow("notice_id");
            int usernameIndex = cursor.getColumnIndexOrThrow("username");
            int textIndex = cursor.getColumnIndexOrThrow("text");
            int roleIndex = cursor.getColumnIndexOrThrow("role");

            while (cursor.moveToNext()) {
                int noticeId = cursor.getInt(noticeIdIndex);
                String username = cursor.getString(usernameIndex);
                String text = cursor.getString(textIndex);
                String role = cursor.getString(roleIndex);
                boolean isOwner = role.equals("사장");
                String displayText = username + ": " + text;
                addNoticeTextView(noticeId, displayText, isOwner);
            }
            cursor.close();
        }
    }
}
