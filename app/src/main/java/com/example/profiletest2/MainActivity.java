package com.example.profiletest2;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    private TextView userInfoTextView;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        userInfoTextView = findViewById(R.id.userInfoTextView);
        sharedPreferences = getSharedPreferences("LoginPrefs", MODE_PRIVATE);

        String name = sharedPreferences.getString("name", "Unknown");
        String companyName = sharedPreferences.getString("companyName", "Unknown");
        String role = sharedPreferences.getString("role", "Unknown");

        userInfoTextView.setText(String.format("%s, %s, %s", name, companyName, role));

        BottomNavigationView navView = findViewById(R.id.bottom_navigation);
        navView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment selectedFragment = null;
                switch (item.getItemId()) {
                    case R.id.navigation_home:
                        selectedFragment = new HomeFragment();
                        break;
                    case R.id.navigation_checkinout:
                        selectedFragment = new CheckInOutFragment();
                        break;
                    case R.id.navigation_todo:
                        selectedFragment = new TodoFragment();
                        break;
                    case R.id.navigation_profile_edit:
                        selectedFragment = new ProfileEditFragment();
                        break;
                }
                if (selectedFragment != null) {
                    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.nav_host_fragment, selectedFragment);
                    transaction.commit();
                }
                return true;
            }
        });

        // 기본 프래그먼트 설정
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment, new HomeFragment()).commit();
        }
    }
}
