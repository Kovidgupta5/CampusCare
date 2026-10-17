package com.example.campuscare;

import android.app.AlertDialog;

import android.content.Intent;

import android.graphics.Bitmap;

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

public class StudentFormActivity extends AppCompatActivity {

    private static final int GALLERY_REQUEST_CODE = 1;

    private static final int CAMERA_REQUEST_CODE = 2;

    EditText edtName, edtDynamicLocation, edtFurnitureType, edtIssueDescription;

    Spinner spinnerLocationType, spinnerSubLocation, spinnerCategory;

    TextView tvSubLocLabel, tvCategoryLabel;

    RadioGroup radioGroupSeverity;

    Button btnSubmit, btnSelectImage;

    ImageView imgComplaintPreview;

    DatabaseHelper myDb;

    String imageUriString = "";

    String currentUsername = "", userRole = "Student";

    @Override

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_student_form);

        myDb = new DatabaseHelper(this);

        if (getIntent().hasExtra("USERNAME")) {

            currentUsername = getIntent().getStringExtra("USERNAME");

        }

        if (getIntent().hasExtra("USER_ROLE")) {

            userRole = getIntent().getStringExtra("USER_ROLE");

        }

        edtName = findViewById(R.id.edtStudentName);

        edtDynamicLocation = findViewById(R.id.edtDynamicLocation);

        edtFurnitureType = findViewById(R.id.edtFurnitureType);

        edtIssueDescription = findViewById(R.id.edtIssueDescription);

        spinnerLocationType = findViewById(R.id.spinnerLocationType);

        spinnerSubLocation = findViewById(R.id.spinnerSubLocation);

        spinnerCategory = findViewById(R.id.spinnerCategory);

        tvSubLocLabel = findViewById(R.id.tvSubLocLabel);

        tvCategoryLabel = findViewById(R.id.tvCategoryLabel);

        radioGroupSeverity = findViewById(R.id.radioGroupSeverity);

        btnSubmit = findViewById(R.id.btnSubmitComplaint);

        btnSelectImage = findViewById(R.id.btnSelectImage);

        imgComplaintPreview = findViewById(R.id.imgComplaintPreview);

        if (!currentUsername.isEmpty()) {

            edtName.setText(currentUsername);

        }

        // Bottom Navigation Setup

        TextView txtTab1 = findViewById(R.id.txtTab1);

        TextView txtTab2 = findViewById(R.id.txtTab2);

        TextView txtTab3 = findViewById(R.id.txtTab3);

        txtTab1.setText("File");

        txtTab2.setText("View");

        txtTab3.setText("Profile");

        findViewById(R.id.navTab1).setOnClickListener(v -> {});

        findViewById(R.id.navTab2).setOnClickListener(v -> {

            Intent intent = new Intent(this, DashboardActivity.class);

            intent.putExtra("USERNAME", currentUsername);

            intent.putExtra("USER_ROLE", userRole);

            startActivity(intent);

            finish();

        });

        findViewById(R.id.navTab3).setOnClickListener(v -> {

            Intent intent = new Intent(this, ProfileActivity.class);

            intent.putExtra("USERNAME", currentUsername);

            intent.putExtra("USER_ROLE", userRole);

            startActivity(intent);

            finish();

        });

        String[] mainLocations = {"Academic Block", "Hostel", "Campus"};

        ArrayAdapter<String> mainAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, mainLocations);

        spinnerLocationType.setAdapter(mainAdapter);

        spinnerLocationType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override

            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                String selectedType = parent.getItemAtPosition(position).toString();

                if (selectedType.equals("Academic Block")) {

                    String[] academicSubs = {"Washroom", "Lecture Room (LR)", "Lab"};

                    ArrayAdapter<String> subAdapter = new ArrayAdapter<>(StudentFormActivity.this, android.R.layout.simple_spinner_dropdown_item, academicSubs);

                    spinnerSubLocation.setAdapter(subAdapter);

                    tvSubLocLabel.setVisibility(View.VISIBLE);

                    spinnerSubLocation.setVisibility(View.VISIBLE);

                    edtDynamicLocation.setVisibility(View.VISIBLE);

                    edtDynamicLocation.setHint("Enter Location Details");

                    tvCategoryLabel.setVisibility(View.VISIBLE);

                    spinnerCategory.setVisibility(View.VISIBLE);

                    edtIssueDescription.setVisibility(View.GONE);

                } else if (selectedType.equals("Hostel")) {

                    String[] hostelSubs = {"Hostel Room", "Mess / Dining Hall", "TV Room", "Hostel Corridor"};

                    ArrayAdapter<String> subAdapter = new ArrayAdapter<>(StudentFormActivity.this, android.R.layout.simple_spinner_dropdown_item, hostelSubs);

                    spinnerSubLocation.setAdapter(subAdapter);

                    tvSubLocLabel.setVisibility(View.VISIBLE);

                    spinnerSubLocation.setVisibility(View.VISIBLE);

                    edtDynamicLocation.setVisibility(View.VISIBLE);

                    edtDynamicLocation.setHint("Enter Room Number / Location Details");

                    tvCategoryLabel.setVisibility(View.VISIBLE);

                    spinnerCategory.setVisibility(View.VISIBLE);

                    edtIssueDescription.setVisibility(View.GONE);

                } else {

                    tvSubLocLabel.setVisibility(View.GONE);

                    spinnerSubLocation.setVisibility(View.GONE);

                    tvCategoryLabel.setVisibility(View.GONE);

                    spinnerCategory.setVisibility(View.GONE);

                    edtDynamicLocation.setVisibility(View.VISIBLE);

                    edtDynamicLocation.setHint("Enter Campus Area / Landmark");

                    edtIssueDescription.setVisibility(View.VISIBLE);

                    edtIssueDescription.setHint("Describe your campus issue...");

                }

                updateCategories();

            }

            @Override

            public void onNothingSelected(AdapterView<?> parent) {}

        });

        spinnerSubLocation.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override

            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                String mainType = spinnerLocationType.getSelectedItem().toString();

                String subLoc = parent.getItemAtPosition(position).toString();

                if (mainType.equals("Academic Block")) {

                    if (subLoc.equals("Washroom")) {

                        edtDynamicLocation.setHint("Enter Washroom Location");

                    } else if (subLoc.equals("Lecture Room (LR)")) {

                        edtDynamicLocation.setHint("Enter LR Room Number");

                    } else if (subLoc.equals("Lab")) {

                        edtDynamicLocation.setHint("Enter Lab Location / Number");

                    }

                } else if (mainType.equals("Hostel")) {

                    if (subLoc.equals("Hostel Room")) {

                        edtDynamicLocation.setHint("Enter Room Number");

                    } else {

                        edtDynamicLocation.setHint("Enter Specific Location Details");

                    }

                }

                updateCategories();

            }

            @Override

            public void onNothingSelected(AdapterView<?> parent) {}

        });

        spinnerCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override

            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                String selectedCategory = parent.getItemAtPosition(position).toString();

                if (selectedCategory.equals("Furniture")) {

                    edtFurnitureType.setVisibility(View.VISIBLE);

                    edtIssueDescription.setVisibility(View.GONE);

                } else if (selectedCategory.equals("Other")) {

                    edtFurnitureType.setVisibility(View.GONE);

                    edtIssueDescription.setVisibility(View.VISIBLE);

                    edtIssueDescription.setHint("Please specify your issue...");

                } else {

                    edtFurnitureType.setVisibility(View.GONE);

                    if (!spinnerLocationType.getSelectedItem().toString().equals("Campus")) {

                        edtIssueDescription.setVisibility(View.GONE);

                    }

                }

            }

            @Override

            public void onNothingSelected(AdapterView<?> parent) {}

        });

        btnSelectImage.setOnClickListener(v -> {

            CharSequence[] options = {"Take Photo", "Choose from Gallery", "Cancel"};

            AlertDialog.Builder builder = new AlertDialog.Builder(StudentFormActivity.this);

            builder.setTitle("Add Issue Photo");

            builder.setItems(options, (dialog, item) -> {

                if (options[item].equals("Take Photo")) {

                    Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                    startActivityForResult(takePictureIntent, CAMERA_REQUEST_CODE);

                } else if (options[item].equals("Choose from Gallery")) {

                    Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                    startActivityForResult(intent, GALLERY_REQUEST_CODE);

                } else if (options[item].equals("Cancel")) {

                    dialog.dismiss();

                }

            });

            builder.show();

        });

        btnSubmit.setOnClickListener(v -> {

            String name = edtName.getText().toString().trim();

            String mainType = spinnerLocationType.getSelectedItem().toString();

            String detailLocation = edtDynamicLocation.getText().toString().trim();

            String location;

            if (mainType.equals("Campus")) {

                location = "Campus - " + detailLocation;

            } else {

                String subType = spinnerSubLocation.getSelectedItem() != null ? spinnerSubLocation.getSelectedItem().toString() : "";

                location = mainType + " (" + subType + ") - " + detailLocation;

            }

            String category;

            if (mainType.equals("Campus")) {

                category = "Campus Issue: " + edtIssueDescription.getText().toString().trim();

            } else {

                category = spinnerCategory.getSelectedItem().toString();

                if (category.equals("Furniture")) {

                    category = "Furniture (" + edtFurnitureType.getText().toString().trim() + ")";

                } else if (category.equals("Other")) {

                    category = "Other: " + edtIssueDescription.getText().toString().trim();

                }

            }

            if (name.isEmpty()) {

                edtName.requestFocus();

                edtName.setError("Enter Your Name");

            } else if (detailLocation.isEmpty()) {

                edtDynamicLocation.requestFocus();

                edtDynamicLocation.setError("Enter Location / Room Number");

            } else if (mainType.equals("Campus") && edtIssueDescription.getText().toString().trim().isEmpty()) {

                edtIssueDescription.requestFocus();

                edtIssueDescription.setError("Please describe your issue");

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

                    edtName.setText(currentUsername);

                    edtDynamicLocation.setText("");

                    edtFurnitureType.setText("");

                    edtIssueDescription.setText("");

                    imgComplaintPreview.setImageURI(null);

                    imageUriString = "";

                } else {

                    Toast.makeText(StudentFormActivity.this, "Error Submitting Complaint", Toast.LENGTH_LONG).show();

                }

            }

        });

    }

    private void updateCategories() {

        if (spinnerLocationType.getSelectedItem() == null) return;

        String mainType = spinnerLocationType.getSelectedItem().toString();

        String subType = spinnerSubLocation.getSelectedItem() != null ? spinnerSubLocation.getSelectedItem().toString() : "";

        if (mainType.equals("Campus")) return;

        ArrayAdapter<String> catAdapter;

        if (mainType.equals("Academic Block")) {

            if (subType.equals("Washroom")) {

                String[] washroomCategories = {"Plumbing / Leakage", "Cleanliness / Hygiene", "Fixture Damage", "Other"};

                catAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, washroomCategories);

            } else {

                String[] academicCategories = {"Fan / AC", "Light / Switch", "Wi-Fi Network", "Smartboard / Projector", "Drinking Water", "Other"};

                catAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, academicCategories);

            }

        } else if (mainType.equals("Hostel")) {

            if (subType.equals("Hostel Room")) {

                String[] hostelRoomCategories = {"Fan / AC", "Light / Switch", "Wi-Fi Network", "Drinking Water", "Furniture", "Other"};

                catAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, hostelRoomCategories);

            } else if (subType.equals("TV Room") || subType.equals("Mess / Dining Hall")) {

                String[] hostelCategoriesWithTV = {"Fan / AC", "Light / Switch", "Wi-Fi Network", "Drinking Water", "TV", "Other"};

                catAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, hostelCategoriesWithTV);

            } else if (subType.equals("Hostel Corridor")) {

                String[] corridorCategories = {"Light / Switch", "Wi-Fi Network", "Drinking Water", "Other"};

                catAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, corridorCategories);

            } else {

                String[] hostelCategories = {"Fan / AC", "Light / Switch", "Wi-Fi Network", "Drinking Water", "Other"};

                catAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, hostelCategories);

            }

        } else {

            catAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, new String[]{});

        }

        spinnerCategory.setAdapter(catAdapter);

    }

    @Override

    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {

            if (requestCode == GALLERY_REQUEST_CODE && data != null && data.getData() != null) {

                Uri imageUri = data.getData();

                imageUriString = imageUri.toString();

                imgComplaintPreview.setImageURI(imageUri);

            } else if (requestCode == CAMERA_REQUEST_CODE && data != null && data.getExtras() != null) {

                Bitmap photo = (Bitmap) data.getExtras().get("data");

                if (photo != null) {

                    Uri tempUri = getImageUri(photo);

                    if (tempUri != null) {

                        imageUriString = tempUri.toString();

                        imgComplaintPreview.setImageURI(tempUri);

                    }

                }

            }

        }

    }

    private Uri getImageUri(Bitmap inImage) {

        String path = MediaStore.Images.Media.insertImage(getContentResolver(), inImage, "CampusCare_Photo", null);

        return path != null ? Uri.parse(path) : null;

    }

}
