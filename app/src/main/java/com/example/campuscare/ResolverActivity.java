package com.example.campuscare;

import android.app.AlertDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

public class ResolverActivity extends AppCompatActivity {

    private static final int GALLERY_REQUEST_CODE = 101;
    private static final int CAMERA_REQUEST_CODE = 102;

    RecyclerView recyclerView;
    TextView tvEmpty;
    DatabaseHelper myDb;
    ArrayList<ComplaintModel> complaintList;
    ComplaintAdapter complaintAdapter;
    String currentUsername = "resolver1";

    ComplaintModel selectedComplaintForProof;
    int selectedProofPosition = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resolver);

        if (getIntent().hasExtra("USERNAME")) {
            currentUsername = getIntent().getStringExtra("USERNAME");
        }

        recyclerView = findViewById(R.id.recyclerViewResolver);
        tvEmpty = findViewById(R.id.tvEmpty);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        myDb = new DatabaseHelper(this);
        complaintList = new ArrayList<>();

        loadDataFromDB();

        complaintAdapter = new ComplaintAdapter(complaintList, "Resolver", (complaint, position) -> {
            selectedComplaintForProof = complaint;
            selectedProofPosition = position;
            showImagePickerDialog();
        });
        recyclerView.setAdapter(complaintAdapter);

        LinearLayout tab2 = findViewById(R.id.navTab2);
        if (tab2 != null) tab2.setVisibility(View.GONE);

        TextView txtTab1 = findViewById(R.id.txtTab1);
        TextView txtTab3 = findViewById(R.id.txtTab3);
        if (txtTab1 != null) txtTab1.setText("Tasks");
        if (txtTab3 != null) txtTab3.setText("Profile");

        findViewById(R.id.navTab1).setOnClickListener(v -> {});
        findViewById(R.id.navTab3).setOnClickListener(v -> {
            Intent intent = new Intent(this, ProfileActivity.class);
            intent.putExtra("USERNAME", currentUsername);
            intent.putExtra("USER_ROLE", "Resolver");
            startActivity(intent);
            finish();
        });
    }

    private void showImagePickerDialog() {
        CharSequence[] options = {"Take Photo", "Choose from Gallery", "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Upload Resolution Proof Photo");
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
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && selectedComplaintForProof != null && selectedProofPosition != -1) {
            String imageUriString = "";
            if (requestCode == GALLERY_REQUEST_CODE && data != null && data.getData() != null) {
                Uri imageUri = data.getData();
                imageUriString = imageUri.toString();
            } else if (requestCode == CAMERA_REQUEST_CODE && data != null && data.getExtras() != null) {
                Bitmap photo = (Bitmap) data.getExtras().get("data");
                if (photo != null) {
                    Uri tempUri = getImageUri(photo);
                    if (tempUri != null) {
                        imageUriString = tempUri.toString();
                    }
                }
            }

            if (!imageUriString.isEmpty()) {
                selectedComplaintForProof.setResolutionImageUri(imageUriString);
                complaintAdapter.notifyItemChanged(selectedProofPosition);
                Toast.makeText(this, "Proof photo attached! Now click Mark Done.", Toast.LENGTH_LONG).show();
            }
        }
    }

    private Uri getImageUri(Bitmap inImage) {
        String path = MediaStore.Images.Media.insertImage(getContentResolver(), inImage, "ResolutionProof", null);
        return path != null ? Uri.parse(path) : null;
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
        if (cursor == null || cursor.getCount() == 0) {
            if (tvEmpty != null) tvEmpty.setVisibility(View.VISIBLE);
            if (recyclerView != null) recyclerView.setVisibility(View.GONE);
            if (cursor != null) cursor.close();
            return;
        }

        if (tvEmpty != null) tvEmpty.setVisibility(View.GONE);
        if (recyclerView != null) recyclerView.setVisibility(View.VISIBLE);

        while (cursor.moveToNext()) {
            int id = cursor.getInt(0);
            // Index 1: USERNAME, Index 2: NAME
            String name = cursor.getString(2);
            String location = cursor.getString(3);
            String category = cursor.getString(4);
            String severity = cursor.getString(5);
            String imageUri = cursor.getString(6);
            String status = cursor.getString(7); // Correct index for STATUS
            String resImageUri = cursor.getString(8); // Correct index for RESOLUTION_IMAGE

            ComplaintModel complaint = new ComplaintModel(id, name, location, category, severity, imageUri, status, resImageUri);
            complaintList.add(complaint);
        }
        cursor.close();
    }
}