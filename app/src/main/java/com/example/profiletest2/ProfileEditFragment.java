package com.example.profiletest2;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class ProfileEditFragment extends Fragment {

    private ImageView profileImageView;
    private EditText etName;
    private Button btnSelectPhoto;
    private Button btnSave;
    private Button btnViewEmployees;
    private Button btnDeleteAccount;
    private TextView tvManagerProfile;

    private static final int REQUEST_CODE_PERMISSION = 100;
    private static final int REQUEST_CODE_PICK_IMAGE = 101;
    private static final String FILE_PATH = "profiles/profile.txt";
    private DatabaseHelper databaseHelper;
    private SharedPreferences sharedPreferences;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile_edit, container, false);

        profileImageView = view.findViewById(R.id.profileImageView);
        etName = view.findViewById(R.id.etName);
        btnSelectPhoto = view.findViewById(R.id.btnSelectPhoto);
        btnSave = view.findViewById(R.id.btnSave);
        btnViewEmployees = view.findViewById(R.id.btnViewEmployees);
        btnDeleteAccount = view.findViewById(R.id.btnDeleteAccount);
        tvManagerProfile = view.findViewById(R.id.tvManagerProfile);

        sharedPreferences = getActivity().getSharedPreferences("LoginPrefs", Context.MODE_PRIVATE);
        databaseHelper = new DatabaseHelper(getContext());

        String role = sharedPreferences.getString("role", "Unknown");
        String managerProfile = sharedPreferences.getString("managerProfile", "");

        if ("사장".equals(role)) {
            btnViewEmployees.setVisibility(View.VISIBLE);
        } else {
            tvManagerProfile.setVisibility(View.VISIBLE);
            tvManagerProfile.setText(managerProfile);
        }

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

        btnViewEmployees.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ViewEmployeesActivity.class);
                startActivity(intent);
            }
        });

        btnDeleteAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDeleteConfirmationDialog();
            }
        });

        return view;
    }

    private void requestPermissionAndPickImage() {
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(),
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
                Toast.makeText(getContext(), "외부 저장소 읽기 권한이 필요합니다.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_PICK_IMAGE && resultCode == getActivity().RESULT_OK && data != null) {
            Uri imageUri = data.getData();
            profileImageView.setImageURI(imageUri); // 선택한 사진을 이미지뷰에 표시
        }
    }

    private void saveProfile() {
        String photoPath = ""; // 사용자가 선택한 사진의 경로
        String name = etName.getText().toString();

        try {
            File directory = new File(getContext().getFilesDir(), "profiles");
            if (!directory.exists()) {
                directory.mkdirs();
            }

            File file = new File(directory, FILE_PATH);
            FileWriter writer = new FileWriter(file);

            writer.write("PhotoPath: " + photoPath + "\n");
            writer.write("Name: " + name + "\n");

            writer.close();
            Toast.makeText(getContext(), "프로필이 저장되었습니다.", Toast.LENGTH_SHORT).show();

            getActivity().getSupportFragmentManager().popBackStack();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(getContext(), "프로필 저장에 실패하였습니다.", Toast.LENGTH_SHORT).show();
        }
    }

    private void showDeleteConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("계정 삭제")
                .setMessage("삭제하시겠습니까?")
                .setPositiveButton("예", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        showFinalDeleteConfirmationDialog();
                    }
                })
                .setNegativeButton("아니오", null)
                .show()
                .getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(ContextCompat.getColor(getContext(), android.R.color.holo_red_dark));
        builder.show().getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(ContextCompat.getColor(getContext(), android.R.color.holo_red_dark));
    }

    private void showFinalDeleteConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("계정 삭제")
                .setMessage("정말 삭제하시겠습니까? 이 작업은 되돌릴 수 없습니다.")
                .setPositiveButton("예", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteAccount();
                    }
                })
                .setNegativeButton("아니오", null)
                .show()
                .getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(ContextCompat.getColor(getContext(), android.R.color.holo_red_dark));
        builder.show().getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(ContextCompat.getColor(getContext(), android.R.color.holo_red_dark));
    }

    private void deleteAccount() {
        String username = sharedPreferences.getString("currentUser", null);
        if (username != null) {
            boolean isDeleted = databaseHelper.deleteUser(username);
            if (isDeleted) {
                Toast.makeText(getContext(), "계정이 삭제되었습니다.", Toast.LENGTH_SHORT).show();
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.remove("currentUser");
                editor.apply();

                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
                getActivity().finish();
            } else {
                Toast.makeText(getContext(), "계정 삭제에 실패하였습니다.", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
