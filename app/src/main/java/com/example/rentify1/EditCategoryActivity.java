// file: editcategoryactivity.java
package com.example.rentify1;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.DataSnapshot;

public class EditCategoryActivity extends AppCompatActivity {

    // declare ui elements
    private EditText editTextEditCategoryName;
    private EditText editTextEditCategoryDescription;
    private DatabaseReference database; // reference to firebase database

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_category);

        // get the category id passed from the previous activity
        String categoryId = getIntent().getStringExtra("CATEGORY_ID");
        // check if the category id is missing
        if (categoryId == null) {
            Toast.makeText(this, "category id is missing", Toast.LENGTH_SHORT).show();
            finish(); // close the activity if the id is missing
            return;
        }

        // initialize ui elements
        editTextEditCategoryName = findViewById(R.id.editTextEditCategoryName);
        editTextEditCategoryDescription = findViewById(R.id.editTextEditCategoryDescription);
        Button buttonEditCategory = findViewById(R.id.buttonListCategory);
        Button buttonDeleteCategory = findViewById(R.id.buttonDeleteCategory);

        // set the database reference to the specific category using the id
        database = FirebaseDatabase.getInstance().getReference("categories").child(categoryId);
        // load the existing category data
        loadCategoryData();

        // set onclick listeners for the buttons
        buttonEditCategory.setOnClickListener(v -> updateCategory());
        buttonDeleteCategory.setOnClickListener(v -> deleteCategory());
    }

    // method to load the existing category data
    private void loadCategoryData() {
        database.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // check if the category exists in the database
                if (snapshot.exists()) {
                    // retrieve the category object from the snapshot
                    Category category = snapshot.getValue(Category.class);
                    // check if the category is not null
                    if (category != null) {
                        // populate the edittext fields with existing data
                        editTextEditCategoryName.setText(category.getName());
                        editTextEditCategoryDescription.setText(category.getDescription());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // show an error message if data loading fails
                Toast.makeText(EditCategoryActivity.this, "failed to load category: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    // method to update the category
    private void updateCategory() {
        // get the new values from the edittext fields
        String newName = editTextEditCategoryName.getText().toString().trim();
        String newDescription = editTextEditCategoryDescription.getText().toString().trim();

        // check if any field is empty
        if (newName.isEmpty() || newDescription.isEmpty()) {
            Toast.makeText(this, "please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // create an updated category object
        Category updatedCategory = new Category(newName, newDescription);
        // update the category in the database
        database.setValue(updatedCategory).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                // show success message and finish the activity
                Toast.makeText(this, "category updated successfully", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                // show error message if the update fails
                Toast.makeText(this, "error updating category", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // method to delete the category (implementation to be added)
    private void deleteCategory() {
        // Query the database to find the category with the matching name
        FirebaseDatabase.getInstance().getReference("categories")
                .orderByChild("name")
                .equalTo(editTextEditCategoryName.getText().toString().trim())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            // Loop through the matching categories and delete them
                            for (DataSnapshot categorySnapshot : snapshot.getChildren()) {
                                categorySnapshot.getRef().removeValue().addOnCompleteListener(task -> {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(EditCategoryActivity.this, "Category deleted successfully", Toast.LENGTH_SHORT).show();
                                        finish();
                                    } else {
                                        Toast.makeText(EditCategoryActivity.this, "Error deleting category", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        } else {
                            Toast.makeText(EditCategoryActivity.this, "Category does not exist", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(EditCategoryActivity.this, "Failed to delete category: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
