package com.example.rentify1;

import java.util.List;

public class Item {
    private String itemId;       // unique ID for each item
    private String name;         // name of the item
    private String description;  // description of the item
    private double fee;          // fee for renting the item
    private String timePeriod;   // time period for renting
    private String lessorId;     // the lessor who owns the item
    private String category;     // category of the item (added this field)
    private List<String> imageUrls;

    // Empty constructor for Firebase
    public Item() {}

    // Constructor
    public Item(String name, String description, double fee, String timePeriod, String category, String lessorId, List<String> imageUrls) {
        this.name = name;
        this.description = description;
        this.fee = fee;
        this.timePeriod = timePeriod;
        this.category = category;
        this.lessorId = lessorId;
        this.imageUrls = imageUrls;
    }

    // Getters and setters
    public String getItemId() { return itemId; }
    public void setItemId(String itemId) { this.itemId = itemId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public double getFee() { return fee; }
    public void setFee(double fee) { this.fee = fee; }

    public String getTimePeriod() { return timePeriod; }
    public void setTimePeriod(String timePeriod) { this.timePeriod = timePeriod; }

    public String getLessorId() { return lessorId; }
    public void setLessorId(String lessorId) { this.lessorId = lessorId; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public List<String> getImageUrls(){ return imageUrls; }
    public void setImageUrls(List<String> imageUrls){ this.imageUrls = imageUrls; }
}
