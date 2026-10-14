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
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class FacultyFormActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;

    EditText edtName, edtFacultyLocationDetail, edtPcNumber, edtFacultyIssueDescription;
    Spinner spinnerFacultyLocation, spinnerStaffItem, spinnerLabItem, spinnerCategory;
    TextView tvStaffItemLabel, tvLabItemLabel, tvCategoryLabel;
    RadioGroup radioGroupSeverity;
    Button btnSubmit, btnViewDashboard, btnSelectImage;
    ImageView imgComplaintPreview;
    DatabaseHelper myDb;

    String imageUriString = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faculty_form);

        myDb = new DatabaseHelper(this);

        edtName = findViewById(R.id.edtFacultyName);
        edtFacultyLocationDetail = findViewById(R.id.edtFacultyLocationDetail);
        edtPcNumber = findViewById(R.id.edtPcNumber);
        edtFacultyIssueDescription = findViewById(R.id.edtFacultyIssueDescription);
        spinnerFacultyLocation = findViewById(R.id.spinnerFacultyLocation);
        spinnerStaffItem = findViewById(R.id.spinnerStaffItem);
        spinnerLabItem = findViewById(R.id.spinnerLabItem);
        spinnerCategory = findViewById(R.id.spinnerCategory);
        tvStaffItemLabel = findViewById(R.id.tvStaffItemLabel);
        tvLabItemLabel = findViewById(R.id.tvLabItemLabel);
        tvCategoryLabel = findViewById(R.id.tvCategoryLabel);
        radioGroupSeverity = findViewById(R.id.radioGroupSeverity);
        btnSubmit = findViewById(R.id.btnSubmitComplaint);
        btnViewDashboard = findViewById(R.id.btnViewDashboard);
        btnSelectImage = findViewById(R.id.btnSelectImage);
        imgComplaintPreview = findViewById(R.id.imgComplaintPreview);

        String[] facultyLocations = {"Staff Room", "Lecture Room (LR)", "Lab", "Washroom"};
        ArrayAdapter<String> locAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, facultyLocations);
        spinnerFacultyLocation.setAdapter(locAdapter);

        String[] staffItems = {"PC", "Chair", "AC", "WIFI", "FAN", "light", "switch", "Other"};
        ArrayAdapter<String> staffAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, staffItems);
        spinnerStaffItem.setAdapter(staffAdapter);

        String[] labItems = {"PC", "wifi", "AC/Fans", "smartboard", "switch", "chair", "Other"};
        ArrayAdapter<String> labAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, labItems);
        spinnerLabItem.setAdapter(labAdapter);

        // LR Categories with "Other" included
        String[] lrCategories = {"Fan / AC", "Light / Switch", "Wi-Fi Network", "Smartboard / Projector", "Other"};
        ArrayAdapter<String> catAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, lrCategories);
        spinnerCategory.setAdapter(catAdapter);

        spinnerFacultyLocation.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedLoc = parent.getItemAtPosition(position).toString();

                tvStaffItemLabel.setVisibility(View.GONE);
                spinnerStaffItem.setVisibility(View.GONE);
                tvLabItemLabel.setVisibility(View.GONE);
                spinnerLabItem.setVisibility(View.GONE);
                edtPcNumber.setVisibility(View.GONE);
                tvCategoryLabel.setVisibility(View.GONE);
                spinnerCategory.setVisibility(View.GONE);
                edtFacultyIssueDescription.setVisibility(View.GONE);
                edtFacultyLocationDetail.setVisibility(View.VISIBLE);

                if (selectedLoc.equals("Staff Room")) {
                    tvStaffItemLabel.setVisibility(View.VISIBLE);
                    spinnerStaffItem.setVisibility(View.VISIBLE);
                    edtFacultyLocationDetail.setHint("Enter Staff Room Location / Number");
                } else if (selectedLoc.equals("Lecture Room (LR)")) {
                    edtFacultyLocationDetail.setHint("Enter LR Room Number");
                    tvCategoryLabel.setVisibility(View.VISIBLE);
                    spinnerCategory.setVisibility(View.VISIBLE);
                } else if (selectedLoc.equals("Lab")) {
                    tvLabItemLabel.setVisibility(View.VISIBLE);
                    spinnerLabItem.setVisibility(View.VISIBLE);
                    edtFacultyLocationDetail.setHint("Enter Lab Location / Number");
                } else if (selectedLoc.equals("Washroom")) {
                    edtFacultyLocationDetail.setHint("Enter Washroom Location");
                    edtFacultyIssueDescription.setVisibility(View.VISIBLE);
                    edtFacultyIssueDescription.setHint("Describe washroom issue...");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        spinnerStaffItem.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String item = parent.getItemAtPosition(position).toString();
                if (item.equals("Other")) {
                    edtFacultyIssueDescription.setVisibility(View.VISIBLE);
                    edtFacultyIssueDescription.setHint("Specify staff room issue...");
                } else {
                    if (!spinnerFacultyLocation.getSelectedItem().toString().equals("Washroom")) {
                        edtFacultyIssueDescription.setVisibility(View.GONE);
                    }
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        spinnerLabItem.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String assetType = parent.getItemAtPosition(position).toString();
                if (assetType.equals("PC")) {
                    edtPcNumber.setVisibility(View.VISIBLE);
                    edtFacultyIssueDescription.setVisibility(View.GONE);
                } else if (assetType.equals("Other")) {
                    edtPcNumber.setVisibility(View.GONE);
                    edtFacultyIssueDescription.setVisibility(View.VISIBLE);
                    edtFacultyIssueDescription.setHint("Specify lab issue...");
                } else {
                    edtPcNumber.setVisibility(View.GONE);
                    edtFacultyIssueDescription.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        spinnerCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String cat = parent.getItemAtPosition(position).toString();
                if (cat.equals("Other")) {
                    edtFacultyIssueDescription.setVisibility(View.VISIBLE);
                    edtFacultyIssueDescription.setHint("Specify LR issue...");
                } else {
                    edtFacultyIssueDescription.setVisibility(View.GONE);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        btnSelectImage.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent, PICK_IMAGE_REQUEST);
        });

        btnSubmit.setOnClickListener(v -> {
            String name = edtName.getText().toString().trim();
            String locType = spinnerFacultyLocation.getSelectedItem().toString();
            String locationDetail = edtFacultyLocationDetail.getText().toString().trim();

            String category = "General Maintenance";
            if (locType.equals("Lecture Room (LR)")) {
                category = spinnerCategory.getSelectedItem().toString();
                if (category.equals("Other")) {
                    category = "Other: " + edtFacultyIssueDescription.getText().toString().trim();
                }
            } else if (locType.equals("Staff Room")) {
                category = "Staff Room Item: " + spinnerStaffItem.getSelectedItem().toString();
                if (spinnerStaffItem.getSelectedItem().toString().equals("Other")) {
                    category = "Other: " + edtFacultyIssueDescription.getText().toString().trim();
                }
            } else if (locType.equals("Lab")) {
                String asset = spinnerLabItem.getSelectedItem().toString();
                if (asset.equals("PC")) {
                    category = "Lab PC #" + edtPcNumber.getText().toString().trim();
                } else if (asset.equals("Other")) {
                    category = "Other: " + edtFacultyIssueDescription.getText().toString().trim();
                } else {
                    category = "Lab Asset: " + asset;
                }
            } else if (locType.equals("Washroom")) {
                category = "Washroom Issue: " + edtFacultyIssueDescription.getText().toString().trim();
            }

            String fullLocation = "";
            if (locType.equals("Staff Room")) {
                fullLocation = "Staff Room - " + locationDetail;
            } else if (locType.equals("Lecture Room (LR)")) {
                fullLocation = "Lecture Room (LR) - Room " + locationDetail;
            } else if (locType.equals("Lab")) {
                fullLocation = "Lab - " + locationDetail;
            } else {
                fullLocation = "Washroom - " + locationDetail;
            }

            if (name.isEmpty()) {
                edtName.requestFocus();
                edtName.setError("Enter Faculty Name");
            } else if (locationDetail.isEmpty()) {
                edtFacultyLocationDetail.requestFocus();
                edtFacultyLocationDetail.setError("Enter location details");
            } else {
                int selectedId = radioGroupSeverity.getCheckedRadioButtonId();
                if (selectedId == -1) {
                    Toast.makeText(FacultyFormActivity.this, "Please select severity level", Toast.LENGTH_SHORT).show();
                    return;
                }
                RadioButton selectedRadioButton = findViewById(selectedId);
                String severity = selectedRadioButton.getText().toString();

                boolean isInserted = myDb.insertData(name, fullLocation, category, severity, imageUriString);
                if (isInserted) {
                    Toast.makeText(FacultyFormActivity.this, "Faculty Complaint Submitted Successfully!", Toast.LENGTH_LONG).show();
                    edtName.setText("");
                    edtFacultyLocationDetail.setText("");
                    edtPcNumber.setText("");
                    edtFacultyIssueDescription.setText("");
                    imgComplaintPreview.setImageURI(null);
                    imageUriString = "";
                } else {
                    Toast.makeText(FacultyFormActivity.this, "Error Submitting Complaint", Toast.LENGTH_LONG).show();
                }
            }
        });

        btnViewDashboard.setOnClickListener(v -> {
            Intent intent = new Intent(FacultyFormActivity.this, DashboardActivity.class);
            intent.putExtra("USER_ROLE", "Faculty");
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