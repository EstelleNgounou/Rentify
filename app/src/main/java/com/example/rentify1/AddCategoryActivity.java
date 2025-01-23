// File: AddCategoryActivity.java
package com.example.rentify1;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AddCategoryActivity extends AppCompatActivity {

    private EditText editTextCategoryName; // input field for category name
    private EditText editTextCategoryDescription; // input field for category description
    private DatabaseReference database; // reference to firebase database

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_category);

        // initialize UI elements
        editTextCategoryName = findViewById(R.id.editTextCategoryName);
        editTextCategoryDescription = findViewById(R.id.editTextCategoryDescription);
        // button to add category
        Button buttonAddCategory = findViewById(R.id.buttonAddCategory);

        // set reference to "categories" node in firebase database
        database = FirebaseDatabase.getInstance().getReference("categories");

        // set onClickListener for adding category
        buttonAddCategory.setOnClickListener(v -> addCategory());
    }

    // method to add a new category to firebase
    private void addCategory() {
        String name = editTextCategoryName.getText().toString().trim();
        String description = editTextCategoryDescription.getText().toString().trim();

        // validate input fields
        if (name.isEmpty() || description.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // create new category object
        Category category = new Category(name, description);

        // generate unique id for category
        String categoryId = database.push().getKey();

        // check if id is generated
        if (categoryId != null) {
            // store category data in firebase and handle response
            database.child(categoryId).setValue(category).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    Toast.makeText(this, "Category added successfully", Toast.LENGTH_SHORT).show();
                    finish(); // end activity if successful
                } else {
                    Toast.makeText(this, "Error adding category", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(this, "Error generating category ID", Toast.LENGTH_SHORT).show();
        }
    }
}
