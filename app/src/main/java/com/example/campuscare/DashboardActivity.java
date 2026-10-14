package com.example.campuscare;

import android.database.Cursor;
import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

public class DashboardActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    DatabaseHelper myDb;
    ArrayList<ComplaintModel> complaintList;
    ComplaintAdapter complaintAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        recyclerView = findViewById(R.id.recyclerViewComplaints);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        myDb = new DatabaseHelper(this);
        complaintList = new ArrayList<>();

        loadDataFromDB();

        complaintAdapter = new ComplaintAdapter(complaintList);
        recyclerView.setAdapter(complaintAdapter);
    }

    private void loadDataFromDB() {
        Cursor cursor = myDb.getAllData();
        if (cursor.getCount() == 0) {
            Toast.makeText(this, "No complaints found in database.", Toast.LENGTH_SHORT).show();
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

            ComplaintModel complaint = new ComplaintModel(id, name, location, category, severity, imageUri, status);
            complaintList.add(complaint);
        }
        cursor.close();
    }
}