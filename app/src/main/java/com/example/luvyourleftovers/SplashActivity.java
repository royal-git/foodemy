package com.example.luvyourleftovers;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        launchMain();



    }

    // Simple button on the splash screen that launches the main activity.
    public void launchMain()
    {
        Intent main = new Intent(this, IngredientsRecipesActivity.class);
        startActivity(main);
    }
}
