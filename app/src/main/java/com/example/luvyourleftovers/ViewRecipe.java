package com.example.luvyourleftovers;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.Future;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Text;

public class ViewRecipe extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_recipe);

        int recipeIdentifier = 0;
        String title = null;
        StringBuilder instructions = new StringBuilder();


        // Get the Intent that started this activity and extract the string
        Intent intent = getIntent();
        Bundle message = intent.getExtras();
        if (message != null) {
            recipeIdentifier = message.getInt("id");
            title = message.getString("title");
            TextView titleView = findViewById(R.id.RecipeDisplayText);
            titleView.setText(title);
        }


        Ion.getDefault(this).getConscryptMiddleware().enable(false);
        Ion.with(this)
                .load("https://spoonacular-recipe-food-nutrition-v1.p.rapidapi.com/recipes/1003464/information")
                .setHeader("x-rapidapi-key", "d***REMOVED***")
                .setHeader("x-rapidapi-host", "spoonacular-recipe-food-nutrition-v1.p.rapidapi.com")
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        try {
                            TextView instructionsTextView = findViewById(R.id.instructions);
                            TextView timeToCook = findViewById(R.id.timeToCook);
                            TextView servings = findViewById(R.id.servings);
                            TextView vegetarian = findViewById(R.id.vegetarian);
                            TextView costMeasure = findViewById(R.id.costRating);
                            String veg = result.get("vegan").getAsBoolean() ? "Vegan" : "Non-Vegan";

                            instructionsTextView.setText(Html.fromHtml(result.get("instructions").getAsString()));
                            timeToCook.setText("Ready in " + result.get("readyInMinutes").toString() + " minutes!");
                            servings.setText("Servings: " + result.get("servings").toString());
                            vegetarian.setText(veg);
                            costMeasure.setText("Cost: $$/$$$$$");


                        } catch (Exception ex) {
                            Toast.makeText(ViewRecipe.this, "Error Loading from API, please try again.", Toast.LENGTH_SHORT).show();
                        }


                    }

                });


        // Set the text box to this value;
    }
}
