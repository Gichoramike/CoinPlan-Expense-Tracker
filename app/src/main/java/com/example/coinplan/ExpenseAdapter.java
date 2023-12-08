package com.example.coinplan;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class ExpenseAdapter extends ArrayAdapter<expense> {

    private Context mContext;
    private int mResource;

    public ExpenseAdapter(@NonNull Context context, int resource, @NonNull ArrayList<expense> objects) {
        super(context, resource, objects);
        this.mContext = context;
        this.mResource = resource;
    }

    @NonNull
    @Override

    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);

        convertView = layoutInflater.inflate(mResource, parent, false);

        TextView txtCategory = convertView.findViewById(R.id.txtCategory);
        TextView txtDescription = convertView.findViewById(R.id.txtDescription);
        TextView txtAmount = convertView.findViewById(R.id.txtAmount);

        txtCategory.setText(getItem(position).getCategory());
        txtDescription.setText(getItem(position).getDescription());
        txtAmount.setText("Ksh " + getItem(position).getKshAmount());

        return convertView;
    }


    // Method to update an item at a specific position
    public void updateItem(int position, expense updatedExpense) {
        if (position >= 0 && position < getCount()) {
            // Update the item at the specified position in the ArrayList
            getItem(position).setCategory(updatedExpense.getCategory());
            getItem(position).setDescription(updatedExpense.getDescription());
            getItem(position).setKshAmount(updatedExpense.getKshAmount());

            // Notify the adapter that the data has changed
            notifyDataSetChanged();
        }
    }

    // Method to remove an item at a specific position
    public void removeItem(int position) {
        if (position >= 0 && position < getCount()) {
            // Remove the item at the specified position in the ArrayList
            remove(getItem(position));

            // Notify the adapter that the data has changed
            notifyDataSetChanged();
        }
    }
}
