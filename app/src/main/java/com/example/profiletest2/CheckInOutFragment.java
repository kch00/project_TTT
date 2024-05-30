package com.example.profiletest2;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import android.content.Context;
import android.content.SharedPreferences;

public class CheckInOutFragment extends Fragment {

    private TextView logTextView;
    private Button checkInOutButton;
    private boolean isCheckedIn = false;
    private SharedPreferences sharedPreferences;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_checkinout, container, false);

        logTextView = view.findViewById(R.id.logTextView);
        checkInOutButton = view.findViewById(R.id.checkInOutButton);

        sharedPreferences = getActivity().getSharedPreferences("CheckInOutPrefs", Context.MODE_PRIVATE);
        isCheckedIn = sharedPreferences.getBoolean("isCheckedIn", false);
        logTextView.setText(sharedPreferences.getString("log", ""));
        updateButtonText();

        checkInOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isCheckedIn) {
                    addLog("퇴근: ");
                } else {
                    addLog("출근: ");
                }
                isCheckedIn = !isCheckedIn;
                updateButtonText();
                saveState();
            }
        });

        return view;
    }

    private void addLog(String prefix) {
        String currentTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.KOREA).format(new Date());
        String newLog = prefix + currentTime;
        String previousLogs = logTextView.getText().toString();
        logTextView.setText(previousLogs + "\n" + newLog);
    }

    private void updateButtonText() {
        if (isCheckedIn) {
            checkInOutButton.setText("퇴근");
        } else {
            checkInOutButton.setText("출근");
        }
    }

    private void saveState() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("isCheckedIn", isCheckedIn);
        editor.putString("log", logTextView.getText().toString());
        editor.apply();
    }
}
