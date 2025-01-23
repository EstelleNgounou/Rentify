package com.example.rentify1;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.graphics.Insets;

public class WelcomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        enableEdgeToEdge(); // Enable edge-to-edge experience
        setContentView(R.layout.activity_welcome2); // Ensure the layout file name is correct

        // Set up the window insets to adjust for system bars
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            // Correctly retrieve system bar insets
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Retrieve the username and user role from the intent
        String username = getIntent().getStringExtra("Name");
        String userType = getIntent().getStringExtra("UserRole");

        // Find the TextViews and set their text
        TextView usernameTextView = findViewById(R.id.usernameTextView);
        TextView userTypeTextView = findViewById(R.id.userTypeTextView);

        // Set username and userType safely to avoid NullPointerException
        usernameTextView.setText(username != null ? username + "!" : "User!");
        userTypeTextView.setText(userType != null ? userType + "." : "Guest.");

        // Define delay time
        int delayTime = 3000; // 3000 ms = 3 seconds

        new Handler().postDelayed(() -> {
            // Check user role and navigate accordingly
            Intent intent;
            if ("lessor".equalsIgnoreCase(userType)) {
                // Navigate to LessorActivity if user is a lessor
                intent = new Intent(WelcomeActivity.this, LessorActivity.class);
            } else {
                // Default to LessorActivity (you can modify this as needed)
                intent = new Intent(WelcomeActivity.this, RenterActivity.class);
            }
            startActivity(intent);
            finish(); // Finish this activity so it's removed from the back stack
        }, delayTime);
    }

    private void enableEdgeToEdge() {
        // Set flags to enable edge-to-edge layout
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        // Set the status bar and navigation bar to be transparent
        getWindow().setStatusBarColor(Color.TRANSPARENT);
        getWindow().setNavigationBarColor(Color.TRANSPARENT);
    }
}
