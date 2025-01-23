package com.example.rentify1;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class RegisterRenterActivity extends AppCompatActivity {
    private EditText firstNameInput, lastNameInput, emailInput, passwordInput;
    private FirebaseAuth auth;
    private DatabaseReference db;
    private FieldValidator fieldValidator; // FieldValidator instance

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_renter);

        // Initialize Firebase instances
        auth = FirebaseAuth.getInstance();
        db = FirebaseDatabase.getInstance().getReference();

        // Initialize input fields and button
        firstNameInput = findViewById(R.id.firstName);
        lastNameInput = findViewById(R.id.lastName);
        emailInput = findViewById(R.id.email);
        passwordInput = findViewById(R.id.password);
        Button registerButton = findViewById(R.id.registerRenterButton);

        // Initialize FieldValidator
        fieldValidator = new FieldValidator(this);

        // Set click listener with expression lambda
        registerButton.setOnClickListener(v -> registerRenter());
    }

    private void registerRenter() {
        // Validate all fields before proceeding
        if (!fieldValidator.validateAllFieldsWithoutTimePeriod(emailInput, passwordInput, firstNameInput, lastNameInput)) {
            return; // Exit if validation fails
        }

        // Get user inputs and trim whitespace
        String firstName = firstNameInput.getText().toString().trim();
        String lastName = lastNameInput.getText().toString().trim();
        String email = emailInput.getText().toString().trim();
        String password = passwordInput.getText().toString().trim();

        // Create user with email and password
        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = auth.getCurrentUser();
                        if (user != null) {
                            saveRenterToRealtimeDatabase(user.getUid(), firstName, lastName, email);
                        }
                    } else {
                        String message = (task.getException() != null) ? task.getException().getMessage() : "unknown error occurred";
                        Toast.makeText(this, "registration failed: " + message, Toast.LENGTH_LONG).show();
                    }
                });
    }
    private void saveRenterToRealtimeDatabase(String userId, String firstName, String lastName, String email) {
        HashMap<String, Object> user = new HashMap<>();
        user.put("firstname", firstName);
        user.put("lastname", lastName);
        user.put("email", email);
        user.put("role", "renter");

        // Save user data to database
        db.child("users").child(userId).setValue(user)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(this, "Renter account created successfully", Toast.LENGTH_SHORT).show();

                    // Create the intent to navigate to RenterActivity
                    Intent intent = new Intent(RegisterRenterActivity.this, WelcomeActivity.class);

                    // Pass the 'renter' role as an extra in the intent
                    intent.putExtra("UserRole", "renter");

                    // Start the RenterActivity
                    startActivity(intent);

                    // Finish this activity so the user can't go back to registration
                    finish();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Error saving user data: " + e.getMessage(), Toast.LENGTH_LONG).show();
                });
    }


}
