package com.example.campuscare;

import android.database.Cursor;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

public class AdminActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    EditText edtNewUsername, edtNewPassword;
    Spinner spinnerUserRole;
    Button btnAddUser;
    DatabaseHelper myDb;
    ArrayList<UserModel> userList;
    UserAdapter userAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        myDb = new DatabaseHelper(this);
        edtNewUsername = findViewById(R.id.edtNewUsername);
        edtNewPassword = findViewById(R.id.edtNewPassword);
        spinnerUserRole = findViewById(R.id.spinnerUserRole);
        btnAddUser = findViewById(R.id.btnAddUser);
        recyclerView = findViewById(R.id.recyclerViewAdmin);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        userList = new ArrayList<>();

        // Added "Resolver" to the available roles list
        String[] roles = {"Student", "Faculty", "Resolver"};
        ArrayAdapter<String> roleAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, roles);
        spinnerUserRole.setAdapter(roleAdapter);

        loadUsersFromDB();

        userAdapter = new UserAdapter(userList);
        recyclerView.setAdapter(userAdapter);

        btnAddUser.setOnClickListener(v -> {
            String username = edtNewUsername.getText().toString().trim();
            String password = edtNewPassword.getText().toString().trim();
            String role = spinnerUserRole.getSelectedItem().toString();

            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(AdminActivity.this, "Please enter username and password", Toast.LENGTH_SHORT).show();
            } else {
                boolean inserted = myDb.addUser(username, password, role);
                if (inserted) {
                    Toast.makeText(AdminActivity.this, "User added successfully!", Toast.LENGTH_SHORT).show();
                    edtNewUsername.setText("");
                    edtNewPassword.setText("");
                    loadUsersFromDB();
                    userAdapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(AdminActivity.this, "Error adding user (Username may already exist)", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void loadUsersFromDB() {
        userList.clear();
        Cursor cursor = myDb.getAllUsers();
        if (cursor.getCount() == 0) {
            cursor.close();
            return;
        }

        while (cursor.moveToNext()) {
            int userId = cursor.getInt(0);
            String username = cursor.getString(1);
            String role = cursor.getString(3); // Column index 3 is ROLE

            UserModel user = new UserModel(userId, username, role);
            userList.add(user);
        }
        cursor.close();
    }
}