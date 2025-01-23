package com.example.rentify1;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class RequestAdapter extends ArrayAdapter<Request> {

    private Context context;
    private List<Request> requestList;

    public RequestAdapter(Context context, List<Request> requestList) {
        super(context, 0, requestList);
        this.context = context;
        this.requestList = requestList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_request, parent, false);
        }

        Request request = requestList.get(position);

        // Get references to the TextViews in the item_request layout
        TextView renterIdTextView = convertView.findViewById(R.id.renterIdTextView);
        TextView itemIdTextView = convertView.findViewById(R.id.itemIdTextView);
        TextView customMessageTextView = convertView.findViewById(R.id.customMessageTextView);
        TextView statusTextView = convertView.findViewById(R.id.statusTextView);

        // Set the request details to the TextViews
        renterIdTextView.setText("Renter ID: " + request.getRenterId());
        itemIdTextView.setText("Item: " + request.getItemId());
        customMessageTextView.setText("Message: " + request.getMessage());
        statusTextView.setText("Status: " + request.getStatus());

        return convertView;
    }
}
