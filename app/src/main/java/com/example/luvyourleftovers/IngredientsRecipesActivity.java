package com.example.luvyourleftovers;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.luvyourleftovers.shopping_cart.CartDBHelper;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.label.FirebaseVisionImageLabel;
import com.google.firebase.ml.vision.label.FirebaseVisionImageLabeler;
import com.koushikdutta.async.future.Future;
import com.koushikdutta.ion.Ion;

import org.apmem.tools.layouts.FlowLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class IngredientsRecipesActivity extends AppCompatActivity implements
    RecyclerViewAdapter.ItemClickListener {

  private static final int CAMERA_REQUEST = 1888;
  private static final int MY_CAMERA_PERMISSION_CODE = 100;
  RecyclerViewAdapter rvaAdapter;
  ArrayList<String> ingredients;
  private Button searchButton;
  CartDBHelper db;
  String formattedInput;
  FirebaseVisionImageLabeler detector;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    db = new CartDBHelper(this);

    // data to populate the RecyclerView with
    ArrayList<String > recipeHeaders = new ArrayList<>();
    ingredients = new ArrayList<>();

    //TODO: Make this pull dynamically from API (@Royal Thomas)
    //Add each of the recipe headers to the ArrayList

    recipeHeaders.add("Pikachu Fried");
    recipeHeaders.add("Chicken");
    recipeHeaders.add("Yogurt");
    recipeHeaders.add("Creme Brule");
    recipeHeaders.add("Profiterol");
    recipeHeaders.add("Pasta");
    recipeHeaders.add("Samosa");
    recipeHeaders.add("Couscous");
    recipeHeaders.add("Sandwich");
    recipeHeaders.add("Burger");
    recipeHeaders.add("Lasagna");
    recipeHeaders.add("Cheesecake");
    recipeHeaders.add("Chocolate Mousse");
    recipeHeaders.add("Soup");

    // set up the RecyclerView
    RecyclerView recyclerView = findViewById(R.id.rvRecipes);
    recyclerView.setLayoutManager(new LinearLayoutManager(this));
    rvaAdapter = new RecyclerViewAdapter(this, recipeHeaders);
    rvaAdapter.setClickListener(this);
    recyclerView.setAdapter(rvaAdapter);

    // set it up to get user inputs
    final EditText ingredientInputArea = findViewById(R.id.inputBox);
    searchButton = findViewById(R.id.searchButton);
    Button photoButton = findViewById(R.id.insertPhoto);
    photoButton.setOnClickListener(v -> {
      if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
        requestPermissions(new String[]{Manifest.permission.CAMERA}, MY_CAMERA_PERMISSION_CODE);
      } else {
        Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cameraIntent, CAMERA_REQUEST);
      }
    });

    // User inserts an ingredient.
    findViewById(R.id.insertButton).setOnClickListener((view) -> {
      String input = ingredientInputArea.getText().toString();
      if (!input.isEmpty()) {
        addToContainer(ingredientInputArea.getText().toString());
        ingredientInputArea.setText("");
      }
    });

    // What happens when search button is clicked.
    searchButton.setOnClickListener((view) -> {
      // TODO add the ingredient to list of previously searched ingredients.
      performSearch();
    });

  }

  @Override
  public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
      @NonNull int[] grantResults) {
    super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    if (requestCode == MY_CAMERA_PERMISSION_CODE) {
      if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
        Toast.makeText(this, getString(R.string.camera_permission_ok), Toast.LENGTH_LONG).show();
        Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cameraIntent, CAMERA_REQUEST);
      } else {
        Toast.makeText(this, getString(R.string.camera_permission_denied), Toast.LENGTH_LONG).show();
      }
    }
  }


  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {
      Bitmap photo = (Bitmap) Objects.requireNonNull(data.getExtras()).get("data");
      FirebaseVisionImage image = FirebaseVisionImage.fromBitmap(photo);
      detector = FirebaseVision.getInstance()
          .getCloudImageLabeler();
      callDetector(image);
    }
  }


  public void performSearch() {
    formattedInput = android.text.TextUtils.join(",", ingredients);
    Toast.makeText(this, formattedInput, Toast.LENGTH_SHORT).show();

  }


  public void addToContainer(String text) {
    ingredients.add(text);
    searchButton.setVisibility(View.VISIBLE);
    Button newIngredientButton = new Button(this);
    newIngredientButton.setText(text);
    final FlowLayout flowLayout = findViewById(R.id.flowLayout);
    flowLayout.addView(newIngredientButton);

    newIngredientButton.setOnClickListener((v) -> {
      ingredients.remove(newIngredientButton.getText());

      flowLayout.removeView(v);
      if (ingredients.size() == 0) {
        searchButton.setVisibility(View.GONE);
      }
    });

    Toast.makeText(this, ingredients.toString(), Toast.LENGTH_SHORT).show();
  }

  //TODO: Grab data on link in API Response header (@Royal Thomas is this your part?)
  @Override
  public void onItemClick(View view, int position) {
    //Sending toast message, but it can also call a method to execute any intent/function call available in-app
    Toast.makeText(this,
        "You clicked " + rvaAdapter.getItem(position) + " on row number " + (position + 1),
        Toast.LENGTH_SHORT).show();
  }


  protected void callDetector(FirebaseVisionImage image) {
    ArrayList<String> results = new ArrayList<>();
    Toast.makeText(this, "Generating Result, Hold on!", Toast.LENGTH_SHORT).show();

    Task<List<FirebaseVisionImageLabel>> result =
        detector.processImage(image)
            .addOnSuccessListener(
                labels -> {
                  handleImageLebels(labels);
                })
            .addOnFailureListener(
                new OnFailureListener() {
                  @Override
                  public void onFailure(@NonNull Exception e) {
                    Toast.makeText(IngredientsRecipesActivity.this, "Error Fetching from Vision API.",
                        Toast.LENGTH_SHORT).show();
                  }
                });

  }

  public void handleImageLebels(List<FirebaseVisionImageLabel> labels) {
    ArrayList<String> results = new ArrayList<>();

    for (FirebaseVisionImageLabel label : labels) {
      String text = label.getText();
      if (isRecognizedIngredient(text)) {
        results.add(text);
      }
    }

    if (results.size() > 0) {
      AlertDialog.Builder builder = new AlertDialog.Builder(this);
      ArrayList<String> selections = new ArrayList<>();
      builder.setTitle("Find any items?")
          .setPositiveButton("Add to list!", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
              selections.forEach((value) -> {
                addToContainer(value);
              });
            }
          })
          .setCancelable(false)
          .setNegativeButton("Cancel", null)
          .setMultiChoiceItems(results.toArray(new String[0]), null,
              new DialogInterface.OnMultiChoiceClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which,
                    boolean isChecked) {
                  selections.add(results.get(which));
                }
              });
      builder.show();
    } else {
      Toast.makeText(this, "Didn't find any ingredients, oops!",
          Toast.LENGTH_SHORT).show();
    }
  }

  Boolean isRecognizedIngredient(String ingredient) {
    Boolean validIngredient = false;
    Future<String> result = Ion.with(this)
        .load("http://royalthomas.me/checkIngredient.php?ingredient=" + uriEncode(ingredient))
        .asString();
    try {
      if (result.get().contains("True")) {
        validIngredient = true;
      }

    } catch (Exception ex) {
      Log.d("IngredientsRecipesActivity", "Ran into issue with checking if ingredient valid");
    }
    return validIngredient;
  }

  String uriEncode(String input) {
    return input.replaceAll(" ", "+").toLowerCase();
  }


}