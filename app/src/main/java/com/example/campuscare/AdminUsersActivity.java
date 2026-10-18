package com.example.campuscare;

import android.content.Intent;

import android.database.Cursor;

import android.os.Bundle;

import android.view.View;

import android.widget.ArrayAdapter;

import android.widget.Button;

import android.widget.EditText;

import android.widget.Spinner;

import android.widget.TextView;

import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import androidx.recyclerview.widget.LinearLayoutManager;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class AdminUsersActivity extends AppCompatActivity {

    RecyclerView recyclerView;

    EditText edtNewUsername, edtNewPassword;

    Spinner spinnerUserRole;

    Button btnAddUser;

    DatabaseHelper myDb;

    ArrayList<UserModel> userList;

    UserAdapter userAdapter;

    String currentUsername = "admin1";

    @Override

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_admin_users);

        if (getIntent() != null && getIntent().hasExtra("USERNAME")) {

            currentUsername = getIntent().getStringExtra("USERNAME");

        }

        myDb = new DatabaseHelper(this);

        edtNewUsername = findViewById(R.id.edtNewUsername);

        edtNewPassword = findViewById(R.id.edtNewPassword);

        spinnerUserRole = findViewById(R.id.spinnerUserRole);

        btnAddUser = findViewById(R.id.btnAddUser);

        recyclerView = findViewById(R.id.recyclerViewUsers);

        if (recyclerView != null) {

            recyclerView.setLayoutManager(new LinearLayoutManager(this));

        }

        userList = new ArrayList<>();

        String[] roles = {"Student", "Faculty", "Resolver"};

        ArrayAdapter<String> roleAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, roles);

        if (spinnerUserRole != null) {

            spinnerUserRole.setAdapter(roleAdapter);

        }

        loadUsersFromDB();

        userAdapter = new UserAdapter(userList);

        if (recyclerView != null) {

            recyclerView.setAdapter(userAdapter);

        }

        if (btnAddUser != null) {

            btnAddUser.setOnClickListener(v -> {

                String username = edtNewUsername != null ? edtNewUsername.getText().toString().trim() : "";

                String password = edtNewPassword != null ? edtNewPassword.getText().toString().trim() : "";

                String role = spinnerUserRole != null ? spinnerUserRole.getSelectedItem().toString() : "Student";

                if (username.isEmpty() || password.isEmpty()) {

                    Toast.makeText(AdminUsersActivity.this, "Please enter username and password", Toast.LENGTH_SHORT).show();

                } else {

                    boolean inserted = myDb.addUser(username, password, role);

                    if (inserted) {

                        Toast.makeText(AdminUsersActivity.this, "User added successfully!", Toast.LENGTH_SHORT).show();

                        if (edtNewUsername != null) edtNewUsername.setText("");

                        if (edtNewPassword != null) edtNewPassword.setText("");

                        loadUsersFromDB();

                        if (userAdapter != null) userAdapter.notifyDataSetChanged();

                    } else {

                        Toast.makeText(AdminUsersActivity.this, "Error adding user (Username may already exist)", Toast.LENGTH_SHORT).show();

                    }

                }

            });

        }

        // Admin Bottom Navigation Setup (Complaints, Users, Profile)

        TextView txtTab1 = findViewById(R.id.txtTab1);

        TextView txtTab2 = findViewById(R.id.txtTab2);

        TextView txtTab3 = findViewById(R.id.txtTab3);

        if (txtTab1 != null) txtTab1.setText("Complaints");

        if (txtTab2 != null) txtTab2.setText("Users");

        if (txtTab3 != null) txtTab3.setText("Profile");

        View navTab1 = findViewById(R.id.navTab1);

        View navTab2 = findViewById(R.id.navTab2);

        View navTab3 = findViewById(R.id.navTab3);

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

                // Already on Users page

            });

        }

        if (navTab3 != null) {

            navTab3.setOnClickListener(v -> {

                Intent intent = new Intent(this, ProfileActivity.class);

                intent.putExtra("USERNAME", currentUsername);

                intent.putExtra("USER_ROLE", "Admin");

                startActivity(intent);

                finish();

            });

        }

    }

    private void loadUsersFromDB() {

        if (userList == null) return;

        userList.clear();

        Cursor cursor = myDb.getAllUsers();

        if (cursor == null || cursor.getCount() == 0) {

            if (cursor != null) cursor.close();

            return;

        }

        while (cursor.moveToNext()) {

            int userId = cursor.getInt(0);

            String username = cursor.getString(1);

            String role = cursor.getString(3);

            UserModel user = new UserModel(userId, username, role);

            userList.add(user);

        }

        cursor.close();

    }

}
