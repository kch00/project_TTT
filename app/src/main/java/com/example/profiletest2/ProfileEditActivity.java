package com.example.profiletest2;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class ProfileEditActivity extends AppCompatActivity {

    private ImageView profileImageView;
    private EditText etName;
    private Button btnSelectPhoto;
    private Button btnSave;

    private static final int REQUEST_CODE_PERMISSION = 100;
    private static final int REQUEST_CODE_PICK_IMAGE = 101;
    private static final String FILE_PATH = "profiles/profile.txt";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_edit);

        profileImageView = findViewById(R.id.profileImageView);
        etName = findViewById(R.id.etName);
        btnSelectPhoto = findViewById(R.id.btnSelectPhoto);
        btnSave = findViewById(R.id.btnSave);

        btnSelectPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestPermissionAndPickImage();
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveProfile();
            }
        });
    }

    private void requestPermissionAndPickImage() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    REQUEST_CODE_PERMISSION);
        } else {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent, REQUEST_CODE_PICK_IMAGE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, REQUEST_CODE_PICK_IMAGE);
            } else {
                Toast.makeText(this, "외부 저장소 읽기 권한이 필요합니다.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_PICK_IMAGE && resultCode == RESULT_OK && data != null) {
            Uri imageUri = data.getData();
            profileImageView.setImageURI(imageUri); // 선택한 사진을 이미지뷰에 표시
        }
    }

    private void saveProfile() {
        String photoPath = ""; // 사용자가 선택한 사진의 경로
        String name = etName.getText().toString();

        try {
            File directory = new File(getFilesDir(), "profiles");
            if (!directory.exists()) {
                directory.mkdirs();
            }

            File file = new File(getFilesDir(), FILE_PATH);
            FileWriter writer = new FileWriter(file);

            writer.write("PhotoPath: " + photoPath + "\n");
            writer.write("Name: " + name + "\n");

            writer.close();
            Toast.makeText(this, "프로필이 저장되었습니다.", Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(ProfileEditActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "프로필 저장에 실패하였습니다.", Toast.LENGTH_SHORT).show();
        }
    }
}



