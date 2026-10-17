package com.example.campuscare;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class ProfileActivity extends AppCompatActivity {

    TextView tvLoggedUser;
    EditText edtNewPassword;
    Button btnUpdatePassword, btnLogout;
    DatabaseHelper myDb;
    String currentUsername = "User", userRole = "Student";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        myDb = new DatabaseHelper(this);

        if (getIntent() != null) {
            if (getIntent().hasExtra("USERNAME") && getIntent().getStringExtra("USERNAME") != null) {
                currentUsername = getIntent().getStringExtra("USERNAME");
            }
            if (getIntent().hasExtra("USER_ROLE") && getIntent().getStringExtra("USER_ROLE") != null) {
                userRole = getIntent().getStringExtra("USER_ROLE");
            }
        }

        tvLoggedUser = findViewById(R.id.tvLoggedUser);
        edtNewPassword = findViewById(R.id.edtNewPassword);
        btnUpdatePassword = findViewById(R.id.btnUpdatePassword);
        btnLogout = findViewById(R.id.btnLogout);

        if (tvLoggedUser != null) {
            tvLoggedUser.setText("Logged in as: " + currentUsername + " (" + userRole + ")");
        }

        if (btnUpdatePassword != null) {
            btnUpdatePassword.setOnClickListener(v -> {
                String newPassword = edtNewPassword != null ? edtNewPassword.getText().toString().trim() : "";
                if (newPassword.isEmpty()) {
                    Toast.makeText(ProfileActivity.this, "Please enter a new password", Toast.LENGTH_SHORT).show();
                } else {
                    boolean updated = myDb.updatePassword(currentUsername, newPassword);
                    if (updated) {
                        Toast.makeText(ProfileActivity.this, "Password updated successfully!", Toast.LENGTH_LONG).show();
                        if (edtNewPassword != null) edtNewPassword.setText("");
                    } else {
                        Toast.makeText(ProfileActivity.this, "Error updating password", Toast.LENGTH_LONG).show();
                    }
                }
            });
        }

        if (btnLogout != null) {
            btnLogout.setOnClickListener(v -> {
                Intent intent = new Intent(ProfileActivity.this, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            });
        }

        // Safe Bottom Navigation Configuration
        TextView txtTab1 = findViewById(R.id.txtTab1);
        TextView txtTab2 = findViewById(R.id.txtTab2);
        TextView txtTab3 = findViewById(R.id.txtTab3);
        LinearLayout navTab1 = findViewById(R.id.navTab1);
        LinearLayout navTab2 = findViewById(R.id.navTab2);
        LinearLayout navTab3 = findViewById(R.id.navTab3);

        if (userRole.equals("Resolver")) {
            if (navTab2 != null) navTab2.setVisibility(View.GONE);
            if (txtTab1 != null) txtTab1.setText("Tasks");
            if (txtTab3 != null) txtTab3.setText("Profile");

            if (navTab1 != null) {
                navTab1.setOnClickListener(v -> {
                    Intent intent = new Intent(this, ResolverActivity.class);
                    intent.putExtra("USERNAME", currentUsername);
                    intent.putExtra("USER_ROLE", "Resolver");
                    startActivity(intent);
                    finish();
                });
            }
        } else if (userRole.equals("Admin")) {
            if (txtTab1 != null) txtTab1.setText("Complaints");
            if (txtTab2 != null) txtTab2.setText("Users");
            if (txtTab3 != null) txtTab3.setText("Profile");

            if (navTab1 != null) {
                navTab1.setOnClickListener(v -> {
                    Intent intent = new Intent(this, AdminActivity.class);
                    intent.putExtra("USERNAME", currentUsername);
                    intent.putExtra("USER_ROLE", "Admin");
                    startActivity(intent);
                    finish();
                });
            }
            if (navTab2 != null) {
                navTab2.setOnClickListener(v -> {
                    Intent intent = new Intent(this, AdminUsersActivity.class);
                    intent.putExtra("USERNAME", currentUsername);
                    intent.putExtra("USER_ROLE", "Admin");
                    startActivity(intent);
                    finish();
                });
            }
        } else {
            // Student or Faculty
            if (txtTab1 != null) txtTab1.setText("File");
            if (txtTab2 != null) txtTab2.setText("View");
            if (txtTab3 != null) txtTab3.setText("Profile");

            if (navTab1 != null) {
                navTab1.setOnClickListener(v -> {
                    Intent intent;
                    if (userRole.equals("Faculty")) {
                        intent = new Intent(this, FacultyFormActivity.class);
                    } else {
                        intent = new Intent(this, StudentFormActivity.class);
                    }
                    intent.putExtra("USERNAME", currentUsername);
                    intent.putExtra("USER_ROLE", userRole);
                    startActivity(intent);
                    finish();
                });
            }
            if (navTab2 != null) {
                navTab2.setOnClickListener(v -> {
                    Intent intent = new Intent(this, DashboardActivity.class);
                    intent.putExtra("USERNAME", currentUsername);
                    intent.putExtra("USER_ROLE", userRole);
                    startActivity(intent);
                    finish();
                });
            }
        }

        if (navTab3 != null) {
            navTab3.setOnClickListener(v -> {
                // Already on profile page
            });
        }
    }
}