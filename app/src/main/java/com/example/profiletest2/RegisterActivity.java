package com.example.profiletest2;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class RegisterActivity extends AppCompatActivity {

    private EditText etUsername, etPassword, etCompanyName, etUniqueId;
    private CheckBox cbOwner;
    private Button btnRegister, btnBackToLogin;
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        etCompanyName = findViewById(R.id.etCompanyName);
        etUniqueId = findViewById(R.id.etUniqueId);
        cbOwner = findViewById(R.id.cbOwner);
        btnRegister = findViewById(R.id.btnRegister);
        btnBackToLogin = findViewById(R.id.btnBackToLogin);

        databaseHelper = new DatabaseHelper(this);

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = etUsername.getText().toString();
                String password = etPassword.getText().toString();
                String companyName = etCompanyName.getText().toString();
                String uniqueId = etUniqueId.getText().toString();
                boolean isOwner = cbOwner.isChecked();

                if (!username.isEmpty() && !password.isEmpty() && !companyName.isEmpty() && !uniqueId.isEmpty()) {
                    long result = databaseHelper.addUser(username, password, companyName, uniqueId, isOwner ? "사장" : "직원", uniqueId);
                    if (result != -1) {
                        Toast.makeText(RegisterActivity.this, "회원가입이 완료되었습니다.", Toast.LENGTH_SHORT).show();
                        finish(); // 회원가입 후 로그인 화면으로 돌아가기
                    } else {
                        Toast.makeText(RegisterActivity.this, "회원가입에 실패했습니다.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(RegisterActivity.this, "모든 필드를 입력해주세요.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnBackToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); // 로그인 화면으로 돌아가기
            }
        });
    }
}
