package com.example.luvyourleftovers;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

/**
 * Splash activity that shows the splash screen momentarily.
 */
public class SplashActivity extends AppCompatActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_splash);
//        ImageView imageView = findViewById(R.id.imageView);
//        imageView.setVisibility(View.VISIBLE);
    try {
      Thread.sleep(2000);
      launchMain();
    } catch (InterruptedException e) {
      e.printStackTrace();
    }

  }

  // Simple button on the splash screen that launches the main activity.
  public void launchMain() {
    Intent main = new Intent(this, IngredientsRecipesActivity.class);
    startActivity(main);
  }
}