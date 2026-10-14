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

    EditText edtName;
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
        spinnerLocationType = findViewById(R.id.spinnerLocationType);
        spinnerSubLocation = findViewById(R.id.spinnerSubLocation);
        spinnerCategory = findViewById(R.id.spinnerCategory);
        radioGroupSeverity = findViewById(R.id.radioGroupSeverity);
        btnSubmit = findViewById(R.id.btnSubmitComplaint);
        btnViewDashboard = findViewById(R.id.btnViewDashboard);
        btnSelectImage = findViewById(R.id.btnSelectImage);
        imgComplaintPreview = findViewById(R.id.imgComplaintPreview);

        String[] mainLocations = {"Academic Block", "Hostel"};
        ArrayAdapter<String> mainAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, mainLocations);
        spinnerLocationType.setAdapter(mainAdapter);

        spinnerLocationType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedType = parent.getItemAtPosition(position).toString();
                ArrayAdapter<String> subAdapter;
                if (selectedType.equals("Academic Block")) {
                    String[] academicSubs = {"Campus", "Lecture Room (LR)", "Lab"};
                    subAdapter = new ArrayAdapter<>(StudentFormActivity.this, android.R.layout.simple_spinner_dropdown_item, academicSubs);
                } else {
                    String[] hostelSubs = {"Hostel Room", "Mess / Dining Hall", "Common Room", "Hostel Corridor"};
                    subAdapter = new ArrayAdapter<>(StudentFormActivity.this, android.R.layout.simple_spinner_dropdown_item, hostelSubs);
                }
                spinnerSubLocation.setAdapter(subAdapter);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        String[] categories = {"Fan / AC", "Light / Switch", "Wi-Fi Network", "Smartboard / Projector", "Washroom Cleanliness"};
        ArrayAdapter<String> catAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, categories);
        spinnerCategory.setAdapter(catAdapter);

        btnSelectImage.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent, PICK_IMAGE_REQUEST);
        });

        btnSubmit.setOnClickListener(v -> {
            String name = edtName.getText().toString().trim();
            String location = spinnerLocationType.getSelectedItem().toString() + " - " + spinnerSubLocation.getSelectedItem().toString();
            String category = spinnerCategory.getSelectedItem().toString();

            if (name.isEmpty()) {
                edtName.requestFocus();
                edtName.setError("Enter Your Name");
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