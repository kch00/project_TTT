package com.example.profiletest2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class RegisterActivity extends AppCompatActivity {

    private EditText etUsername, etPassword, etConfirmPassword, etUniqueId, etCompanyName;
    private RadioGroup rgRole;
    private RadioButton rbOwner, rbEmployee;
    private Button btnRegister, btnBack;
    private DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        db = new DatabaseHelper(this);

        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        etConfirmPassword = findViewById(R.id.etConfirmPassword);
        etUniqueId = findViewById(R.id.etUniqueId);
        etCompanyName = findViewById(R.id.etCompanyName);
        rgRole = findViewById(R.id.rgRole);
        rbOwner = findViewById(R.id.rbOwner);
        rbEmployee = findViewById(R.id.rbEmployee);
        btnRegister = findViewById(R.id.btnRegister);
        btnBack = findViewById(R.id.btnBack);

        rgRole.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.rbOwner) {
                etCompanyName.setVisibility(View.VISIBLE);
            } else {
                etCompanyName.setVisibility(View.GONE);
            }
        });

        btnRegister.setOnClickListener(view -> {
            String username = etUsername.getText().toString().trim();
            String password = etPassword.getText().toString().trim();
            String confirmPassword = etConfirmPassword.getText().toString().trim();
            String uniqueId = etUniqueId.getText().toString().trim();
            String companyName = etCompanyName.getText().toString().trim();
            String role = rbOwner.isChecked() ? "Owner" : "Employee";

            if (username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty() || uniqueId.isEmpty() || (rbOwner.isChecked() && companyName.isEmpty())) {
                Toast.makeText(RegisterActivity.this, "모든 필드를 입력하세요.", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!password.equals(confirmPassword)) {
                Toast.makeText(RegisterActivity.this, "비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show();
                return;
            }

            boolean isInserted = db.insertUser(username, password, role, uniqueId, rbOwner.isChecked() ? companyName : "");
            if (isInserted) {
                Toast.makeText(RegisterActivity.this, "회원가입 성공", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(RegisterActivity.this, "회원가입 실패: 데이터베이스 오류", Toast.LENGTH_SHORT).show();
            }
        });

        btnBack.setOnClickListener(view -> {
            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        });
    }
}
