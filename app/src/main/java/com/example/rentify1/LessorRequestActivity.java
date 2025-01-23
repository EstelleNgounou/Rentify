package com.example.rentify1;

import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class LessorRequestActivity extends AppCompatActivity {

    private ListView requestsListView;
    private List<Request> requestList;
    private RequestAdapter requestAdapter;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lessor_request);

        // Initialize Firebase references
        auth = FirebaseAuth.getInstance();

        // Initialize UI elements
        requestsListView = findViewById(R.id.requestsListView);

        // Initialize the list and adapter
        requestList = new ArrayList<>();
        requestAdapter = new RequestAdapter(this, requestList);
        requestsListView.setAdapter(requestAdapter);

        // Load rental requests
        loadRequests();

        // Handle rental request item clicks
        requestsListView.setOnItemClickListener((parent, view, position, id) -> {
            Request request = requestList.get(position);
            showRequestOptionsDialog(request);
        });
    }

    private void loadRequests() {
        String lessorId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("requests")
                .whereEqualTo("lessorId", lessorId)
                .whereEqualTo("status", "Pending")
                .addSnapshotListener((querySnapshot, error) -> {
                    if (error != null) {
                        Toast.makeText(this, "Error loading requests: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if (querySnapshot != null) {
                        requestList.clear(); // Clear old data
                        for (DocumentSnapshot document : querySnapshot) {
                            Request request = document.toObject(Request.class);
                            request.setRequestId(document.getId());
                            requestList.add(request);
                        }
                        requestAdapter.notifyDataSetChanged(); // Notify the adapter to refresh the UI
                    }
                });
    }

    private void showRequestOptionsDialog(Request request) {
        // Show options to accept or deny the request
        String requestDetails = "Request from Renter: " + request.getRenterId() + "\n" +
                "Message: " + request.getMessage() + "\n" +
                "Item: " + request.getItemId();

        new AlertDialog.Builder(this)
                .setTitle("Rental Request")
                .setMessage(requestDetails)
                .setPositiveButton("Accept", (dialog, which) -> updateRequestStatus(request, "Accepted"))
                .setNegativeButton("Deny", (dialog, which) -> updateRequestStatus(request, "Denied"))
                .show();
    }

    private void updateRequestStatus (Request request, String status){
            FirebaseFirestore db = FirebaseFirestore.getInstance();

            db.collection("requests")
                    .document(request.getRequestId())
                    .update("status", status)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(this, "Request " + status, Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(this, "Failed to update request", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }

