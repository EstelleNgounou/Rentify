package com.example.rentify1;

public class Request {
    private String requestId; // Unique ID for each request
    private String itemId;    // ID of the requested item
    private String renterId;  // ID of the renter making the request
    private String lessorId;  // ID of the lessor who owns the item
    private String status;    // Status of the request: "Pending", "Accepted", "Declined"
    private String message;   // Optional message from the renter

    // Empty constructor for Firebase
    public Request() {}

    // Constructor
    public Request(String requestId, String itemId, String renterId, String lessorId, String status, String message) {
        this.requestId = requestId;
        this.itemId = itemId;
        this.renterId = renterId;
        this.lessorId = lessorId;
        this.status = status;
        this.message = message;
    }

    // Getters and setters
    public String getRequestId() { return requestId; }
    public void setRequestId(String requestId) { this.requestId = requestId; }

    public String getItemId() { return itemId; }
    public void setItemId(String itemId) { this.itemId = itemId; }

    public String getRenterId() { return renterId; }
    public void setRenterId(String renterId) { this.renterId = renterId; }

    public String getLessorId() { return lessorId; }
    public void setLessorId(String lessorId) { this.lessorId = lessorId; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
}