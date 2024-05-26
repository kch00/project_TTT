package com.example.profiletest2;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class TodoListAdapter extends ArrayAdapter<String> {

    public TodoListAdapter(Context context, List<String> todos) {
        super(context, 0, todos);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(android.R.layout.simple_list_item_1, parent, false);
        }
        TextView tvTodo = convertView.findViewById(android.R.id.text1);
        tvTodo.setText(getItem(position));
        return convertView;
    }
}
