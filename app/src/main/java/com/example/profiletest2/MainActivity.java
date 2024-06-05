package com.example.profiletest2;

import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sharedPreferences = getSharedPreferences("LoginPrefs", MODE_PRIVATE);
        String role = sharedPreferences.getString("role", "Unknown");

        BottomNavigationView navView = findViewById(R.id.bottom_navigation);
        navView.setOnNavigationItemSelectedListener(navListener);

        // Load default fragment based on role
        if (savedInstanceState == null) {
            if (role.equals("사장")) {
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ManagerHomeFragment()).commit();
                setTitle("메인화면");
            } else {
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new EmployeeHomeFragment()).commit();
                setTitle("메인화면");
            }
        }
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull android.view.MenuItem item) {
            Fragment selectedFragment = null;
            String role = sharedPreferences.getString("role", "Unknown");

            switch (item.getItemId()) {
                case R.id.navigation_home:
                    selectedFragment = role.equals("사장") ? new ManagerHomeFragment() : new EmployeeHomeFragment();
                    setTitle("메인화면");
                    break;
                case R.id.navigation_checkinout:
                    selectedFragment = role.equals("사장") ? new ManagerAttendanceFragment() : new EmployeeAttendanceFragment();
                    setTitle("출/퇴근");
                    break;
                case R.id.navigation_todo:
                    selectedFragment = role.equals("사장") ? new ManagerTodoFragment() : new EmployeeTodoFragment();
                    setTitle("TODO List");
                    break;
                case R.id.navigation_profile_edit:
                    selectedFragment = role.equals("사장") ? new ManagerProfileFragment() : new EmployeeProfileFragment();
                    setTitle("프로필 수정");
                    break;
            }

            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment).commit();
            return true;
        }
    };
}
