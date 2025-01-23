package com.example.rentify1;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class ItemDetailActivity extends AppCompatActivity {

    private TextView itemName, itemDescription, itemFee, itemCategory;
    private ImageView itemImage;
    private Button sendRequestButton;
    private EditText customMessageEditText; // EditText for custom message

    private String itemId, itemNameStr, itemDescriptionStr, itemCategoryStr, itemTimePeriodStr, lessorId;
    private double itemFeeAmount;
    private ArrayList<String> itemImageUrls;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_detail);

        itemName = findViewById(R.id.itemName);
        itemDescription = findViewById(R.id.itemDescription);
        itemFee = findViewById(R.id.itemFee);
        itemCategory = findViewById(R.id.itemCategory);
        itemImage = findViewById(R.id.itemImage);
        sendRequestButton = findViewById(R.id.sendRequestButton);
        customMessageEditText = findViewById(R.id.customMessage); // Initialize the EditText

        // Retrieve data from the intent
        itemId = getIntent().getStringExtra("itemId");
        itemNameStr = getIntent().getStringExtra("itemName");
        itemDescriptionStr = getIntent().getStringExtra("itemDescription");
        itemFeeAmount = getIntent().getDoubleExtra("itemFee", 0);
        itemTimePeriodStr = getIntent().getStringExtra("itemTimePeriod");
        itemCategoryStr = getIntent().getStringExtra("itemCategory");
        itemImageUrls = getIntent().getStringArrayListExtra("itemImageUrls");
        lessorId = getIntent().getStringExtra("lessorId"); // Retrieve lessor ID (make sure it's passed from the previous activity)

        // Populate the views
        itemName.setText(itemNameStr);
        itemDescription.setText(itemDescriptionStr);
        itemFee.setText(String.format("Fee: $%.2f for %s", itemFeeAmount, itemTimePeriodStr));
        itemCategory.setText(String.format("Category: %s", itemCategoryStr));

        if (itemImageUrls != null && !itemImageUrls.isEmpty()) {
            Glide.with(this).load(itemImageUrls.get(0)).into(itemImage);
        } else {
            itemImage.setImageResource(R.drawable.placeholder);
        }

        // Set up the button click listener
        sendRequestButton.setOnClickListener(v -> sendRequest());
    }

    private void sendRequest() {
        // Get the custom message from the EditText
        String customMessage = customMessageEditText.getText().toString().trim();

        // Get the current renter's ID from Firebase Authentication
        String renterId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        // Check if the lessorId is null or empty (you may need to handle this in the previous activity if it isn't passed)
        if (lessorId == null || lessorId.isEmpty()) {
            Toast.makeText(ItemDetailActivity.this, "Lessor ID is missing", Toast.LENGTH_SHORT).show();
            return;
        }

        // Create a request object
        Request request = new Request();
        request.setItemId(itemId);
        request.setRenterId(renterId); // Set the real renter ID
        request.setLessorId(lessorId); // Set the real lessor ID
        request.setStatus("Pending");

        // Set the custom message, or a default message if none is provided
        if (customMessage.isEmpty()) {
            request.setMessage("Request to rent this item.");
        } else {
            request.setMessage(customMessage);
        }

        // Save the request to Firestore
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("requests").add(request)
                .addOnSuccessListener(documentReference -> {
                    Toast.makeText(ItemDetailActivity.this, "Request sent successfully!", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(ItemDetailActivity.this, "Failed to send request.", Toast.LENGTH_SHORT).show();
                });
    }
}
