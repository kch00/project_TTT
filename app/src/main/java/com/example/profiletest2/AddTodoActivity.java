package com.example.profiletest2;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;

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
                String filename = UUID.randomUUID().toString() + ".txt";

                try (FileOutputStream fos = openFileOutput(filename, Context.MODE_PRIVATE)) {
                    fos.write(todo.getBytes());
                } catch (IOException e) {
                    e.printStackTrace();
                }

                Intent resultIntent = new Intent();
                resultIntent.putExtra("TODO_FILE", filename);
                setResult(RESULT_OK, resultIntent);
                finish();
            }
        });
    }
}


