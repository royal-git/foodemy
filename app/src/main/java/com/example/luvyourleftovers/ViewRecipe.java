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
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import com.example.luvyourleftovers.basic_classes.APICaller;
import com.example.luvyourleftovers.basic_classes.APICaller.OnFetchRecipeDetails;
import com.example.luvyourleftovers.basic_classes.DBHelper;
import com.example.luvyourleftovers.basic_classes.Ingredient;
import com.example.luvyourleftovers.basic_classes.Recipe;
import com.example.luvyourleftovers.shopping_cart.CartItem;
import com.google.android.material.button.MaterialButton;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;
import xdroid.toaster.Toaster;

public class ViewRecipe extends AppCompatActivity {

    DBHelper db;
    Recipe recipe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_recipe);
        db = new DBHelper(this);
        String title = null;
        StringBuilder instructions = new StringBuilder();
        MaterialButton likeButton = findViewById(R.id.like_button);

        // Get the Intent that started this activity and extract the string
        Intent intent = getIntent();
        Bundle message = intent.getExtras();
        recipe = (Recipe) intent.getSerializableExtra("recipe");
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
          if (!db.exists(recipe)) {
              db.insert(recipe);
        }else{
              db.delete(recipe);
        }
        setupLikeButton(likeButton);
      });

    }

    private void updateTextViews(Recipe recipe) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                TextView instructionsTextView = findViewById(R.id.instructions);
                TextView timeToCook = findViewById(R.id.timeToCook);
                TextView servings = findViewById(R.id.servings);
                TextView vegetarian = findViewById(R.id.vegetarian);
                TextView costMeasure = findViewById(R.id.costRating);
                ArrayList<Ingredient> missingIngredients = recipe.getMissedIngredients();
                ArrayList<Ingredient> usedIngredients = recipe.getUsedIngredients();

                System.out.println(recipe.getMissedIngredients());
                instructionsTextView.setText(recipe.getInstructions());
                timeToCook.setText("Ready in " + recipe.getTimeToCook() + " minutes!");
                servings.setText("Servings: " + recipe.getServings());
                String veg = recipe.isVegan() ? "Vegan" : "Non-Vegan";
                vegetarian.setText(veg);
                costMeasure.setText(recipe.isCheap() ? "$$" : "$$$$");

                LinearLayout layout = (LinearLayout)findViewById(R.id.missing_ingredients_layout);
                for (Ingredient ingredient : missingIngredients) {

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
                for (Ingredient ingredient : usedIngredients) {

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
        builder.setMessage("Would you like to add " + item + " to your cart?");
        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                db.insert(new CartItem(item, 1, imageUrl));
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
        if (db.exists(recipe)) {
            button
                .setIconTint(ColorStateList.valueOf(ContextCompat.getColor(this, R.color.red)));
        }else{
            button
                .setIconTint(ColorStateList.valueOf(ContextCompat.getColor(this, R.color.grey)));
        }
    }
}
