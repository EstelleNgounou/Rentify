package com.example.rentify1;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class AddItemActivity extends AppCompatActivity {

    private DatabaseReference categoriesRef; // Realtime Database reference for categories
    private FirebaseFirestore firestore; // Firestore reference for items
    private StorageReference storageRef; // Firebase Storage reference
    private FirebaseAuth auth; // Firebase Authentication instance

    private static final int PICK_IMAGES_REQUEST = 1;
    private List<Uri> imageUris = new ArrayList<>();
    private List<String> categories = new ArrayList<>();
    private ArrayAdapter<String> categoryAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);

        // Initialize Firebase
        categoriesRef = FirebaseDatabase.getInstance().getReference("categories"); // Realtime Database reference for categories
        firestore = FirebaseFirestore.getInstance(); // Firestore reference for items
        storageRef = FirebaseStorage.getInstance().getReference("item_images");
        auth = FirebaseAuth.getInstance();

        // Initialize UI elements
        EditText nameInput = findViewById(R.id.item_name);
        EditText descriptionInput = findViewById(R.id.item_description);
        EditText feeInput = findViewById(R.id.item_fee);
        EditText timePeriodInput = findViewById(R.id.item_time_period);
        Spinner categorySpinner = findViewById(R.id.item_category);
        Button addItemButton = findViewById(R.id.btn_add_item);
        Button selectImagesButton = findViewById(R.id.btn_select_images);

        // Set up category spinner
        categoryAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categories);
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinner.setAdapter(categoryAdapter);

        // Fetch categories from Realtime Database
        fetchCategoriesFromRealtimeDatabase();

        // Button to select images
        selectImagesButton.setOnClickListener(v -> openFileChooser());

        // Button to add item
        addItemButton.setOnClickListener(view -> {
            String name = nameInput.getText().toString().trim();
            String description = descriptionInput.getText().toString().trim();
            String feeString = feeInput.getText().toString().trim();
            String timePeriod = timePeriodInput.getText().toString().trim();
            String category = categorySpinner.getSelectedItem().toString();

            // Validate fields
            if (!validateFields(nameInput, descriptionInput, feeInput, timePeriodInput)) return;

            double fee = Double.parseDouble(feeString);
            String lessorId = auth.getCurrentUser().getUid();

            if (!imageUris.isEmpty()) {
                uploadImagesAndSaveItem(name, description, fee, timePeriod, category, lessorId);
            } else {
                saveItemToFirestore(name, description, fee, timePeriod, category, lessorId, new ArrayList<>());
            }
        });
    }

    private void fetchCategoriesFromRealtimeDatabase() {
        categoriesRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // Iterate through the children of categoriesRef to retrieve category names
                for (DataSnapshot categorySnapshot : snapshot.getChildren()) {
                    Category category = categorySnapshot.getValue(Category.class);
                    if (category != null) {
                        categories.add(category.getName());  // Add category name to the list
                    }
                }
                categoryAdapter.notifyDataSetChanged(); // Update spinner
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(AddItemActivity.this, "Failed to load categories.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void openFileChooser() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        startActivityForResult(intent, PICK_IMAGES_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGES_REQUEST && resultCode == RESULT_OK) {
            imageUris.clear();
            if (data.getClipData() != null) {
                int count = data.getClipData().getItemCount();
                for (int i = 0; i < count; i++) {
                    imageUris.add(data.getClipData().getItemAt(i).getUri());
                }
            } else if (data.getData() != null) {
                imageUris.add(data.getData());
            }
            Toast.makeText(this, imageUris.size() + " images selected", Toast.LENGTH_SHORT).show();
        }
    }

    private void uploadImagesAndSaveItem(String name, String description, double fee, String timePeriod, String category, String lessorId) {
        List<String> imageUrls = new ArrayList<>();
        for (Uri imageUri : imageUris) {
            String uniqueFileName = UUID.randomUUID().toString();
            StorageReference imageRef = storageRef.child(uniqueFileName);

            imageRef.putFile(imageUri)
                    .addOnSuccessListener(taskSnapshot -> imageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                        imageUrls.add(uri.toString());
                        if (imageUrls.size() == imageUris.size()) {
                            saveItemToFirestore(name, description, fee, timePeriod, category, lessorId, imageUrls);
                        }
                    }))
                    .addOnFailureListener(e -> Toast.makeText(this, "Failed to upload an image.", Toast.LENGTH_SHORT).show());
        }
    }

    private void saveItemToFirestore(String name, String description, double fee, String timePeriod, String category, String lessorId, List<String> imageUrls) {
        String itemId = firestore.collection("items").document().getId(); // Generate unique document ID

        Item newItem = new Item(name, description, fee, timePeriod, category, lessorId, imageUrls);

        firestore.collection("items").document(itemId).set(newItem)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(this, "Item added successfully!", Toast.LENGTH_SHORT).show();
                    finish();
                })
                .addOnFailureListener(e -> Toast.makeText(this, "Failed to add item.", Toast.LENGTH_SHORT).show());
    }

    private boolean validateFields(EditText nameInput, EditText descriptionInput, EditText feeInput, EditText timePeriodInput) {
        if (nameInput.getText().toString().trim().isEmpty()) {
            nameInput.setError("Name is required!");
            nameInput.requestFocus();
            return false;
        }
        if (descriptionInput.getText().toString().trim().isEmpty()) {
            descriptionInput.setError("Description is required!");
            descriptionInput.requestFocus();
            return false;
        }
        String feeString = feeInput.getText().toString().trim();
        if (feeString.isEmpty()) {
            feeInput.setError("Fee is required!");
            feeInput.requestFocus();
            return false;
        }
        try {
            double fee = Double.parseDouble(feeString);
            if (fee <= 0) {
                feeInput.setError("Fee must be a positive number!");
                feeInput.requestFocus();
                return false;
            }
        } catch (NumberFormatException e) {
            feeInput.setError("Invalid fee! Enter a numeric value.");
            feeInput.requestFocus();
            return false;
        }
        if (timePeriodInput.getText().toString().trim().isEmpty()) {
            timePeriodInput.setError("Time period is required!");
            timePeriodInput.requestFocus();
            return false;
        }
        return true;
    }
}
