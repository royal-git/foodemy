package com.example.luvyourleftovers;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
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


        JsonObject json = new JsonObject();
        json.addProperty("foo", "bar");
        Ion.getDefault(this).getConscryptMiddleware().enable(false);


        // Load the Instructions.
        Ion.with(this)
                .load("https://api.spoonacular.com/recipes/324694/analyzedInstructions?stepBreakdown=false&apiKey=9f55e2d2d78c44c3b6ad2799f349904d").asString().setCallback(new FutureCallback<String>() {

            @Override
            public void onCompleted(Exception e, String result) {
                try {
                    JSONArray recipes = new JSONArray(result);
                    for (int i = 0; i < recipes.length(); i++) {
                        JSONArray steps = recipes.getJSONObject(i).getJSONArray("steps");
                        for (int j = 0; j < steps.length(); j++) {
                            instructions.append(steps.getJSONObject(j).get("step") + "\n\n");
                        }
                    }
                    TextView instructionsTextView = findViewById(R.id.instructions);
                    instructionsTextView.setText(instructions);
                    System.out.println(instructions);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }

            }
        });


        // Set the text box to this value;
    }
}
