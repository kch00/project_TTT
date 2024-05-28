package com.example.profiletest2;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class ProfileEditActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;
    private ImageView profileImageView;
    private EditText etName;
    private Button btnSelectPhoto;
    private Button btnSave;
    private DatabaseHelper db;
    private int userId;
    private Uri imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_edit);

        db = new DatabaseHelper(this);

        // 현재 사용자의 ID를 가져옵니다
        userId = getIntent().getIntExtra("USER_ID", -1);

        profileImageView = findViewById(R.id.profileImageView);
        etName = findViewById(R.id.etName);
        btnSelectPhoto = findViewById(R.id.btnSelectPhoto);
        btnSave = findViewById(R.id.btnSave);

        btnSelectPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveProfile();
            }
        });

        // 프로필 사진을 로드합니다
        loadProfile();
    }

    private void openGallery() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "이미지를 선택하세요"), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            profileImageView.setImageURI(imageUri);
        }
    }

    private void saveProfile() {
        String photoUri = (imageUri != null) ? imageUri.toString() : "";
        String name = etName.getText().toString();

        // 프로필 정보를 저장합니다
        boolean isInserted = db.insertProfilePhotoUri(userId, photoUri);
        if (isInserted) {
            Toast.makeText(this, "프로필이 저장되었습니다.", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(ProfileEditActivity.this, MainActivity.class);
            intent.putExtra("USER_ID", userId);
            startActivity(intent);
            finish();
        } else {
            Toast.makeText(this, "프로필 저장에 실패하였습니다.", Toast.LENGTH_SHORT).show();
        }
    }

    private void loadProfile() {
        String photoUri = db.getProfilePhotoUri(userId);
        if (photoUri != null) {
            imageUri = Uri.parse(photoUri);
            profileImageView.setImageURI(imageUri);
        }
    }
}
