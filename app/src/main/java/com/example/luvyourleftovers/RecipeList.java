package com.example.luvyourleftovers;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.luvyourleftovers.basic_classes.Recipe;
import java.util.ArrayList;

/**
 * View that shows a list of recipes that is sent to it -> Primary purpose is to display search
 * results.
 */
public class RecipeList extends AppCompatActivity implements RecyclerViewAdapter.ItemClickListener {

  RecyclerViewAdapter rvaAdapter;
  ArrayList<Recipe> recipeHeaders;

  // Setup everything as required so that search results can be shown.
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_recipe_list);
    Intent intent = getIntent();

    Bundle message = intent.getExtras();

    recipeHeaders = (ArrayList<Recipe>) intent.getSerializableExtra("recipeHeaders");

    String recipeListHeading = getIntent().getStringExtra("RecipeTypes");
    TextView header = findViewById(R.id.textView);

    //Set the heading text of the current recipe list (favourites, search results)
    header.setText(recipeListHeading);
    /**
     * @author: Lorenzo Battilocchi
     */
    RecyclerView recyclerView = findViewById(R.id.recyclerView);
    recyclerView.setLayoutManager(new LinearLayoutManager(this));

    rvaAdapter = new RecyclerViewAdapter(this, recipeHeaders);
    recyclerView.setAdapter(rvaAdapter);

    rvaAdapter.setClickListener(this);


  }

  // When an item inside the search results is clicked
  // -> send to the screen where that recipe can be viewed
  @Override
  public void onItemClick(View view, int position) {

    Recipe recipe = recipeHeaders.get(position);
    int recipeId = recipeHeaders.get(position).getRecipeId();

    Intent intent = new Intent(this, ViewRecipe.class);
    intent.putExtra("id", recipeId);
    intent.putExtra("recipe", recipe);
    startActivity(intent);

  }
}
