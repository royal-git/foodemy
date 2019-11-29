package com.example.luvyourleftovers;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.luvyourleftovers.basic_classes.DBHelper;
import com.example.luvyourleftovers.basic_classes.Recipe;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;

public class FavouritesList extends AppCompatActivity {

  private RecyclerView recyclerView;
  private RecyclerView.Adapter adapter;
  private RecyclerView.LayoutManager layoutManager;
  private ArrayList<Recipe> data;
  private DBHelper db;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_favourites_list);

    // Setup the required things that we need to show the list of item.s
    db = new DBHelper(this);
    recyclerView = (RecyclerView) findViewById(R.id.rv_favourites);
    recyclerView.setHasFixedSize(true);
    layoutManager = new LinearLayoutManager(this);
    recyclerView.setLayoutManager(layoutManager);
    recyclerView.setItemAnimator(new DefaultItemAnimator());
    data = db.getAllRecipes();
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

    private ArrayList<Recipe> dataSet;
    DBHelper db;

    public class MyViewHolder extends RecyclerView.ViewHolder {

      TextView textViewName;
      ImageView imageViewIcon;
      Button removeButton;
      RelativeLayout container;

      public MyViewHolder(View itemView) {
        super(itemView);
        this.textViewName = (TextView) itemView.findViewById(R.id.itemName);
        this.imageViewIcon = (ImageView) itemView.findViewById(R.id.itemImage);
        this.removeButton = (Button) itemView.findViewById(R.id.removeItemButton);
        this.container = itemView.findViewById(R.id.single_favourites_item);
      }
    }

    public CustomAdapter(Context context, ArrayList<Recipe> data) {
      db = new DBHelper(context);
      this.dataSet = db.getAllRecipes();
      dataSet.forEach((item) -> {
        System.out.println(item.getName() + "->" + item.getImageLink());
      });
      System.out.println("TESTSTSIOETJSOT" + dataSet);
      Log.d("Verify", dataSet.toString());
    }

    // Basically inflates the child or item class
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent,
        int viewType) {
      View view = LayoutInflater.from(parent.getContext())
          .inflate(R.layout.favourites_item, parent, false);
      MyViewHolder myViewHolder = new MyViewHolder(view);
      return myViewHolder;
    }

    // Handles each item and how it's displayed in the list
    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int listPosition) {

      TextView textViewName = holder.textViewName;
      Button removeButton = holder.removeButton;
      ImageView imageView = holder.imageViewIcon;
      RelativeLayout item = holder.container;

      String imageUrl = dataSet.get(listPosition).getImageLink();
      textViewName.setText(dataSet.get(listPosition).getName());

      Picasso.get().load(imageUrl).into(imageView);

      // Handler for when a recipe is to be removed from the cart.
      removeButton.setOnClickListener((View) -> {
        DBHelper db = new DBHelper(View.getContext());
        db.delete(dataSet.get(listPosition));
        dataSet.remove(listPosition);
        notifyDataSetChanged();
      });

      item.setOnClickListener(new OnClickListener() {
        @Override
        public void onClick(View view) {
          Recipe recipe = dataSet.get(listPosition);
          int recipeId = dataSet.get(listPosition).getRecipeId();
          Intent intent = new Intent(getApplicationContext(), ViewRecipe.class);
          intent.putExtra("id", recipeId);
          intent.putExtra("recipe", recipe);
          startActivity(intent);
        }
      });

    }


    // Return the number of items in the cart.
    @Override
    public int getItemCount() {
      return dataSet.size();
    }
  }
}
