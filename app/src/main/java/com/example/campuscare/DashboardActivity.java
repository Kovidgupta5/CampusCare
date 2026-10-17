package com.example.campuscare;

import android.content.Intent;

import android.database.Cursor;

import android.os.Bundle;

import android.view.View;

import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import androidx.recyclerview.widget.LinearLayoutManager;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class DashboardActivity extends AppCompatActivity {

    RecyclerView recyclerView;

    TextView tvEmpty;

    DatabaseHelper myDb;

    ArrayList<ComplaintModel> complaintList;

    ComplaintAdapter complaintAdapter;

    String userRole = "Student", currentUsername = "";

    @Override

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_dashboard);

        if (getIntent().hasExtra("USER_ROLE")) {

            userRole = getIntent().getStringExtra("USER_ROLE");

        }

        if (getIntent().hasExtra("USERNAME")) {

            currentUsername = getIntent().getStringExtra("USERNAME");

        }

        recyclerView = findViewById(R.id.recyclerViewComplaints);

        tvEmpty = findViewById(R.id.tvEmpty);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        myDb = new DatabaseHelper(this);

        complaintList = new ArrayList<>();

        loadDataFromDB();

        complaintAdapter = new ComplaintAdapter(complaintList, userRole);

        recyclerView.setAdapter(complaintAdapter);

        // Bottom Navigation Setup

        TextView txtTab1 = findViewById(R.id.txtTab1);

        TextView txtTab2 = findViewById(R.id.txtTab2);

        TextView txtTab3 = findViewById(R.id.txtTab3);

        txtTab1.setText("File");

        txtTab2.setText("View");

        txtTab3.setText("Profile");

        findViewById(R.id.navTab1).setOnClickListener(v -> {

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

        findViewById(R.id.navTab2).setOnClickListener(v -> {});

        findViewById(R.id.navTab3).setOnClickListener(v -> {

            Intent intent = new Intent(this, ProfileActivity.class);

            intent.putExtra("USERNAME", currentUsername);

            intent.putExtra("USER_ROLE", userRole);

            startActivity(intent);

            finish();

        });

    }

    @Override

    protected void onResume() {

        super.onResume();

        loadDataFromDB();

        if (complaintAdapter != null) {

            complaintAdapter.notifyDataSetChanged();

        }

    }

    private void loadDataFromDB() {

        complaintList.clear();

        Cursor cursor = myDb.getAllData();

        if (cursor.getCount() == 0) {

            tvEmpty.setVisibility(View.VISIBLE);

            recyclerView.setVisibility(View.GONE);

            cursor.close();

            return;

        }

        tvEmpty.setVisibility(View.GONE);

        recyclerView.setVisibility(View.VISIBLE);

        while (cursor.moveToNext()) {

            int id = cursor.getInt(0);

            String name = cursor.getString(1);

            String location = cursor.getString(2);

            String category = cursor.getString(3);

            String severity = cursor.getString(4);

            String imageUri = cursor.getString(5);

            String status = cursor.getString(6);

            String resImageUri = cursor.getString(7);

            ComplaintModel complaint = new ComplaintModel(id, name, location, category, severity, imageUri, status, resImageUri);

            complaintList.add(complaint);

        }

        cursor.close();

    }

}
