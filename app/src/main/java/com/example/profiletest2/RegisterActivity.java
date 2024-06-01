package com.example.profiletest2;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class RegisterActivity extends AppCompatActivity {

    private EditText etUsername, etPassword, etCompanyName, etUniqueId;
    private Button btnRegister;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        etCompanyName = findViewById(R.id.etCompanyName);
        etUniqueId = findViewById(R.id.etUniqueId);
        btnRegister = findViewById(R.id.btnRegister);

        sharedPreferences = getSharedPreferences("LoginPrefs", MODE_PRIVATE);

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = etUsername.getText().toString();
                String password = etPassword.getText().toString();
                String companyName = etCompanyName.getText().toString();
                String uniqueId = etUniqueId.getText().toString();

                if (!username.isEmpty() && !password.isEmpty() && !companyName.isEmpty() && !uniqueId.isEmpty()) {
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("name", username);
                    editor.putString("companyName", companyName);
                    editor.putString("role", "사장"); // 예제에서는 사장으로 설정
                    editor.putString("uniqueId", uniqueId);
                    editor.putString("password", password);
                    editor.apply();

                    Toast.makeText(RegisterActivity.this, "회원가입이 완료되었습니다.", Toast.LENGTH_SHORT).show();
                    finish(); // 회원가입 후 로그인 화면으로 돌아가기
                } else {
                    Toast.makeText(RegisterActivity.this, "모든 필드를 입력해주세요.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
