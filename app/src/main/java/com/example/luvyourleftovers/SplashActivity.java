package com.example.luvyourleftovers;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.luvyourleftovers.shopping_cart.ShoppingCart;

public class SplashActivity extends AppCompatActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
    }

    // Simple button on the splash screen that launches the main activity.
    public void launchMain(View view)
    {
        Intent main = new Intent(this, IngredientsRecipesActivity.class);
        startActivity(main);
    }
}
