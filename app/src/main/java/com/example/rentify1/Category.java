// File: Category.java
package com.example.rentify1;

// defines the `Category` class for storing category details
public class Category {
    private String id;
    private String name; // category name
    private String description; // category description

    // no-argument constructor required by Firebase for data mapping
    public Category() {}

    // constructor to initialize category with name and description
    public Category(String name, String description) {
        this.name = name;
        this.description = description;
    }
    public String getId(){
        return id;
    }
    public void setId(String id){
        this.id = id;
    }
    // returns the category name
    public String getName() {
        return name;
    }

    // sets the category name (not used directly but kept for future extensibility)
    public void setName(String name) {
        this.name = name;
    }

    // returns the category description
    public String getDescription() {
        return description;
    }

    // sets the category description (not used directly but kept for future extensibility)
    public void setDescription(String description) {
        this.description = description;
    }
}
