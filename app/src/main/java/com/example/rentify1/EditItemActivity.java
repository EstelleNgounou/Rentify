package com.example.rentify1;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class EditItemActivity extends AppCompatActivity {

    private FirebaseFirestore db; // Firestore for items
    private DatabaseReference categoriesRef; // Realtime Database for categories
    private String itemId;
    private Spinner categorySpinner; // Spinner for selecting category
    private ArrayList<String> categories = new ArrayList<>(); // List to hold categories
    private ArrayAdapter<String> categoryAdapter; // Adapter for spinner
    private EditText nameInput;
    private EditText descriptionInput;
    private EditText feeInput;
    private EditText timePeriodInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_item);

        db = FirebaseFirestore.getInstance();
        categoriesRef = FirebaseDatabase.getInstance().getReference("categories"); // Realtime Database reference

        // Get references to input fields
        EditText nameInput = findViewById(R.id.edit_item_name);
        EditText descriptionInput = findViewById(R.id.edit_item_description);
        EditText feeInput = findViewById(R.id.edit_item_fee);
        EditText timePeriodInput = findViewById(R.id.edit_item_time_period);
        categorySpinner = findViewById(R.id.spinner_category);
        Button updateButton = findViewById(R.id.btn_update_item);
        Button deleteButton = findViewById(R.id.btn_delete_item);

        // Get the item ID from intent extras
        itemId = getIntent().getStringExtra("ITEM_NAME"); // Retrieve the itemId passed from ListItemsActivity

        // Set up spinner adapter
        categoryAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categories);
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinner.setAdapter(categoryAdapter);

        // Fetch categories from Realtime Database
        fetchCategoriesFromRealtimeDatabase();

        // Fetch the item details from Firestore and populate the fields
        fetchItemDetails();

        // Update button functionality
        updateButton.setOnClickListener(view -> {
            // Validate fields before proceeding with update
            if (validateFields(nameInput, descriptionInput, feeInput, timePeriodInput)) {
                // Get the values from input fields
                String newName = nameInput.getText().toString().trim();
                String newDescription = descriptionInput.getText().toString().trim();
                double newFee = Double.parseDouble(feeInput.getText().toString().trim());
                String newTimePeriod = timePeriodInput.getText().toString().trim();
                String newCategory = categorySpinner.getSelectedItem().toString(); // Get the selected category

                // Query Firestore to find the document by its current name
                db.collection("items")
                        .whereEqualTo("name", itemId) // Find the document with the original name
                        .get()
                        .addOnSuccessListener(queryDocumentSnapshots -> {
                            if (!queryDocumentSnapshots.isEmpty()) {
                                for (DocumentSnapshot document : queryDocumentSnapshots) {
                                    String documentId = document.getId(); // Get the document ID
                                    // Perform the update
                                    db.collection("items").document(documentId)
                                            .update(
                                                    "name", newName,
                                                    "description", newDescription,
                                                    "fee", newFee,
                                                    "timePeriod", newTimePeriod,
                                                    "category", newCategory
                                            )
                                            .addOnSuccessListener(aVoid -> {
                                                Toast.makeText(this, "Item updated successfully!", Toast.LENGTH_SHORT).show();
                                                finish(); // Close the activity
                                            })
                                            .addOnFailureListener(e -> Toast.makeText(this, "Failed to update item.", Toast.LENGTH_SHORT).show());
                                }
                            } else {
                                Toast.makeText(this, "Item not found.", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .addOnFailureListener(e -> Toast.makeText(this, "Failed to fetch item.", Toast.LENGTH_SHORT).show());
            }
        });

        // Delete button functionality
        deleteButton.setOnClickListener(view -> {
            new AlertDialog.Builder(EditItemActivity.this)
                    .setMessage("Are you sure you want to delete this item?")
                    .setPositiveButton("Yes", (dialog, which) -> deleteItemByName(itemId))  // Proceed with deletion
                    .setNegativeButton("No", null)  // Do nothing if "No" is clicked
                    .show();  // Show the confirmation dialog
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
            public void onCancelled(DatabaseError error) {
                Toast.makeText(EditItemActivity.this, "Failed to load categories.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void fetchItemDetails() {
        // Fetch the item details from Firestore using item ID
        db.collection("items")
                .document(itemId)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        // Populate fields with the item details
                        String itemName = documentSnapshot.getString("name");
                        String itemDescription = documentSnapshot.getString("description");
                        double itemFee = documentSnapshot.getDouble("fee");
                        String itemTimePeriod = documentSnapshot.getString("timePeriod");
                        String itemCategory = documentSnapshot.getString("category");

                        // Set the values to the EditTexts
                        nameInput.setText(itemName);
                        descriptionInput.setText(itemDescription);
                        feeInput.setText(String.valueOf(itemFee));
                        timePeriodInput.setText(itemTimePeriod);

                        // Set the selected category in the spinner once categories are loaded
                        categorySpinner.post(() -> {
                            int categoryPosition = categories.indexOf(itemCategory);
                            if (categoryPosition >= 0) {
                                categorySpinner.setSelection(categoryPosition);
                            }
                        });
                    }
                })
                .addOnFailureListener(e -> Toast.makeText(this, "Failed to load item details", Toast.LENGTH_SHORT).show());
    }


    private void deleteItemByName(String itemName) {
        db.collection("items")
                .whereEqualTo("name", itemName)  // Query for item with matching name
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (!queryDocumentSnapshots.isEmpty()) {
                        for (DocumentSnapshot document : queryDocumentSnapshots) {
                            String documentId = document.getId();  // Get the document ID
                            db.collection("items").document(documentId)  // Delete the document by its ID
                                    .delete()
                                    .addOnSuccessListener(aVoid -> {
                                        Toast.makeText(EditItemActivity.this, "Item deleted successfully!", Toast.LENGTH_SHORT).show();
                                        finish();  // Close the activity after deletion
                                    })
                                    .addOnFailureListener(e -> Toast.makeText(EditItemActivity.this, "Failed to delete item.", Toast.LENGTH_SHORT).show());
                        }
                    } else {
                        Toast.makeText(EditItemActivity.this, "Item not found.", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(EditItemActivity.this, "Failed to fetch item.", Toast.LENGTH_SHORT).show();
                });
    }
    private boolean validateFields(EditText nameInput, EditText descriptionInput, EditText feeInput,
                                   EditText timePeriodInput) {
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
