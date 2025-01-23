package com.example.rentify1;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class ListItemsActivity extends AppCompatActivity {

    private ListView listViewItems;
    private DatabaseReference databaseItems;
    private ArrayList<String> itemList;  // List to hold item names
    private ArrayAdapter<String> itemAdapter;
    private Button editItems;
    private boolean isEditingEnabled = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_items);

        // Initialize UI elements
        listViewItems = findViewById(R.id.listViewItems);
        itemList = new ArrayList<>();
        editItems = findViewById(R.id.editItems);

        // Initialize Firebase reference
        databaseItems = FirebaseDatabase.getInstance().getReference("items");

        // Initialize ArrayAdapter with item names (Strings)
        itemAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, itemList);

        // Load items from Firebase
        loadItems();

        // Set the adapter to the ListView
        listViewItems.setAdapter(itemAdapter);

        // Set on item click listener for editing items
        listViewItems.setOnItemClickListener((parent, view, position, id) -> {
            // Get the selected item's name (assuming items have a unique name or ID)
            String selectedItemName = itemList.get(position);

            // Open EditItemActivity to edit the selected item
            Intent intent = new Intent(ListItemsActivity.this, EditItemActivity.class);
            intent.putExtra("ITEM_NAME", selectedItemName);  // Pass the selected item's name
            startActivity(intent);
        });

        editItems.setOnClickListener(v -> {
            isEditingEnabled = !isEditingEnabled;  // Toggle editing mode
            if (isEditingEnabled) {
                Toast.makeText(ListItemsActivity.this, "Select an item to edit", Toast.LENGTH_SHORT).show();
                editItems.setText("Cancel Edit");  // Change button text to "Cancel Edit"
            } else {
                Toast.makeText(ListItemsActivity.this, "Editing disabled", Toast.LENGTH_SHORT).show();
                editItems.setText("Edit Item");  // Revert button text to "Edit Category"
            }
        });
    }

    // Method to load items from Firebase
    private void loadItems() {
        FirebaseFirestore firestore = FirebaseFirestore.getInstance(); // Firestore instance
        firestore.collection("items")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    itemList.clear(); // Clear the list
                    for (DocumentSnapshot document : queryDocumentSnapshots) {
                        String itemName = document.getString("name"); // Assuming "name" field exists
                        if (itemName != null) {
                            itemList.add(itemName); // Add item name to the list
                        }
                    }
                    itemAdapter.notifyDataSetChanged(); // Notify the adapter
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(ListItemsActivity.this, "Failed to load items", Toast.LENGTH_SHORT).show();
                });
    }
}