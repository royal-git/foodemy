package com.example.luvyourleftovers;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

/**
 * Activity displays the Recipe Selected by a User.
 *  Should use Intent to transfer Recipe Objects from previous Activity to current Activity (using Intents)
 *  the previous recipe to this current activity.
 **/
public class DisplayRecipe extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_recipe);
    }
}
