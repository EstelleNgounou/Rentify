package com.example.rentify1;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat.Type;

public class LessorActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Enable edge-to-edge layout
        enableEdgeToEdge();

        setContentView(R.layout.activity_lessor);

        // Initialize buttons
        Button addItemButton = findViewById(R.id.btn_select_images); // Button for adding an item
        Button listItemsButton = findViewById(R.id.btn_list_items); // Button for listing items
        Button viewRequestsButton = findViewById(R.id.btn_view_requests); // Button for viewing requests

        // Add item button click listener
        addItemButton.setOnClickListener(view -> {
            Intent intent = new Intent(LessorActivity.this, AddItemActivity.class);
            startActivity(intent);
        });

        // List items button click listener
        listItemsButton.setOnClickListener(view -> {
            Intent intent = new Intent(LessorActivity.this, ListItemsActivity.class);
            startActivity(intent);
        });

        // View requests button click listener
        viewRequestsButton.setOnClickListener(view -> {
            Intent intent = new Intent(LessorActivity.this, LessorRequestActivity.class);
            startActivity(intent);
        });

        // Apply insets to adjust padding for edge-to-edge layout
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.lessor), (v, insets) -> {
            v.setPadding(
                    insets.getInsets(Type.systemBars()).left,
                    insets.getInsets(Type.systemBars()).top,
                    insets.getInsets(Type.systemBars()).right,
                    insets.getInsets(Type.systemBars()).bottom
            );
            return insets;
        });
    }

    private void enableEdgeToEdge() {
        // Enable edge-to-edge layout
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        getWindow().setStatusBarColor(Color.TRANSPARENT); // Make status bar transparent
        getWindow().setNavigationBarColor(Color.TRANSPARENT); // Make navigation bar transparent
    }
}
