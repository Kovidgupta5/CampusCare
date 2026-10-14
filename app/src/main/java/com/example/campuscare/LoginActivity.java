package com.example.campuscare;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    EditText edtUsername, edtPassword;
    Button btnLogin;
    DatabaseHelper myDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        myDb = new DatabaseHelper(this);
        edtUsername = findViewById(R.id.edtUsername);
        edtPassword = findViewById(R.id.edtPassword);
        btnLogin = findViewById(R.id.btnLogin);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = edtUsername.getText().toString().trim();
                String password = edtPassword.getText().toString().trim();

                if (username.isEmpty() || password.isEmpty()) {
                    Toast.makeText(LoginActivity.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                    return;
                }

                String role = myDb.checkUserRole(username, password);

                if (role != null) {
                    Toast.makeText(LoginActivity.this, "Welcome, " + role + "!", Toast.LENGTH_SHORT).show();

                    if (role.equals("Student")) {
                        Intent intent = new Intent(LoginActivity.this, CampusCareActivity.class);
                        startActivity(intent);
                        finish();
                    } else if (role.equals("Faculty") || role.equals("Resolver")) {
                        Intent intent = new Intent(LoginActivity.this, DashboardActivity.class);
                        intent.putExtra("USER_ROLE", role); // Pass specific role context
                        startActivity(intent);
                        finish();
                    }
                } else {
                    Toast.makeText(LoginActivity.this, "Invalid Username or Password", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}