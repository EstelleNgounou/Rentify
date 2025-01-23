package com.example.rentify1;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth auth; // firebase authentication instance
    private DatabaseReference db; // database reference
    private EditText emailInput; // EditText for email
    private EditText passwordInput; // EditText for password
    private Button loginButton; // Login button


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FirebaseApp.initializeApp(this);

        setContentView(R.layout.activity_main); // set the layout for main activity

        // initialize firebase auth and database
        auth = FirebaseAuth.getInstance();
        db = FirebaseDatabase.getInstance().getReference();

        emailInput = findViewById(R.id.emailInput);
        passwordInput = findViewById(R.id.passwordInput);
        loginButton = findViewById(R.id.loginButton);

        // set greeting message
        TextView greetingText = findViewById(R.id.greeting_text);
        greetingText.setText(R.string.greeting_text); // get greeting text from resources

        // initialize the renter register button
        Button registerRenterButton = findViewById(R.id.registerRenterButton);
        registerRenterButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, RegisterRenterActivity.class);
            intent.putExtra("UserRole", "Renter"); // pass user role as renter
            startActivity(intent); // start the register renter activity
        });

        // initialize the lessor register button
        Button registerLessorButton = findViewById(R.id.registerLessorButton);
        registerLessorButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, RegisterLessorActivity.class);
            intent.putExtra("UserRole", "Lessor"); // pass user role as lessor
            startActivity(intent); // start the register lessor activity
        });

        // check if any admin accounts already exist
        if (auth.getCurrentUser() == null) {
            Toast.makeText(this, "User is not authenticated. Please log in.", Toast.LENGTH_SHORT).show();
        } else {
            // Proceed with checking for admin account
            checkAndCreateAdminAccount();
        }
        // Set click listener for the login button
        loginButton.setOnClickListener(v -> login());
    }



    private void checkAndCreateAdminAccount() {
        db.child("users") // access the users node in database
                .orderByChild("role") // order users by their role
                .equalTo("admin") // filter for admin accounts
                .get() // get the results
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        // check if the result is not null
                        if (task.getResult() != null && !task.getResult().exists()) { // if no admin account exists
                            createAdminAccount(); // create admin account
                        }
                    } else {
                        String errorMessage = task.getException() != null ? task.getException().getMessage() : "unknown error occurred";
                        Toast.makeText(this, "error checking admin account: " + errorMessage, Toast.LENGTH_SHORT).show(); // show error message
                    }
                });
    }

    private void createAdminAccount() {
        String email = "admin@rentify.com"; //***************** admin email *********************
        String password = "XPI76SZUqyCjVxgnUjm0"; // admin password

        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        String userId = task.getResult().getUser().getUid();
                        // Call the function to add user data
                        addUserToDatabase(email, "admin");
                    } else {
                        String errorMessage = task.getException() != null ? task.getException().getMessage() : "unknown error occurred";
                        Toast.makeText(this, "Error creating admin account: " + errorMessage, Toast.LENGTH_LONG).show();
                    }
                });
    }

    private void addUserToDatabase(String email, String role) {
        // Save the user data under the "users" node using the user's UID
        String key = db.child("users").push().getKey(); // Generates a unique key

        // Create a map to hold user data
        Map<String, Object> userData = new HashMap<>();
        userData.put("email", email);
        userData.put("role", role);

        // Save the user data under the "users" node using the generated unique key
        db.child("users").child(key).setValue(userData)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(MainActivity.this, "User data added successfully", Toast.LENGTH_SHORT).show();
                    } else {
                        String errorMessage = task.getException() != null ? task.getException().getMessage() : "Unknown error";
                        Toast.makeText(MainActivity.this, "Error adding user data: " + errorMessage, Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void login(){
        String email = emailInput.getText().toString().trim();
        String password = passwordInput.getText().toString().trim();

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please enter a valid email and password", Toast.LENGTH_SHORT).show();
            return;
        }

        auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        // User is signed in
                        checkUserRole(email);
                    } else {
                        // Sign in failed
                        String errorMessage = task.getException() != null ? task.getException().getMessage() : "Error occurred";
                        Toast.makeText(this, "Login failed: " + errorMessage, Toast.LENGTH_LONG).show();
                    }
                });
    }

    private void checkUserRole(String email) {
        db.child("users").orderByChild("email").equalTo(email).get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult() != null && task.getResult().exists()) {
                        // User found
                        DataSnapshot userSnapshot = task.getResult().getChildren().iterator().next();
                        String role = userSnapshot.child("role").getValue(String.class); // Get user role

                        // Open appropriate activity based on user role
                        if ("admin".equals(role)) {
                            Intent intent = new Intent(MainActivity.this, AdminActivity.class);
                            startActivity(intent);
                        } else if ("renter".equals(role)) {
                            Intent intent = new Intent(MainActivity.this, RenterActivity.class);
                            startActivity(intent);
                        } else if ("lessor".equals(role)) {
                            Intent intent = new Intent(MainActivity.this, LessorActivity.class);
                            startActivity(intent);
                        }
                    } else {
                        Toast.makeText(this, "User not existent", Toast.LENGTH_SHORT).show();
                    }
                });
    }

}
