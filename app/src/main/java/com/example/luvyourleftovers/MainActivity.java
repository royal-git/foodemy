package com.example.luvyourleftovers;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.apmem.tools.layouts.FlowLayout;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements RecyclerViewAdapter.ItemClickListener {

    RecyclerViewAdapter rvaAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // data to populate the RecyclerView with
        ArrayList<String> recipeHeaders = new ArrayList<>();
        final ArrayList<String> ingredients = new ArrayList<>();

        //TODO: Make this pull dynamically from API (@Royal Thomas)
        //Add each of the recipe headers to the ArrayList
        recipeHeaders.add("Pikachu Fried");
        recipeHeaders.add("Chicken");
        recipeHeaders.add("Yogurt");
        recipeHeaders.add("Creme Brule");
        recipeHeaders.add("Profiterol");
        recipeHeaders.add("Pasta");
        recipeHeaders.add("Samosa");
        recipeHeaders.add("Couscous");
        recipeHeaders.add("Sandwich");
        recipeHeaders.add("Burger");
        recipeHeaders.add("Lasagna");
        recipeHeaders.add("Cheesecake");
        recipeHeaders.add("Chocolate Mousse");
        recipeHeaders.add("Soup");

        // set up the RecyclerView
        RecyclerView recyclerView = findViewById(R.id.rvRecipes);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        rvaAdapter = new RecyclerViewAdapter(this, recipeHeaders);
        rvaAdapter.setClickListener(this);
        recyclerView.setAdapter(rvaAdapter);

        // set it up to get user inputs
        final EditText ingredientInputArea = findViewById(R.id.inputBox);
        Button searchButton = findViewById(R.id.searchButton);

        // User inserts an ingredient.
        findViewById(R.id.insertButton).setOnClickListener((view) -> {
            String input = ingredientInputArea.getText().toString();
            if (!input.isEmpty()) {
                addToContainer(ingredientInputArea.getText().toString(), ingredients);
                ingredientInputArea.setText("");
            }
        });

        // What happens when search button is clicked.
        searchButton.setOnClickListener((view) -> {
            // TODO add the ingredient to list of previously searched ingredients.
            performSearch(ingredients);
        });

    }

    /**
     * To be implemented, it just joins the arraylist using ,
     */
    public void performSearch(ArrayList<String> input) {
        String formattedInput = android.text.TextUtils.join(",", input);
        Toast.makeText(this, formattedInput, Toast.LENGTH_SHORT).show();
    }


    public void addToContainer(String text, final ArrayList<String> ingredients) {
        ingredients.add(text);
        Button newIngredientButton = new Button(this);
        newIngredientButton.setText(text);
        final FlowLayout flowLayout = findViewById(R.id.flowLayout);
        flowLayout.addView(newIngredientButton);

        newIngredientButton.setOnClickListener((v) -> {
            ingredients.remove(newIngredientButton.getText());
            flowLayout.removeView(v);
        });

        Toast.makeText(this, ingredients.toString(), Toast.LENGTH_SHORT).show();
    }

    //TODO: Grab data on link in API Response header (@Royal Thomas is this your part?)
    @Override
    public void onItemClick(View view, int position) {
        //Sending toast message, but it can also call a method to execute any intent/function call available in-app
        Toast.makeText(this, "You clicked " + rvaAdapter.getItem(position) + " on row number " + (position + 1), Toast.LENGTH_SHORT).show();
    }
}