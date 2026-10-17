package com.example.campuscare;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class ProfileActivity extends AppCompatActivity {

    TextView tvLoggedUser;
    EditText edtNewPassword;
    Button btnUpdatePassword, btnLogout;
    DatabaseHelper myDb;
    String currentUsername = "", userRole = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        myDb = new DatabaseHelper(this);

        if (getIntent().hasExtra("USERNAME")) {
            currentUsername = getIntent().getStringExtra("USERNAME");
        }
        if (getIntent().hasExtra("USER_ROLE")) {
            userRole = getIntent().getStringExtra("USER_ROLE");
        }

        tvLoggedUser = findViewById(R.id.tvLoggedUser);
        edtNewPassword = findViewById(R.id.edtNewPassword);
        btnUpdatePassword = findViewById(R.id.btnUpdatePassword);
        btnLogout = findViewById(R.id.btnLogout);

        tvLoggedUser.setText("Logged in as: " + currentUsername + " (" + userRole + ")");

        btnUpdatePassword.setOnClickListener(v -> {
            String newPassword = edtNewPassword.getText().toString().trim();
            if (newPassword.isEmpty()) {
                Toast.makeText(ProfileActivity.this, "Please enter a new password", Toast.LENGTH_SHORT).show();
            } else {
                boolean updated = myDb.updatePassword(currentUsername, newPassword);
                if (updated) {
                    Toast.makeText(ProfileActivity.this, "Password updated successfully!", Toast.LENGTH_LONG).show();
                    edtNewPassword.setText("");
                } else {
                    Toast.makeText(ProfileActivity.this, "Error updating password", Toast.LENGTH_LONG).show();
                }
            }
        });

        btnLogout.setOnClickListener(v -> {
            Intent intent = new Intent(ProfileActivity.this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });
    }
}