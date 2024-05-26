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

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class TodoListActivity extends AppCompatActivity {

    private TextView tvEmpty;
    private ListView todoListView;
    private ArrayList<String> todoList;
    private TodoListAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo_list);

        tvEmpty = findViewById(R.id.tvEmpty);
        todoListView = findViewById(R.id.todoListView);
        todoList = new ArrayList<>();

        adapter = new TodoListAdapter(this, todoList);
        todoListView.setAdapter(adapter);

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
            String filename = data.getStringExtra("TODO_FILE");
            if (filename != null) {
                StringBuilder todoContent = new StringBuilder();
                try (FileInputStream fis = openFileInput(filename);
                     BufferedReader reader = new BufferedReader(new InputStreamReader(fis))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        todoContent.append(line);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

                todoList.add(todoContent.toString());
                adapter.notifyDataSetChanged();
                updateUI();
            }
        }
    }
}
