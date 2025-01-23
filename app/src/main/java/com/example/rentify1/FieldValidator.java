package com.example.rentify1;

import android.text.TextUtils;
import android.util.Patterns;
import android.widget.EditText;
import android.widget.Toast;
import android.content.Context;

public class FieldValidator {

    private final Context context;

    public FieldValidator(Context context) {
        this.context = context;
    }

    // Validate Email
    public boolean validateEmail(EditText emailField) {
        String email = emailField.getText().toString().trim();
        if (TextUtils.isEmpty(email)) {
            emailField.setError("Email is required.");
            return false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailField.setError("Enter a valid email address.");
            return false;
        }
        return true;
    }

    // Validate Password
    public boolean validatePassword(EditText passwordField) {
        String password = passwordField.getText().toString();
        if (TextUtils.isEmpty(password)) {
            passwordField.setError("Password is required.");
            return false;
        } else if (password.length() < 6) {
            passwordField.setError("Password must be at least 6 characters.");
            return false;
        }
        return true;
    }

    // Validate First Name
    public boolean validateFirstName(EditText firstNameField) {
        String firstName = firstNameField.getText().toString().trim();
        if (TextUtils.isEmpty(firstName)) {
            firstNameField.setError("First name is required.");
            return false;
        } else if (firstName.length() < 2) {
            firstNameField.setError("First name must be at least 3 characters.");
            return false;
        }
        return true;
    }

    // Validate Last Name
    public boolean validateLastName(EditText lastNameField) {
        String lastName = lastNameField.getText().toString().trim();
        if (TextUtils.isEmpty(lastName)) {
            lastNameField.setError("Last name is required.");
            return false;
        } else if (lastName.length() < 2) {
            lastNameField.setError("Last name must be at least 2 characters.");
            return false;
        }
        return true;
    }

    // Validate Time Period
    public boolean validateTimePeriod(EditText timePeriodField) {
        String timePeriod = timePeriodField.getText().toString().trim();

        if (TextUtils.isEmpty(timePeriod)) {
            timePeriodField.setError("Time period is required.");
            return false;
        }

        // Check if the input contains a valid number followed by "days" or "weeks"
        if (!timePeriod.matches("\\d+\\s*(day|week)s?")) {
            timePeriodField.setError("Enter a valid time period (e.g., 5 days or 3 weeks).");
            return false;
        }

        return true;
    }

    // Validate Search Field
    public boolean validateSearchField(EditText searchField) {
        String query = searchField.getText().toString().trim();
        if (TextUtils.isEmpty(query)) {
            searchField.setError("Search term is required.");
            Toast.makeText(context, "Please enter a search term", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    // Main Validation Call for All Fields
    public boolean validateAllFields(EditText emailField, EditText passwordField, EditText firstNameField, EditText lastNameField, EditText timePeriodField) {
        boolean isEmailValid = validateEmail(emailField);
        boolean isPasswordValid = validatePassword(passwordField);
        boolean isFirstNameValid = validateFirstName(firstNameField);
        boolean isLastNameValid = validateLastName(lastNameField);
        boolean isTimePeriodValid = validateTimePeriod(timePeriodField); // Validate time period

        if (isEmailValid && isPasswordValid && isFirstNameValid && isLastNameValid && isTimePeriodValid) {
            Toast.makeText(context, "All fields are valid!", Toast.LENGTH_SHORT).show();
            return true;
        } else {
            Toast.makeText(context, "Please correct the errors.", Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    // Overloaded method without the time period field
    public boolean validateAllFieldsWithoutTimePeriod(EditText emailField, EditText passwordField, EditText firstNameField, EditText lastNameField) {
        boolean isEmailValid = validateEmail(emailField);
        boolean isPasswordValid = validatePassword(passwordField);
        boolean isFirstNameValid = validateFirstName(firstNameField);
        boolean isLastNameValid = validateLastName(lastNameField);

        if (isEmailValid && isPasswordValid && isFirstNameValid && isLastNameValid) {
            Toast.makeText(context, "All fields are valid!", Toast.LENGTH_SHORT).show();
            return true;
        } else {
            Toast.makeText(context, "Please correct the errors.", Toast.LENGTH_SHORT).show();
            return false;
        }
    }
}
