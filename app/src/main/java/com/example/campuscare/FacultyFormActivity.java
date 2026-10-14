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

    EditText edtName, edtFacultyLocationDetail, edtPcNumber;
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

        // Faculty Locations
        String[] facultyLocations = {"Staff Room", "Lecture Room (LR)", "Lab", "Washroom"};
        ArrayAdapter<String> locAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, facultyLocations);
        spinnerFacultyLocation.setAdapter(locAdapter);

        // Staff Room items
        String[] staffItems = {"PC", "Chair", "AC", "WIFI", "FAN", "light", "switch"};
        ArrayAdapter<String> staffAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, staffItems);
        spinnerStaffItem.setAdapter(staffAdapter);

        // Lab items
        String[] labItems = {"PC", "wifi", "AC/Fans", "smartboard", "switch", "chair"};
        ArrayAdapter<String> labAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, labItems);
        spinnerLabItem.setAdapter(labAdapter);

        // LR Categories (No Drinking Water)
        String[] lrCategories = {"Fan / AC", "Light / Switch", "Wi-Fi Network", "Smartboard / Projector"};
        ArrayAdapter<String> catAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, lrCategories);
        spinnerCategory.setAdapter(catAdapter);

        spinnerFacultyLocation.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedLoc = parent.getItemAtPosition(position).toString();

                // Reset visibility defaults
                tvStaffItemLabel.setVisibility(View.GONE);
                spinnerStaffItem.setVisibility(View.GONE);
                tvLabItemLabel.setVisibility(View.GONE);
                spinnerLabItem.setVisibility(View.GONE);
                edtPcNumber.setVisibility(View.GONE);
                tvCategoryLabel.setVisibility(View.GONE);
                spinnerCategory.setVisibility(View.GONE);
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
                } else {
                    edtPcNumber.setVisibility(View.GONE);
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

            // Category is only required/selected for Lecture Room (LR), default to "General Maintenance" for others
            String category = locType.equals("Lecture Room (LR)") ? spinnerCategory.getSelectedItem().toString() : "General Maintenance";

            String fullLocation = "";
            if (locType.equals("Staff Room")) {
                String item = spinnerStaffItem.getSelectedItem().toString();
                fullLocation = "Staff Room (" + item + ") - " + locationDetail;
            } else if (locType.equals("Lecture Room (LR)")) {
                fullLocation = "Lecture Room (LR) - Room " + locationDetail;
            } else if (locType.equals("Lab")) {
                String asset = spinnerLabItem.getSelectedItem().toString();
                if (asset.equals("PC")) {
                    String pcNum = edtPcNumber.getText().toString().trim();
                    fullLocation = "Lab - " + locationDetail + " (PC #" + pcNum + ")";
                } else {
                    fullLocation = "Lab - " + locationDetail + " (" + asset + ")";
                }
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