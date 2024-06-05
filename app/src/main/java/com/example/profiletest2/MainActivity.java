package com.example.profiletest2;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    private TextView userInfoTextView;
    private LinearLayout mainLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        userInfoTextView = findViewById(R.id.userInfoTextView);
        mainLayout = findViewById(R.id.mainLayout);

        SharedPreferences sharedPreferences = getSharedPreferences("LoginPrefs", MODE_PRIVATE);
        String username = sharedPreferences.getString("username", "Unknown");
        String companyId = sharedPreferences.getString("uniqueId", "Unknown");
        String role = sharedPreferences.getString("role", "Unknown");

        userInfoTextView.setText(username + " (" + companyId + " - " + role + ")");

        BottomNavigationView navView = findViewById(R.id.bottom_navigation);
        navView.setOnNavigationItemSelectedListener(navListener);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment, new HomeFragment()).commit();
        }
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            menuItem -> {
                Fragment selectedFragment = null;

                switch (menuItem.getItemId()) {
                    case R.id.navigation_home:
                        selectedFragment = new HomeFragment();
                        showMainLayout(true);
                        break;
                    case R.id.navigation_checkinout:
                        selectedFragment = new CheckInOutFragment();
                        showMainLayout(false);
                        break;
                    case R.id.navigation_todo:
                        selectedFragment = new TodoFragment();
                        showMainLayout(false);
                        break;
                    case R.id.navigation_profile_edit:
                        selectedFragment = new ProfileEditFragment();
                        showMainLayout(false);
                        break;
                }

                getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment, selectedFragment).commit();
                return true;
            };

    private void showMainLayout(boolean show) {
        if (show) {
            mainLayout.setVisibility(View.VISIBLE);
        } else {
            mainLayout.setVisibility(View.GONE);
        }
    }
}
