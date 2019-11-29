package com.example.luvyourleftovers;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.example.luvyourleftovers.shopping_cart.ShoppingCart;

public class SplashActivity extends AppCompatActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ImageView imageView = findViewById(R.id.imageView);
        imageView.setVisibility(View.VISIBLE);
        try {
            Thread.sleep(2000);
            launchMain();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    // Simple button on the splash screen that launches the main activity.
    public void launchMain()
    {
        Intent main = new Intent(this, IngredientsRecipesActivity.class);
        startActivity(main);
    }
}
