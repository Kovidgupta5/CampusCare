package com.example.campuscare;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class StudentFormActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;

    EditText edtName, edtDynamicLocation;
    Spinner spinnerLocationType, spinnerSubLocation, spinnerCategory;
    RadioGroup radioGroupSeverity;
    Button btnSubmit, btnViewDashboard, btnSelectImage;
    ImageView imgComplaintPreview;
    DatabaseHelper myDb;

    String imageUriString = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_form);

        myDb = new DatabaseHelper(this);

        edtName = findViewById(R.id.edtStudentName);
        edtDynamicLocation = findViewById(R.id.edtDynamicLocation);
        spinnerLocationType = findViewById(R.id.spinnerLocationType);
        spinnerSubLocation = findViewById(R.id.spinnerSubLocation);
        spinnerCategory = findViewById(R.id.spinnerCategory);
        radioGroupSeverity = findViewById(R.id.radioGroupSeverity);
        btnSubmit = findViewById(R.id.btnSubmitComplaint);
        btnViewDashboard = findViewById(R.id.btnViewDashboard);
        btnSelectImage = findViewById(R.id.btnSelectImage);
        imgComplaintPreview = findViewById(R.id.imgComplaintPreview);

        // Main Area Options (Academic Block, Hostel, Washroom)
        String[] mainLocations = {"Academic Block", "Hostel", "Washroom"};
        ArrayAdapter<String> mainAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, mainLocations);
        spinnerLocationType.setAdapter(mainAdapter);

        spinnerLocationType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedType = parent.getItemAtPosition(position).toString();

                if (selectedType.equals("Academic Block")) {
                    String[] academicSubs = {"Campus", "Lecture Room (LR)", "Lab"};
                    ArrayAdapter<String> subAdapter = new ArrayAdapter<>(StudentFormActivity.this, android.R.layout.simple_spinner_dropdown_item, academicSubs);
                    spinnerSubLocation.setAdapter(subAdapter);
                    spinnerSubLocation.setVisibility(View.VISIBLE);
                    edtDynamicLocation.setVisibility(View.VISIBLE);
                    edtDynamicLocation.setHint("Enter Campus / LR / Lab Details");
                } else if (selectedType.equals("Hostel")) {
                    String[] hostelSubs = {"Hostel Room", "Mess / Dining Hall", "TV Room", "Hostel Corridor"};
                    ArrayAdapter<String> subAdapter = new ArrayAdapter<>(StudentFormActivity.this, android.R.layout.simple_spinner_dropdown_item, hostelSubs);
                    spinnerSubLocation.setAdapter(subAdapter);
                    spinnerSubLocation.setVisibility(View.VISIBLE);
                    edtDynamicLocation.setVisibility(View.VISIBLE);
                    edtDynamicLocation.setHint("Enter Room Number / Location Details");
                } else {
                    // General Washroom selected
                    spinnerSubLocation.setVisibility(View.GONE);
                    edtDynamicLocation.setVisibility(View.VISIBLE);
                    edtDynamicLocation.setHint("Enter Washroom Location");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        // Dynamic hint updates based on sub-location selection
        spinnerSubLocation.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String subLoc = parent.getItemAtPosition(position).toString();
                if (subLoc.equals("Hostel Room")) {
                    edtDynamicLocation.setHint("Enter Room Number");
                } else if (subLoc.equals("Lecture Room (LR)")) {
                    edtDynamicLocation.setHint("Enter LR Room Number");
                } else if (subLoc.equals("Lab")) {
                    edtDynamicLocation.setHint("Enter Lab Location / Number");
                } else if (subLoc.equals("Campus")) {
                    edtDynamicLocation.setHint("Enter Campus Location");
                } else {
                    edtDynamicLocation.setHint("Enter Specific Location Details");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        // Categories
        String[] categories = {"Fan / AC", "Light / Switch", "Wi-Fi Network", "Smartboard / Projector", "Plumbing / General"};
        ArrayAdapter<String> catAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, categories);
        spinnerCategory.setAdapter(catAdapter);

        btnSelectImage.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent, PICK_IMAGE_REQUEST);
        });

        btnSubmit.setOnClickListener(v -> {
            String name = edtName.getText().toString().trim();
            String mainType = spinnerLocationType.getSelectedItem().toString();
            String detailLocation = edtDynamicLocation.getText().toString().trim();

            String location;
            if (mainType.equals("Washroom")) {
                location = "Washroom - " + detailLocation;
            } else {
                String subType = spinnerSubLocation.getSelectedItem().toString();
                location = mainType + " (" + subType + ") - " + detailLocation;
            }

            String category = spinnerCategory.getSelectedItem().toString();

            if (name.isEmpty()) {
                edtName.requestFocus();
                edtName.setError("Enter Your Name");
            } else if (detailLocation.isEmpty()) {
                edtDynamicLocation.requestFocus();
                edtDynamicLocation.setError("Enter Location / Room Number");
            } else {
                int selectedId = radioGroupSeverity.getCheckedRadioButtonId();
                if (selectedId == -1) {
                    Toast.makeText(StudentFormActivity.this, "Please select severity level", Toast.LENGTH_SHORT).show();
                    return;
                }
                RadioButton selectedRadioButton = findViewById(selectedId);
                String severity = selectedRadioButton.getText().toString();

                boolean isInserted = myDb.insertData(name, location, category, severity, imageUriString);
                if (isInserted) {
                    Toast.makeText(StudentFormActivity.this, "Student Complaint Submitted Successfully!", Toast.LENGTH_LONG).show();
                    edtName.setText("");
                    edtDynamicLocation.setText("");
                    imgComplaintPreview.setImageURI(null);
                    imageUriString = "";
                } else {
                    Toast.makeText(StudentFormActivity.this, "Error Submitting Complaint", Toast.LENGTH_LONG).show();
                }
            }
        });

        btnViewDashboard.setOnClickListener(v -> {
            Intent intent = new Intent(StudentFormActivity.this, DashboardActivity.class);
            intent.putExtra("USER_ROLE", "Student");
            startActivity(intent);
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri imageUri = data.getData();
            imageUriString = imageUri.toString();
            imgComplaintPreview.setImageURI(imageUri);
        }
    }
}