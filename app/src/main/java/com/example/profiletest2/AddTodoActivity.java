package com.example.profiletest2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class AddTodoActivity extends AppCompatActivity {

    private EditText etTodo;
    private Button btnSaveTodo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo_add);

        etTodo = findViewById(R.id.etTodo);
        btnSaveTodo = findViewById(R.id.btnSaveTodo);

        btnSaveTodo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String todo = etTodo.getText().toString();
                Intent resultIntent = new Intent();
                resultIntent.putExtra("TODO_FILE", todo);
                setResult(RESULT_OK, resultIntent);
                finish();
            }
        });
    }
}
