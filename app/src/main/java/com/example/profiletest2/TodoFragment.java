package com.example.profiletest2;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import com.example.profiletest2.DatabaseHelper;
import com.example.profiletest2.R;

public class TodoFragment extends Fragment {

    private EditText etTodoText;
    private Button btnAddTodo;
    private LinearLayout todoLayout;
    private ScrollView scrollView;
    private DatabaseHelper databaseHelper;
    private SharedPreferences sharedPreferences;
    private boolean isOwner;
    private int userId;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_todo, container, false);

        etTodoText = view.findViewById(R.id.etTodoText);
        btnAddTodo = view.findViewById(R.id.btnAddTodo);
        todoLayout = view.findViewById(R.id.todoLayout);
        scrollView = view.findViewById(R.id.scrollView);

        databaseHelper = new DatabaseHelper(getContext());
        sharedPreferences = getActivity().getSharedPreferences("LoginPrefs", getContext().MODE_PRIVATE);

        String role = sharedPreferences.getString("role", "Unknown");
        String companyId = sharedPreferences.getString("uniqueId", "Unknown");
        userId = sharedPreferences.getInt("userId", -1);
        isOwner = role.equals("사장");

        btnAddTodo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addTodoItem();
            }
        });

        loadTodoItems(companyId);

        return view;
    }

    private void addTodoItem() {
        String text = etTodoText.getText().toString();
        if (!text.isEmpty()) {
            long result = databaseHelper.addTodoItem(userId, text, isOwner ? "사장" : "직원");
            if (result != -1) {
                int todoId = (int) result;
                addTodoTextView(todoId, text, isOwner);
                etTodoText.setText("");
                Toast.makeText(getContext(), "할일 항목이 추가되었습니다.", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getContext(), "할일 항목 추가에 실패했습니다.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void addTodoTextView(int todoId, String text, boolean isOwner) {
        TextView textView = new TextView(getContext());
        textView.setText(text);
        textView.setPadding(16, 16, 16, 16);
        textView.setTag(todoId); // todoId를 태그로 설정
        if (isOwner) {
            textView.setTextColor(ContextCompat.getColor(getContext(), android.R.color.holo_red_dark));
        } else {
            textView.setTextColor(ContextCompat.getColor(getContext(), android.R.color.black));
        }
        textView.setOnLongClickListener(new View.OnLongClickListener() { // 길게 누르면 삭제
            @Override
            public boolean onLongClick(View v) {
                int id = (int) v.getTag();
                if (databaseHelper.deleteTodoItem(id)) {
                    todoLayout.removeView(v);
                    Toast.makeText(getContext(), "할일 항목이 삭제되었습니다.", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(), "할일 항목 삭제에 실패했습니다.", Toast.LENGTH_SHORT).show();
                }
                return true;
            }
        });
        todoLayout.addView(textView);
    }

    private void loadTodoItems(String companyId) {
        Cursor cursor = databaseHelper.getTodoByCompanyId(companyId);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                @SuppressLint("Range") int todoId = cursor.getInt(cursor.getColumnIndex("todo_id"));
                @SuppressLint("Range") String username = cursor.getString(cursor.getColumnIndex("username"));
                @SuppressLint("Range") String text = cursor.getString(cursor.getColumnIndex("text"));
                @SuppressLint("Range") String role = cursor.getString(cursor.getColumnIndex("role"));
                boolean isOwner = role.equals("사장");
                String displayText = username + ": " + text;
                addTodoTextView(todoId, displayText, isOwner);
            }
            cursor.close();
        }
    }
}
