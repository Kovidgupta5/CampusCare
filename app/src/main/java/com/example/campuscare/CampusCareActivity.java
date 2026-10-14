package com.example.campuscare;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
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

public class CampusCareActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;

    EditText edtName, edtLocation;
    Spinner spinnerCategory;
    RadioGroup radioGroupSeverity;
    Button btnSubmit, btnViewDashboard, btnSelectImage;
    ImageView imgComplaintPreview;
    DatabaseHelper myDb;

    String imageUriString = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_campus_care);

        myDb = new DatabaseHelper(this);

        edtName = findViewById(R.id.edtReporterName);
        edtLocation = findViewById(R.id.edtLocation);
        spinnerCategory = findViewById(R.id.spinnerCategory);
        radioGroupSeverity = findViewById(R.id.radioGroupSeverity);
        btnSubmit = findViewById(R.id.btnSubmitComplaint);
        btnViewDashboard = findViewById(R.id.btnViewDashboard);
        btnSelectImage = findViewById(R.id.btnSelectImage);
        imgComplaintPreview = findViewById(R.id.imgComplaintPreview);

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

        btnSelectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, PICK_IMAGE_REQUEST);
            }
        });

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
                        Toast.makeText(CampusCareActivity.this, "Please select a severity level", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    RadioButton selectedRadioButton = findViewById(selectedId);
                    String severity = selectedRadioButton.getText().toString();
                    String category = spinnerCategory.getSelectedItem().toString();

                    boolean isInserted = myDb.insertData(name, location, category, severity, imageUriString);
                    if (isInserted) {
                        Toast.makeText(CampusCareActivity.this, "Complaint Saved Successfully!", Toast.LENGTH_LONG).show();
                        edtName.setText("");
                        edtLocation.setText("");
                        imgComplaintPreview.setImageURI(null);
                        imageUriString = "";
                    } else {
                        Toast.makeText(CampusCareActivity.this, "Error Saving Complaint", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });

        btnViewDashboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CampusCareActivity.this, DashboardActivity.class);
                startActivity(intent);
            }
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