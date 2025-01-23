// File: AdminActivity.java
package com.example.rentify1;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class AdminActivity extends AppCompatActivity {

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        // initialize button for adding category
        // button to navigate to add category screen
        Button buttonAddCategory = findViewById(R.id.buttonAddCategory);

        // set click listener to start AddCategoryActivity
        buttonAddCategory.setOnClickListener(v -> {
            Intent intent = new Intent(AdminActivity.this, AddCategoryActivity.class); // intent to open add category screen
            startActivity(intent); // start AddCategoryActivity
        });

        //initialize button for editing category
        //button to navigate to edit category
        Button buttonListCategory = findViewById(R.id.buttonListCategory);

        buttonListCategory.setOnClickListener(v->{
            Intent intent = new Intent(AdminActivity.this, CategoryListActivity.class);
            startActivity(intent);
        });

        Button buttonListUsers = findViewById(R.id.buttonListUsers);

        buttonListUsers.setOnClickListener(v-> {
            Intent intent = new Intent(AdminActivity.this, ListUsersActivity.class);
            startActivity(intent);
        });

    }
}
