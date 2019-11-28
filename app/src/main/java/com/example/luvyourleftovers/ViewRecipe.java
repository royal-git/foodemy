package com.example.luvyourleftovers;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import com.example.luvyourleftovers.basic_classes.APICaller;
import com.example.luvyourleftovers.basic_classes.APICaller.OnFetchRecipeDetails;
import com.example.luvyourleftovers.basic_classes.FavouritesDB;
import com.example.luvyourleftovers.basic_classes.RecipeObject;
import com.google.android.material.button.MaterialButton;
import com.squareup.picasso.Picasso;

public class ViewRecipe extends AppCompatActivity {

    FavouritesDB favouritesDB;
    RecipeObject recipe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_recipe);
        favouritesDB = new FavouritesDB(this);
        String title = null;
        StringBuilder instructions = new StringBuilder();
        MaterialButton likeButton = findViewById(R.id.like_button);

        // Get the Intent that started this activity and extract the string
        Intent intent = getIntent();
        Bundle message = intent.getExtras();
        recipe = (RecipeObject) intent.getSerializableExtra("recipe");
        TextView titleView = findViewById(R.id.RecipeDisplayText);
        titleView.setText(recipe.getName());

        if (!recipe.getImageLink().isEmpty()) {
            Picasso.get().load(recipe.getImageLink())
                .into((ImageView) findViewById(R.id.recipeImage));
        }

        // Code for dealing with favourites.
        setupLikeButton(likeButton);

        // Add/Remove to favourites based on user action.

        // Check if recipe has instructions already.
        if (recipe.getInstructions().isEmpty()) {
            new APICaller(this).getRecipeInformation(recipe, new OnFetchRecipeDetails() {
                @Override
                public void onSuccess(Boolean result) {
                    updateTextViews(recipe);
                }
            });
        }


      likeButton.setOnClickListener((View) -> {
        if(!favouritesDB.ifExists(recipe)){
          favouritesDB.insertRecipe(recipe);
        }else{
          favouritesDB.deleteRecipe(recipe);
        }
        setupLikeButton(likeButton);
      });

//
//
//        Ion.getDefault(this).getConscryptMiddleware().enable(false);
//        Ion.with(this)
//                .load("https://spoonacular-recipe-food-nutrition-v1.p.rapidapi.com/recipes/1003464/information")
//                .setHeader("x-rapidapi-key", "d***REMOVED***")
//                .setHeader("x-rapidapi-host", "spoonacular-recipe-food-nutrition-v1.p.rapidapi.com")
//                .asJsonObject()
//                .setCallback(new FutureCallback<JsonObject>() {
//                    @Override
//                    public void onCompleted(Exception e, JsonObject result) {
//                        try {
//                            TextView instructionsTextView = findViewById(R.id.instructions);
//                            TextView timeToCook = findViewById(R.id.timeToCook);
//                            TextView servings = findViewById(R.id.servings);
//                            TextView vegetarian = findViewById(R.id.vegetarian);
//                            TextView costMeasure = findViewById(R.id.costRating);
//                            String veg = result.get("vegan").getAsBoolean() ? "Vegan" : "Non-Vegan";
//
//                            instructionsTextView.setText(Html.fromHtml(result.get("instructions").getAsString()));
//                            timeToCook.setText("Ready in " + result.get("readyInMinutes").toString() + " minutes!");
//                            servings.setText("Servings: " + result.get("servings").toString());
//                            vegetarian.setText(veg);
//                            costMeasure.setText("Cost: $$/$$$$$");
//
//
//                        } catch (Exception ex) {
//                            Toast.makeText(ViewRecipe.this, "Error Loading from API, please try again.", Toast.LENGTH_SHORT).show();
//                        }
//
//
//                    }
//
//                });


        // Set the text box to this value;
    }

    private void updateTextViews(RecipeObject recipe) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                TextView instructionsTextView = findViewById(R.id.instructions);
                TextView timeToCook = findViewById(R.id.timeToCook);
                TextView servings = findViewById(R.id.servings);
                TextView vegetarian = findViewById(R.id.vegetarian);
                TextView costMeasure = findViewById(R.id.costRating);

                instructionsTextView.setText(recipe.getInstructions());
                timeToCook.setText("Ready in " + recipe.getTimeToCook() + " minutes!");
                servings.setText("Servings: 8");
                String veg = recipe.isVegan() ? "Vegan" : "Non-Vegan";
                vegetarian.setText(veg);
                costMeasure.setText("Cost: $$/$$$$$");
            }
        });
    }

    public void setupLikeButton(MaterialButton button) {
        if (favouritesDB.ifExists(recipe)) {
            button
                .setIconTint(ColorStateList.valueOf(ContextCompat.getColor(this, R.color.red)));
        }else{

            button
                .setIconTint(ColorStateList.valueOf(ContextCompat.getColor(this, R.color.grey)));
        }
    }
}
