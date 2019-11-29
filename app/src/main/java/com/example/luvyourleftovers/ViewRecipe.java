package com.example.luvyourleftovers;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import com.example.luvyourleftovers.basic_classes.APICaller;
import com.example.luvyourleftovers.basic_classes.APICaller.OnFetchRecipeDetails;
import com.example.luvyourleftovers.basic_classes.FavouritesDB;
import com.example.luvyourleftovers.basic_classes.IngredientObject;
import com.example.luvyourleftovers.basic_classes.RecipeObject;
import com.example.luvyourleftovers.shopping_cart.CartDBHelper;
import com.example.luvyourleftovers.shopping_cart.CartItem;
import com.google.android.material.button.MaterialButton;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;
import xdroid.toaster.Toaster;

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
                ArrayList<IngredientObject> missingIngredients = recipe.getMissedIngredients();
                ArrayList<IngredientObject> usedIngredients = recipe.getUsedIngredients();

                System.out.println(recipe.getMissedIngredients());
                instructionsTextView.setText(recipe.getInstructions());
                timeToCook.setText("Ready in " + recipe.getTimeToCook() + " minutes!");
                servings.setText("Servings: " + recipe.getServings());
                String veg = recipe.isVegan() ? "Vegan" : "Non-Vegan";
                vegetarian.setText(veg);
                costMeasure.setText(recipe.isCheap() ? "$$" : "$$$$");

                LinearLayout layout = (LinearLayout)findViewById(R.id.missing_ingredients_layout);
                for (IngredientObject ingredient : missingIngredients) {

                    // Setup the views and add it as a child to the parent for missing ingredients.
                    View child = getLayoutInflater().inflate(R.layout.single_ingredient, null);
                    ImageView imgView = child.findViewById(R.id.single_ingredient_image);
                    TextView textView = child.findViewById(R.id.single_ingredient_text);
                    Picasso.get().load(ingredient.getImageUrl()).into(imgView);
                    textView.setText(ingredient.getName());

                    // Set onclick listener to add to cart.
                    child.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            alertConfirmationDialog(ingredient.getName(), ingredient.getImageUrl());
                        }
                    });
                    layout.addView(child);
                }

                layout = (LinearLayout) findViewById(R.id.used_ingredients_layout);
                for (IngredientObject ingredient : usedIngredients) {

                    // Setup the views and add it as a child to the parent for missing ingredients.
                    View child = getLayoutInflater().inflate(R.layout.single_ingredient, null);
                    ImageView imgView = child.findViewById(R.id.single_ingredient_image);
                    TextView textView = child.findViewById(R.id.single_ingredient_text);
                    Picasso.get().load(ingredient.getImageUrl()).into(imgView);
                    textView.setText(ingredient.getName());

                    layout.addView(child);
                }

            }
        });
    }

    public void alertConfirmationDialog(String item, String imageUrl) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Would you like to add this to your cart?");
        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                CartDBHelper mHelper = new CartDBHelper(getApplicationContext());
                mHelper.insertItem(new CartItem(item, 1, imageUrl));
                dialog.dismiss();
                Toaster.toast("Successfully added " + item + " to the cart!");
            }
        });

        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Do nothing
                dialog.dismiss();
            }
        });

        AlertDialog alert = builder.create();
        alert.show();
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
