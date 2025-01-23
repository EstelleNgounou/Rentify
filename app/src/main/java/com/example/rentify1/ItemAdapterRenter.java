package com.example.rentify1;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

public class ItemAdapterRenter extends RecyclerView.Adapter<ItemAdapterRenter.ItemViewHolder> {

    private final Context context;
    private final List<Item> itemList;
    private final OnItemClickListener onItemClickListener;

    // Constructor with a click listener parameter
    public ItemAdapterRenter(Context context, List<Item> itemList, OnItemClickListener onItemClickListener) {
        this.context = context;
        this.itemList = itemList;
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.items_card, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        Item item = itemList.get(position);

        // Populate the view with item data
        holder.itemName.setText(item.getName());
        holder.itemDescription.setText(item.getDescription());
        holder.itemFee.setText(String.format("Fee: $%.2f for %s", item.getFee(), item.getTimePeriod()));
        holder.itemCategory.setText(String.format("Category: %s", item.getCategory()));

        // Load item image using Glide
        if (item.getImageUrls() != null && !item.getImageUrls().isEmpty()) {
            String imageUrl = item.getImageUrls().get(0); // Load the first image URL
            Glide.with(context)
                    .load(imageUrl)
                    .placeholder(R.drawable.placeholder) // Display placeholder while loading
                    .into(holder.itemImage);
        } else {
            holder.itemImage.setImageResource(R.drawable.placeholder); // Display placeholder if no image
        }

        // Set an OnClickListener for the entire item view
        holder.itemView.setOnClickListener(v -> {
            // Notify the click event to the listener
            onItemClickListener.onItemClick(item);
        });
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public static class ItemViewHolder extends RecyclerView.ViewHolder {
        TextView itemName, itemDescription, itemFee, itemCategory;
        ImageView itemImage;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            itemName = itemView.findViewById(R.id.itemName);
            itemDescription = itemView.findViewById(R.id.itemDescription);
            itemFee = itemView.findViewById(R.id.itemFee);
            itemCategory = itemView.findViewById(R.id.itemCategory);
            itemImage = itemView.findViewById(R.id.itemImage);
        }
    }

    // Interface to handle item clicks
    public interface OnItemClickListener {
        void onItemClick(Item item);
    }
}
