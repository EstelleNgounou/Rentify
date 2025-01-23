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

import java.util.HashMap; // Import HashMap

public class RegisterLessorActivity extends AppCompatActivity {
    private EditText firstNameInput, lastNameInput, emailInput, passwordInput;
    private Button registerButton;
    private FirebaseAuth auth;
    private DatabaseReference db;
    private FieldValidator fieldValidator; // Declare FieldValidator

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_lessor);

        // Initialize Firebase Auth and Database
        auth = FirebaseAuth.getInstance();
        db = FirebaseDatabase.getInstance().getReference();

        // Get references to UI components
        firstNameInput = findViewById(R.id.firstNameLessor);
        lastNameInput = findViewById(R.id.lastNameLessor);
        emailInput = findViewById(R.id.emailLessor);
        passwordInput = findViewById(R.id.passwordLessor);
        registerButton = findViewById(R.id.registerLessorButton);

        // Initialize FieldValidator
        fieldValidator = new FieldValidator(this);

        // Set a click listener for the register button
        registerButton.setOnClickListener(v -> registerLessor());
    }

    private void registerLessor() {
        // Validate input fields using FieldValidator
        boolean isFirstNameValid = fieldValidator.validateFirstName(firstNameInput); // Assuming first name is treated like a username
        boolean isLastNameValid = fieldValidator.validateLastName(lastNameInput); // Assuming last name is treated like a username
        boolean isEmailValid = fieldValidator.validateEmail(emailInput);
        boolean isPasswordValid = fieldValidator.validatePassword(passwordInput);

        if (!isFirstNameValid || !isLastNameValid || !isEmailValid || !isPasswordValid) {
            // One or more fields are invalid, no need to proceed with registration
            Toast.makeText(this, "Please correct the errors before proceeding.", Toast.LENGTH_SHORT).show();
            return;
        }

        String firstName = firstNameInput.getText().toString().trim();
        String lastName = lastNameInput.getText().toString().trim();
        String email = emailInput.getText().toString().trim();
        String password = passwordInput.getText().toString().trim();

        // Create user in Firebase Authentication
        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = auth.getCurrentUser();
                        if (user != null) {
                            saveLessorToRealtimeDatabase(user.getUid(), firstName, lastName, email);
                        }
                    } else {
                        // Handle null pointer for exception message safely
                        String message = (task.getException() != null) ? task.getException().getMessage() : "Unknown error occurred";
                        Toast.makeText(this, "Registration failed: " + message, Toast.LENGTH_LONG).show();
                    }
                });
    }

    private void saveLessorToRealtimeDatabase(String userId, String firstName, String lastName, String email) {
        HashMap<String, Object> user = new HashMap<>();
        user.put("firstname", firstName);
        user.put("lastname", lastName);
        user.put("email", email);
        user.put("role", "lessor");

        // Save the user data to the Realtime Database
        db.child("users").child(userId).setValue(user)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(this, "Renter account created successfully", Toast.LENGTH_SHORT).show();

                    // Create the intent to navigate to RenterActivity
                    Intent intent = new Intent(RegisterLessorActivity.this, WelcomeActivity.class);

                    // Pass the 'renter' role as an extra in the intent
                    intent.putExtra("UserRole", "lessor");

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
