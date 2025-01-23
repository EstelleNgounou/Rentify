package com.example.rentify1;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import android.widget.ArrayAdapter;
import java.util.List;
import java.util.Locale;

public class ItemAdapter extends ArrayAdapter<Item> {
    private final Context context;
    private final List<Item> itemList;

    public ItemAdapter(@NonNull Context context, List<Item> itemList) {
        super(context, R.layout.item_row, itemList);
        this.context = context;
        this.itemList = itemList;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_row, parent, false);
        }

        Item item = itemList.get(position);

        TextView nameTextView = convertView.findViewById(R.id.nameTextView);
        TextView categoryTextView = convertView.findViewById(R.id.categoryTextView);
        TextView feeTextView = convertView.findViewById(R.id.feeTextView);

        nameTextView.setText(item.getName());
        categoryTextView.setText(item.getCategory());

        // Use Locale.US for currency formatting
        feeTextView.setText(String.format(Locale.US, "$%.2f", item.getFee()));

        return convertView;
    }
}
