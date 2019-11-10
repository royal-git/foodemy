package com.example.luvyourleftovers;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements RecyclerViewAdapter.ItemClickListener {

    RecyclerViewAdapter rvaAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // data to populate the RecyclerView with
        ArrayList<String> recipeHeaders = new ArrayList<>();

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
    }

    @Override
    public void onItemClick(View view, int position) {
        //Sending toast message, but it can also call a method to execute any intent/function call available in-app
        Toast.makeText(this, "You clicked " + rvaAdapter.getItem(position) + " on row number " + (position+1), Toast.LENGTH_SHORT).show();
    }
}