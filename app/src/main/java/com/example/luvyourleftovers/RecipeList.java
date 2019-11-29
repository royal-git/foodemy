package com.example.luvyourleftovers;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.luvyourleftovers.basic_classes.RecipeObject;
import java.util.ArrayList;

public class RecipeList extends AppCompatActivity implements  RecyclerViewAdapter.ItemClickListener{

    RecyclerViewAdapter rvaAdapter;
    ArrayList<RecipeObject> recipeHeaders;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_list);
        Intent intent = getIntent();

        Bundle message = intent.getExtras();
//        Bundle myExtras = getIntent().getExtras();

        recipeHeaders = (ArrayList<RecipeObject>) intent.getSerializableExtra("recipeHeaders");
//        for(String recipe: recipeHeaders){
//            Log.d("recipe", recipe);
//        }

        String recipeListHeading = getIntent().getStringExtra("RecipeTypes");
        TextView header = (TextView)findViewById(R.id.textView);

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

    @Override
    public void onItemClick(View view, int position) {
        //For the RecyclerView

//        Toast.makeText(this,
//                "You clicked " + rvaAdapter.getItem(position) + " on row number " + (position + 1),
//                Toast.LENGTH_SHORT).show();
        RecipeObject recipe = recipeHeaders.get(position);
        int recipeId = recipeHeaders.get(position).getRecipeId();
        Toast.makeText(this, "You Clicked on Recipe: "+recipeHeaders.get(position).getName()
                +" RecipeID: "+Integer.toString(recipeId), Toast.LENGTH_SHORT).show();


        Intent intent = new Intent(this, ViewRecipe.class);
        intent.putExtra("id", recipeId);
        intent.putExtra("recipe",recipe);
        startActivity(intent);

    }
}
