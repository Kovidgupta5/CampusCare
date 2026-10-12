package com.example.campuscare;

import android.os.Bundle;

import android.view.View;

import android.widget.ArrayAdapter;

import android.widget.Button;

import android.widget.EditText;

import android.widget.RadioButton;

import android.widget.RadioGroup;

import android.widget.Spinner;

import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class CampusCareActivity extends AppCompatActivity {

    EditText edtName, edtLocation;

    Spinner spinnerCategory;

    RadioGroup radioGroupSeverity;

    Button btnSubmit;

    DatabaseHelper myDb; // Database reference

    @Override

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_campus_care);

        myDb = new DatabaseHelper(this); // Initialize database helper

        edtName = findViewById(R.id.edtReporterName);

        edtLocation = findViewById(R.id.edtLocation);

        spinnerCategory = findViewById(R.id.spinnerCategory);

        radioGroupSeverity = findViewById(R.id.radioGroupSeverity);

        btnSubmit = findViewById(R.id.btnSubmitComplaint);

        String[] categories = {

                "Fan / AC",

                "Light / Switch",

                "Wi-Fi Network",

                "Smartboard / Projector",

                "Washroom Cleanliness"

        };

        ArrayAdapter<String> adapter = new ArrayAdapter<>(

                this,

                android.R.layout.simple_spinner_dropdown_item,

                categories

        );

        spinnerCategory.setAdapter(adapter);

        btnSubmit.setOnClickListener(new View.OnClickListener() {

            @Override

            public void onClick(View v) {

                String name = edtName.getText().toString().trim();

                String location = edtLocation.getText().toString().trim();

                if (name.isEmpty()) {

                    edtName.requestFocus();

                    edtName.setError("Enter Reporter Name");

                } else if (location.isEmpty()) {

                    edtLocation.requestFocus();

                    edtLocation.setError("Enter Location");

                } else {

                    int selectedId = radioGroupSeverity.getCheckedRadioButtonId();

                    if (selectedId == -1) {

                        Toast.makeText(

                                CampusCareActivity.this,

                                "Please select a severity level",

                                Toast.LENGTH_SHORT

                        ).show();

                        return;

                    }

                    RadioButton selectedRadioButton = findViewById(selectedId);

                    String severity = selectedRadioButton.getText().toString();

                    String category = spinnerCategory.getSelectedItem().toString();

                    // Save data into SQLite database

                    boolean isInserted = myDb.insertData(name, location, category, severity);

                    if (isInserted) {

                        Toast.makeText(

                                CampusCareActivity.this,

                                "Complaint Saved to Database Successfully!",

                                Toast.LENGTH_LONG

                        ).show();

                    } else {

                        Toast.makeText(

                                CampusCareActivity.this,

                                "Error Saving Complaint",

                                Toast.LENGTH_LONG

                        ).show();

                    }

                }

            }

        });

    }

}
