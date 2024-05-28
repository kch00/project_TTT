package com.example.profiletest2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

public class TodoListActivity extends AppCompatActivity {

    private TextView tvEmpty;
    private ListView todoListView;
    private ArrayList<String> todoList;
    private TodoListAdapter adapter;
    private DatabaseHelper db;
    private int userId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo_list);

        db = new DatabaseHelper(this);

        // 현재 사용자의 ID를 가져옵니다
        userId = getIntent().getIntExtra("USER_ID", -1);

        tvEmpty = findViewById(R.id.tvEmpty);
        todoListView = findViewById(R.id.todoListView);
        todoList = new ArrayList<>();

        adapter = new TodoListAdapter(this, todoList);
        todoListView.setAdapter(adapter);

        // TODO 리스트를 로드합니다
        loadTodoList();

        ImageButton btnAddTodo = findViewById(R.id.btnAddTodo);
        btnAddTodo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TodoListActivity.this, AddTodoActivity.class);
                startActivityForResult(intent, 1);
            }
        });

        Button btnBackToMain = findViewById(R.id.btnBackToMain);
        btnBackToMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        updateUI();
    }

    private void updateUI() {
        if (todoList.isEmpty()) {
            tvEmpty.setVisibility(View.VISIBLE);
            todoListView.setVisibility(View.GONE);
        } else {
            tvEmpty.setVisibility(View.GONE);
            todoListView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && data != null) {
            String newTodo = data.getStringExtra("TODO_ITEM");
            if (newTodo != null) {
                todoList.add(newTodo);
                adapter.notifyDataSetChanged();
                updateUI();

                // TODO 리스트를 저장합니다
                saveTodoList();
            }
        }
    }

    private void saveTodoList() {
        JSONArray jsonArray = new JSONArray();
        for (String todo : todoList) {
            jsonArray.put(todo);
        }
        db.insertTodoList(userId, jsonArray.toString());
    }

    private void loadTodoList() {
        String todoListJson = db.getTodoList(userId);
        if (todoListJson != null) {
            try {
                JSONArray jsonArray = new JSONArray(todoListJson);
                for (int i = 0; i < jsonArray.length(); i++) {
                    todoList.add(jsonArray.getString(i));
                }
                adapter.notifyDataSetChanged();
                updateUI();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
