package com.example.campuscare;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

public class DashboardActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    Button btnOpenForm;
    DatabaseHelper myDb;
    ArrayList<ComplaintModel> complaintList;
    ComplaintAdapter complaintAdapter;
    String userRole = "Student";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        if (getIntent().hasExtra("USER_ROLE")) {
            userRole = getIntent().getStringExtra("USER_ROLE");
        }

        recyclerView = findViewById(R.id.recyclerViewComplaints);
        btnOpenForm = findViewById(R.id.btnOpenForm);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        myDb = new DatabaseHelper(this);
        complaintList = new ArrayList<>();

        loadDataFromDB();

        complaintAdapter = new ComplaintAdapter(complaintList, userRole);
        recyclerView.setAdapter(complaintAdapter);

        // Allows Faculty or any dashboard user to report a new issue
        btnOpenForm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DashboardActivity.this, CampusCareActivity.class);
                startActivity(intent);
            }
        });
    }

    private void loadDataFromDB() {
        Cursor cursor = myDb.getAllData();
        if (cursor.getCount() == 0) {
            return;
        }

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