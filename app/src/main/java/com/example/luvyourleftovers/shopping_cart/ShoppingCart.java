package com.example.luvyourleftovers.shopping_cart;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.luvyourleftovers.FindShops;
import com.example.luvyourleftovers.R;
import com.example.luvyourleftovers.basic_classes.DBHelper;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;

/**
 * This class is responsible for the shopping cart view, it also has a child class that is
 * responsible for drawing the list of items in the cart. This view uses a local SQLite storage in
 * order to fetch the items in the cart.
 */
public class ShoppingCart extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<CartItem> data;
    private DBHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_cart);

        // Setup the required things that we need to show the list of item.s
        db = new DBHelper(this);
        recyclerView = (RecyclerView) findViewById(R.id.rv_shoppingcart);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        data = db.getAllCartItems();
        adapter = new CustomAdapter(this, data);
        recyclerView.setAdapter(adapter);

    }


    // Just a method that is called when the button is clicked to show shops that are nearby.
    public void openShops(View view) {
        Intent intent = new Intent(this, FindShops.class);
        startActivity(intent);
    }


    @Override
    protected void onResume() {
        super.onResume();
    }


    // Adapter that handles the RecyclerView for the shopping List.
    class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.MyViewHolder> {

        private ArrayList<CartItem> dataSet;
        DBHelper db;

        public class MyViewHolder extends RecyclerView.ViewHolder {

            TextView textViewName;
            TextView textViewQauntity;
            ImageView imageViewIcon;
            Button removeButton;

            public MyViewHolder(View itemView) {
                super(itemView);
                this.textViewName = (TextView) itemView.findViewById(R.id.itemName);
                this.textViewQauntity = (TextView) itemView.findViewById(R.id.itemQuantity);
                this.imageViewIcon = (ImageView) itemView.findViewById(R.id.itemImage);
                this.removeButton = (Button) itemView.findViewById(R.id.removeItemButton);
            }
        }

        public CustomAdapter(Context context, ArrayList<CartItem> data) {
            db = new DBHelper(context);
            this.dataSet = db.getAllCartItems();
        }

        // Basically inflates the child or item class
        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent,
            int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cart_item, parent, false);
            MyViewHolder myViewHolder = new MyViewHolder(view);
            return myViewHolder;
        }

        // Handles each item and how it's displayed in the list
        @Override
        public void onBindViewHolder(final MyViewHolder holder, final int listPosition) {

            TextView textViewName = holder.textViewName;
            TextView textViewQuantity = holder.textViewQauntity;
            Button removeButton = holder.removeButton;
            ImageView imageView = holder.imageViewIcon;

            String imageUrl = dataSet.get(listPosition).getImageUrl();
            textViewName.setText(dataSet.get(listPosition).getName());

            Picasso.get().load(imageUrl).into(imageView);
            textViewQuantity
                .setText("Quantity: " + Integer.toString(dataSet.get(listPosition).getQuantity()));

            // Handler for when a recipe is to be removed from the cart. 
            removeButton.setOnClickListener((View) -> {
                DBHelper db = new DBHelper(View.getContext());
                db.delete(dataSet.get(listPosition));
                dataSet.remove(listPosition);
                notifyDataSetChanged();
            });
        }

        // Return the number of items in the cart.
        @Override
        public int getItemCount() {
            return dataSet.size();
        }
    }


}
