package com.example.rentify1;

import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.List;

public class RenterRequestActivity extends AppCompatActivity {

    private ListView requestsListView; // Replace RecyclerView with ListView
    private RequestAdapter requestAdapter;
    private List<Request> requestList = new ArrayList<>();
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_renter_request);

        // Edge-to-Edge UI setup
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialize ListView
        requestsListView = findViewById(R.id.requestsListView);

        // Initialize adapter and attach it to ListView
        requestAdapter = new RequestAdapter(this, requestList);
        requestsListView.setAdapter(requestAdapter);

        // Load the user's requests
        loadRequests();
    }

    private void loadRequests() {

        String renterId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        FirebaseFirestore.getInstance()
                .collection("requests")
                .whereEqualTo("renterId", renterId)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        requestList.clear();
                        for (DocumentSnapshot document : task.getResult()) {
                            Request request = document.toObject(Request.class);
                            request.setRequestId(document.getId());
                            requestList.add(request);
                        }
                        requestAdapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(this, "Failed to load requests", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
