package com.example.profiletest2;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import android.content.Context;
import android.content.SharedPreferences;

public class TodoFragment extends Fragment {

    private ListView todoListView;
    private ImageButton btnAddTodo;
    private TextView tvEmpty;
    private ArrayList<String> todoList;
    private ArrayAdapter<String> adapter;
    private SharedPreferences sharedPreferences;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_todo, container, false);

        todoListView = view.findViewById(R.id.todoListView);
        btnAddTodo = view.findViewById(R.id.btnAddTodo);
        tvEmpty = view.findViewById(R.id.tvEmpty);

        sharedPreferences = getActivity().getSharedPreferences("TodoPrefs", Context.MODE_PRIVATE);
        loadTodos();

        adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, todoList);
        todoListView.setAdapter(adapter);
        updateEmptyView();

        btnAddTodo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddTodoDialog();
            }
        });

        return view;
    }

    private void showAddTodoDialog() {
        final EditText input = new EditText(getActivity());
        new android.app.AlertDialog.Builder(getActivity())
                .setTitle("TODO 추가")
                .setView(input)
                .setPositiveButton("추가", (dialog, which) -> {
                    String todo = input.getText().toString();
                    if (!todo.isEmpty()) {
                        todoList.add(todo);
                        adapter.notifyDataSetChanged();
                        saveTodos();
                        updateEmptyView();
                    }
                })
                .setNegativeButton("취소", null)
                .show();
    }

    private void updateEmptyView() {
        if (todoList.isEmpty()) {
            tvEmpty.setVisibility(View.VISIBLE);
        } else {
            tvEmpty.setVisibility(View.GONE);
        }
    }

    private void saveTodos() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Set<String> todoSet = new HashSet<>(todoList);
        editor.putStringSet("todos", todoSet);
        editor.apply();
    }

    private void loadTodos() {
        Set<String> todoSet = sharedPreferences.getStringSet("todos", new HashSet<>());
        todoList = new ArrayList<>(todoSet);
    }
}
