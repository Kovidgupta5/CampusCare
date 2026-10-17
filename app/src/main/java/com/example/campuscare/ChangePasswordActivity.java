package com.example.campuscare;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class ChangePasswordActivity extends AppCompatActivity {

    EditText edtUsername, edtNewPassword;
    Button btnUpdatePassword;
    DatabaseHelper myDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        myDb = new DatabaseHelper(this);
        edtUsername = findViewById(R.id.edtUsername);
        edtNewPassword = findViewById(R.id.edtNewPassword);
        btnUpdatePassword = findViewById(R.id.btnUpdatePassword);

        btnUpdatePassword.setOnClickListener(v -> {
            String username = edtUsername.getText().toString().trim();
            String newPassword = edtNewPassword.getText().toString().trim();

            if (username.isEmpty() || newPassword.isEmpty()) {
                Toast.makeText(ChangePasswordActivity.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            } else {
                boolean updated = myDb.updatePassword(username, newPassword);
                if (updated) {
                    Toast.makeText(ChangePasswordActivity.this, "Password updated successfully!", Toast.LENGTH_LONG).show();
                    finish();
                } else {
                    Toast.makeText(ChangePasswordActivity.this, "Username not found", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}