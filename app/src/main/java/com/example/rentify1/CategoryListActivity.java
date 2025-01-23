package com.example.rentify1;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.DataSnapshot;

import java.util.ArrayList;
public class CategoryListActivity extends AppCompatActivity {

    private ListView listViewCategories;
    private Button buttonEditCategory;  // Button to enable editing
    private DatabaseReference databaseCategories;
    private ArrayList<String> categoryList;  // Changed to store category names (Strings)
    private ArrayAdapter<String> categoryAdapter;  // Updated to use String array adapter
    private boolean isEditingEnabled = false;  // State variable to check if editing is enabled
    private ArrayList<String> categoryIds;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_list);

        // Initialize UI elements
        listViewCategories = findViewById(R.id.listViewCategories);
        buttonEditCategory = findViewById(R.id.buttonEditCategory);
        categoryList = new ArrayList<>();  // List to hold category names
        categoryIds = new ArrayList<>();

        // Initialize Firebase reference
        databaseCategories = FirebaseDatabase.getInstance().getReference("categories");

        // Initialize ArrayAdapter with category names (Strings)
        categoryAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, categoryList);

        // Load categories from Firebase
        loadCategories();

        // Set the adapter to the ListView
        listViewCategories.setAdapter(categoryAdapter);

        // Set on item click listener for editing categories
        listViewCategories.setOnItemClickListener((parent, view, position, id) -> {
            if (isEditingEnabled) {
                String selectedCategoryId = categoryIds.get(position); // Get category ID by index
                Intent intent = new Intent(CategoryListActivity.this, EditCategoryActivity.class);
                intent.putExtra("CATEGORY_ID", selectedCategoryId); // Pass the category ID
                startActivity(intent);
            }
        });


        // Set onClickListener for Edit Category button
        buttonEditCategory.setOnClickListener(v -> {
            isEditingEnabled = !isEditingEnabled;  // Toggle editing mode
            if (isEditingEnabled) {
                Toast.makeText(CategoryListActivity.this, "Select a category to edit", Toast.LENGTH_SHORT).show();
                buttonEditCategory.setText("Cancel Edit");  // Change button text to "Cancel Edit"
            } else {
                Toast.makeText(CategoryListActivity.this, "Editing disabled", Toast.LENGTH_SHORT).show();
                buttonEditCategory.setText("Edit Category");  // Revert button text to "Edit Category"
            }
        });
    }

    // Method to load categories from Firebase
    private void loadCategories() {
        databaseCategories.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                categoryList.clear();
                categoryIds.clear(); // Clear both lists

                for (DataSnapshot categorySnapshot : snapshot.getChildren()) {
                    String categoryId = categorySnapshot.getKey(); // Get the category ID
                    String categoryName = categorySnapshot.child("name").getValue(String.class); // Get the category name

                    if (categoryId != null && categoryName != null) {
                        categoryList.add(categoryName); // Add to the display list
                        categoryIds.add(categoryId); // Store the corresponding ID
                    }
                }
                categoryAdapter.notifyDataSetChanged(); // Update the ListView
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Toast.makeText(CategoryListActivity.this, "Failed to load categories", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
