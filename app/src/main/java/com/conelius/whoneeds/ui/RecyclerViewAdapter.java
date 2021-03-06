package com.conelius.whoneeds.ui;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.conelius.whoneeds.R;
import com.conelius.whoneeds.data.DatabaseHandler;
import com.conelius.whoneeds.model.Item;
import com.google.android.material.snackbar.Snackbar;

import java.text.MessageFormat;
import java.util.List;

import static android.content.ContentValues.TAG;

/**
 * created by Conelius on 1/29/2020 at 5:36 AM
 */
public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    private Context context;
    private List<Item> itemList;
    private AlertDialog alertDialog;
    private AlertDialog.Builder builder;
    private LayoutInflater layoutInflater;

    public RecyclerViewAdapter(Context context, List<Item> itemList) {
        this.context = context;
        this.itemList = itemList;
    }

    @NonNull
    @Override
    public RecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_row, parent, false);

        return new ViewHolder(view,context);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewAdapter.ViewHolder holder, int position) {

        Item item = itemList.get(position);

        holder.itemName.setText(MessageFormat.format("Item: {0}", item.getItemName()));
        holder.itemColor.setText(MessageFormat.format("Color: {0}", item.getItemColor()));
        holder.quantity.setText(MessageFormat.format("Qty: {0}", String.valueOf(item.getItemQuantity())));
        holder.size.setText(MessageFormat.format("Size: {0}", String.valueOf(item.getItemSize())));
        holder.dateAdded.setText(MessageFormat.format("Added on: {0}", item.getDateItemAdded()));

        Log.d(TAG, "onBindViewHolder: "+ item.getItemColor());


    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView itemName;
        public TextView itemColor;
        public TextView quantity;
        public TextView size;
        public TextView dateAdded;
        public Button editButton;
        public Button deleteButton;
        public int id;

        public ViewHolder(@NonNull View itemView, Context ctx) {
            super(itemView);

            context = ctx;

            itemName = itemView.findViewById(R.id.item_name);
            itemColor = itemView.findViewById(R.id.item_color);
            quantity = itemView.findViewById(R.id.item_quantity);
            size = itemView.findViewById(R.id.item_size);
            dateAdded = itemView.findViewById(R.id.item_date);
            editButton = itemView.findViewById(R.id.deleteButton);
            deleteButton = itemView.findViewById(R.id.editButton);
            editButton.setOnClickListener(this);
            deleteButton.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            int position;
            position = getAdapterPosition();
            Item item = itemList.get(position);
            switch (v.getId()) {
                case R.id.editButton:
                    //edit item
                    editItem(item);
                    break;
                case R.id.deleteButton:
                    //delete item
                    deleteItem(item.getId());

                    break;
            }
        }

        private void editItem(final Item newItem) {

            builder = new Builder(context);
            layoutInflater = LayoutInflater.from(context);
            final View view = layoutInflater.inflate(R.layout.poup,null);

            final EditText item;
            final EditText itemQuantity;
            final EditText itemColor;
            final EditText itemSize;
            TextView title;
            Button saveButton;

            item = view.findViewById(R.id.item);
            itemColor = view.findViewById(R.id.itemColor);
            itemQuantity = view.findViewById(R.id.itemQuantity);
            itemSize = view.findViewById(R.id.itemSize);
            title = view.findViewById(R.id.title);
            saveButton = view.findViewById(R.id.saveButton);

            title.setText(R.string.edit_item);
            saveButton.setText(R.string.update_text);

            item.setText(newItem.getItemName());
            itemColor.setText(newItem.getItemColor());
            itemQuantity.setText(String.valueOf(newItem.getItemQuantity()));
            itemSize.setText(String.valueOf( newItem.getItemSize()));

            builder.setView(view);
            alertDialog = builder.create();
            alertDialog.show();

            saveButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //update our items

                    DatabaseHandler db = new DatabaseHandler(context);

                    //update our items

                    newItem.setItemName(item.getText().toString().trim());
                    newItem.setItemQuantity(Integer.valueOf(itemQuantity.getText().toString()));
                    newItem.setItemColor(itemColor.getText().toString().trim());
                    newItem.setItemSize(Integer.valueOf(itemSize.getText().toString()));


                    if (!item.getText().toString().isEmpty()
                            && !itemColor.getText().toString().isEmpty()
                            && !itemQuantity.getText().toString().isEmpty()
                            && !itemSize.getText().toString().isEmpty()) {
                        db.updateItem(newItem);
                        notifyItemChanged(getAdapterPosition(),newItem); // pass actual ob ject changed

                    } else {
                        Snackbar.make(view,"Fields Empty",Snackbar.LENGTH_SHORT).show();
                    }

                    alertDialog.dismiss();


                }
            });

        }

        private void deleteItem(final int id) {

            builder = new AlertDialog.Builder(context);

            layoutInflater = LayoutInflater.from(context);

            View view = layoutInflater.inflate(R.layout.confirmation_pop, null);

            builder.setView(view);

            alertDialog = builder.create();
            alertDialog.show();

            Button yesButton = view.findViewById(R.id.conf_yes_button);
            Button noButton = view.findViewById(R.id.conf_no_button);



            yesButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    DatabaseHandler db = new DatabaseHandler(context);

                    db.deleteItem(id);
                    itemList.remove(getAdapterPosition());
                    notifyItemRemoved(getAdapterPosition());

                    alertDialog.dismiss();
                }
            });

            noButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    alertDialog.dismiss();
                }
            });



        }

    }


}
