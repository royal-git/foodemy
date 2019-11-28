package com.example.luvyourleftovers;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

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

        Button shareButton = findViewById(R.id.share_button);
        shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                startActivity(Intent.createChooser(sharingIntent, "Share"));
            }
        });
    }
}
