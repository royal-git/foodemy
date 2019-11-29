package com.example.luvyourleftovers;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;

/**
 * Splash activity that shows the splash screen momentarily.
 */
public class SplashActivity extends AppCompatActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_splash);

  }

  // Simple button on the splash screen that launches the main activity.
  public void launchMain(View view) {
    Intent main = new Intent(this, IngredientsRecipesActivity.class);
    startActivity(main);
  }
}