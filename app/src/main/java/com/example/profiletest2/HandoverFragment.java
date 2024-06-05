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

public class HandoverFragment extends Fragment {

    private EditText etHandoverText;
    private Button btnAddHandover;
    private LinearLayout handoverLayout;
    private DatabaseHelper databaseHelper;
    private SharedPreferences sharedPreferences;
    private boolean isOwner;
    private int userId;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_handover, container, false);

        etHandoverText = view.findViewById(R.id.etHandoverText);
        btnAddHandover = view.findViewById(R.id.btnAddHandover);
        handoverLayout = view.findViewById(R.id.handoverLayout);

        databaseHelper = new DatabaseHelper(getContext());
        sharedPreferences = getActivity().getSharedPreferences("LoginPrefs", getContext().MODE_PRIVATE);

        String role = sharedPreferences.getString("role", "Unknown");
        String companyId = sharedPreferences.getString("uniqueId", "Unknown");
        userId = sharedPreferences.getInt("userId", -1);
        isOwner = role.equals("사장");

        btnAddHandover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addHandoverItem();
            }
        });

        loadHandoverItems(companyId);

        return view;
    }

    private void addHandoverItem() {
        String text = etHandoverText.getText().toString();
        if (!text.isEmpty()) {
            long result = databaseHelper.addHandoverItem(userId, text, isOwner ? "사장" : "직원");
            if (result != -1) {
                int handoverId = (int) result;
                addHandoverTextView(handoverId, text, isOwner);
                etHandoverText.setText("");
                Toast.makeText(getContext(), "인수인계 항목이 추가되었습니다.", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getContext(), "인수인계 항목 추가에 실패했습니다.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void addHandoverTextView(int handoverId, String text, boolean isOwner) {
        TextView textView = new TextView(getContext());
        textView.setText(text);
        textView.setPadding(16, 16, 16, 16);
        textView.setTag(handoverId); // handoverId를 태그로 설정
        if (isOwner) {
            textView.setTextColor(ContextCompat.getColor(getContext(), android.R.color.holo_red_dark));
        } else {
            textView.setTextColor(ContextCompat.getColor(getContext(), android.R.color.black));
        }
        textView.setOnLongClickListener(new View.OnLongClickListener() { // 길게 누르면 삭제
            @Override
            public boolean onLongClick(View v) {
                int id = (int) v.getTag();
                if (databaseHelper.deleteHandoverItem(id)) {
                    handoverLayout.removeView(v);
                    Toast.makeText(getContext(), "인수인계 항목이 삭제되었습니다.", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(), "인수인계 항목 삭제에 실패했습니다.", Toast.LENGTH_SHORT).show();
                }
                return true;
            }
        });
        handoverLayout.addView(textView);
    }

    private void loadHandoverItems(String companyId) {
        Cursor cursor = databaseHelper.getHandoverByCompanyId(companyId);
        if (cursor != null) {
            int handoverIdIndex = cursor.getColumnIndexOrThrow("handover_id");
            int usernameIndex = cursor.getColumnIndexOrThrow("username");
            int textIndex = cursor.getColumnIndexOrThrow("text");
            int roleIndex = cursor.getColumnIndexOrThrow("role");

            while (cursor.moveToNext()) {
                int handoverId = cursor.getInt(handoverIdIndex);
                String username = cursor.getString(usernameIndex);
                String text = cursor.getString(textIndex);
                String role = cursor.getString(roleIndex);
                boolean isOwner = role.equals("사장");
                String displayText = username + ": " + text;
                addHandoverTextView(handoverId, displayText, isOwner);
            }
            cursor.close();
        }
    }
}
