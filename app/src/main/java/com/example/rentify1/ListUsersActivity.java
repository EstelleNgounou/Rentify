package com.example.rentify1;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ListUsersActivity extends AppCompatActivity {
    private DatabaseReference database;
    private String selectedUserId;
    private ArrayAdapter<String> adapter;
    private List<String> userDisplayList; // List to display user info
    private List<Map<String, String>> userDataList; // List to hold user data

    private static final String FIRST_NAME_KEY = "firstname";
    private static final String ROLE_KEY = "role";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_list_users);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialize UI components
        ListView listViewUsers = findViewById(R.id.listViewUsers);

        // Initialize lists
        userDisplayList = new ArrayList<>();
        userDataList = new ArrayList<>();

        // Set up adapter for the ListView
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, userDisplayList);
        listViewUsers.setAdapter(adapter);

        // Initialize Firebase database reference
        database = FirebaseDatabase.getInstance().getReference("users");

        loadUsers();

        listViewUsers.setOnItemClickListener((parent, view, position, id) -> {
            Map<String, String> selectedUser = userDataList.get(position);
            selectedUserId = selectedUser.get("id");
            new AlertDialog.Builder(ListUsersActivity.this)
                    .setTitle("User Options")
                    .setPositiveButton("Disable", (dialog, which) -> disableUser())
                    .setNegativeButton("Delete", (dialog, which) -> deleteUser())
                    .setNeutralButton("Cancel", null)
                    .show();
        });
    }

    private void disableUser() {
        if (selectedUserId == null || selectedUserId.isEmpty()) {
            Toast.makeText(this, "Please select a user", Toast.LENGTH_SHORT).show();
            return;
        }
        DatabaseReference user = database.child(selectedUserId);
        user.child("disabled").setValue(true).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(this, "The user has been disabled", Toast.LENGTH_SHORT).show();
                userDisplayList.removeIf(userInfo -> userInfo.startsWith(selectedUserId));
                adapter.notifyDataSetChanged();
            } else {
                Toast.makeText(this, "Failed to disable user", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void deleteUser() {
        if (selectedUserId == null || selectedUserId.isEmpty()) {
            Toast.makeText(this, "Please select a user", Toast.LENGTH_SHORT).show();
            return;
        }
        DatabaseReference user = database.child(selectedUserId);
        user.removeValue().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(this, "The user has been deleted", Toast.LENGTH_SHORT).show();
                userDisplayList.removeIf(userInfo -> userInfo.startsWith(selectedUserId));

                // Check if userData is null before comparing
                userDataList.removeIf(userData -> selectedUserId.equals(userData.get("id")));

                adapter.notifyDataSetChanged();
            } else {
                Toast.makeText(this, "Failed to delete user", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadUsers() {
        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                userDisplayList.clear(); // Clear existing user display list
                userDataList.clear(); // Clear existing user data list

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Map<String, String> userData = new HashMap<>();
                    String userId = snapshot.getKey();
                    String userName = snapshot.child(FIRST_NAME_KEY).getValue(String.class);
                    String userType = snapshot.child(ROLE_KEY).getValue(String.class);

                    if (userName != null && userId != null) {
                        userData.put("id", userId);
                        userData.put("name", userName);
                        userData.put("userType", userType != null ? userType : "unknown");

                        // Add user info to display list
                        userDisplayList.add(userName + " [" + userData.get("userType") + "]");
                        userDataList.add(userData); // Add user data to the data list
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(ListUsersActivity.this, "Failed to load users", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
