package com.example.luvyourleftovers.shopping_cart;

import android.widget.Toolbar;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.luvyourleftovers.R;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class ShoppingCart extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<CartItem> data;
    private CartDBHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_cart);





        db = new CartDBHelper(this);
        recyclerView = (RecyclerView) findViewById(R.id.rv_shoppingcart);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        data = db.getAllItems();
        adapter = new CustomAdapter(this, data);
        recyclerView.setAdapter(adapter);

    }

    @Override
    protected void onResume() {
        super.onResume();
        System.out.println(db.getAllItems().size());
    }
}

class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.MyViewHolder> {

    private ArrayList<CartItem> dataSet;
    CartDBHelper db;

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView textViewName;
        TextView textViewQauntity;
        ImageView imageViewIcon;
        Button removeButton;

        public MyViewHolder(View itemView) {
            super(itemView);
            this.textViewName = (TextView) itemView.findViewById(R.id.itemName);
            this.textViewQauntity = (TextView) itemView.findViewById(R.id.itemQuantity);
            this.removeButton = (Button) itemView.findViewById(R.id.removeItemButton);
        }
    }

    public CustomAdapter(Context context, ArrayList<CartItem> data) {
        db = new CartDBHelper(context);
        this.dataSet = db.getAllItems();


    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent,
                                           int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cart_item, parent, false);
        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int listPosition) {

        TextView textViewName = holder.textViewName;
        TextView textViewQuantity = holder.textViewQauntity;
        Button removeButton = holder.removeButton;
        ImageView imageView = holder.imageViewIcon;
        Integer id = dataSet.get(listPosition).getId();
        textViewName.setText(dataSet.get(listPosition).getName());
        textViewQuantity.setText("Quantity: " + Integer.toString(dataSet.get(listPosition).getQuantity()));

        removeButton.setOnClickListener((View) -> {
            CartDBHelper db = new CartDBHelper(View.getContext());

            db.removeItem(id);
            dataSet.remove(listPosition);
            notifyDataSetChanged();

        });
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }
}
