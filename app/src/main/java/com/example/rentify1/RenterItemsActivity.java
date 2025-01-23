package com.example.rentify1;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.List;

public class RenterItemsActivity extends AppCompatActivity {

    private RecyclerView itemsRecyclerView;
    private ItemAdapterRenter itemAdapter;
    private List<Item> itemList = new ArrayList<>();
    private EditText searchField;
    private Button searchButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_renter_items);

        // Initialize UI components
        itemsRecyclerView = findViewById(R.id.itemsRecyclerView);
        searchField = findViewById(R.id.searchField);
        searchButton = findViewById(R.id.searchButton);

        itemsRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Load all items initially
        loadItemsFromFirestore(null);

        // Set up search functionality
        searchButton.setOnClickListener(v -> {
            String query = searchField.getText().toString().trim();
            loadItemsFromFirestore(query.isEmpty() ? null : query);
        });
    }

    private void loadItemsFromFirestore(String query) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        Query firestoreQuery = db.collection("items");

        // If a query is provided, filter items by name or category
        if (query != null) {
            firestoreQuery = firestoreQuery.whereGreaterThanOrEqualTo("name", query)
                    .whereLessThanOrEqualTo("name", query + "\uf8ff");
        }

        firestoreQuery.get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    itemList.clear(); // Clear any existing items in the list
                    for (DocumentSnapshot document : queryDocumentSnapshots) {
                        Item item = document.toObject(Item.class);
                        item.setItemId(document.getId());

                        // Get the lessorId from the document
                        String lessorId = document.getString("lessorId");
                        item.setLessorId(lessorId);  // Assuming you have a setLessorId method in your Item class

                        itemList.add(item);
                    }

                    // Set up the adapter with the updated list
                    itemAdapter = new ItemAdapterRenter(this, itemList, this::onItemClicked);
                    itemsRecyclerView.setAdapter(itemAdapter);

                    if (itemList.isEmpty()) {
                        Toast.makeText(this, "No items found", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Failed to load items.", Toast.LENGTH_SHORT).show();
                });
    }

    // Handle item clicks
    private void onItemClicked(Item item) {
        Intent intent = new Intent(this, ItemDetailActivity.class);
        intent.putExtra("itemId", item.getItemId());
        intent.putExtra("itemName", item.getName());
        intent.putExtra("itemDescription", item.getDescription());
        intent.putExtra("itemFee", item.getFee());
        intent.putExtra("itemTimePeriod", item.getTimePeriod());
        intent.putExtra("itemCategory", item.getCategory());
        intent.putStringArrayListExtra("itemImageUrls", new ArrayList<>(item.getImageUrls()));
        intent.putExtra("lessorId", item.getLessorId());  // Pass the lessor ID to the next activity
        startActivity(intent);
    }
}
