package com.example.luvyourleftovers;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import com.example.luvyourleftovers.basic_classes.APICaller;
import com.example.luvyourleftovers.basic_classes.APICaller.OnReturnRecipeList;
import com.example.luvyourleftovers.basic_classes.DBHelper;
import com.example.luvyourleftovers.basic_classes.Recipe;
import com.example.luvyourleftovers.shopping_cart.ShoppingCart;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.label.FirebaseVisionImageLabel;
import com.google.firebase.ml.vision.label.FirebaseVisionImageLabeler;
import com.koushikdutta.async.future.Future;
import com.koushikdutta.ion.Ion;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import org.apmem.tools.layouts.FlowLayout;
import xdroid.toaster.Toaster;


/**
 * Controller that is responsible for the home screen, it facilitates the user to input ingredients
 * through text, image and also sends the user to the search results page after.
 *
 * Uses Google's Cloud Vision API to perform image classification.
 */
public class IngredientsRecipesActivity extends AppCompatActivity {

  //set up Camera properties
  private static final int CAMERA_REQUEST = 1888;
  private static final int MY_CAMERA_PERMISSION_CODE = 100;

  ArrayList<String> ingredients;
  
  private Button searchButton;
  DBHelper db;
  
  String formattedInput;
  //used for the Google Cloud Vision utility for classifying objects in an image.
  FirebaseVisionImageLabeler detector;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    db = new DBHelper(this);

    // data to populate the RecyclerView with
    ArrayList<String > recipeHeaders = new ArrayList<>();
    ingredients = new ArrayList<>();

    // set it up to get user inputs
    final EditText ingredientInputArea = findViewById(R.id.inputBox);
    searchButton = findViewById(R.id.searchButton);
    ImageButton photoButton = findViewById(R.id.insertPhoto);

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
      String formattedInput = android.text.TextUtils.join(",", ingredients);
      Context context = this;

      Toast.makeText(this,
          "Hold up, put on thy aprons because we're fetching some delicious recipes!",
          Toast.LENGTH_LONG).show();

      new APICaller(this).fetchRecipes(formattedInput, 25, 1, new OnReturnRecipeList() {
        @Override
        public void onSuccess(ArrayList<Recipe> value) {

          // Clear past results.
          recipeHeaders.clear();

          // Insert into the recyclerview.
          for (Recipe recipe : value) {
            recipeHeaders.add(recipe.getName());
          }

          //send To Search Results List
          Intent intent = new Intent(context, RecipeList.class);
          intent.putExtra("recipeHeaders", value);
          intent.putExtra("RecipeTypes","Search Results");
          startActivity(intent);
        }
      });
    });

  }

  public void openFavourites(View view) {
    Intent intent = new Intent(this, FavouritesList.class);
    startActivity(intent);
  }

  public void openCart(View view) {
    Intent intent = new Intent(this, ShoppingCart.class);
    startActivity(intent);
  }

  // Handles the response after when the user presses the camera button to see if the user has
  // allowed the app to access the camera
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


  // This is called when the user has taken a photo, it  creates a new FireBaseVisionImage
  // of the photo and does analysis on it by calling callDetector()
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


  // Method used to add dynamically created buttons to the view, this is used to dynamically
  // insert one button per ingredient so that the user can then press these to remove these ingredients from the list.
  public void addToContainer(String text) {
    ingredients.add(text);
    searchButton.setVisibility(View.VISIBLE);

    // Setup new ingredient buttons to allow users to delete their inputs.
    MaterialButton newIngredientButton = new MaterialButton(this, null, R.attr.borderlessButtonStyle);
    newIngredientButton.setText(text);
    newIngredientButton.setTypeface(null, Typeface.NORMAL);
    newIngredientButton.setIcon(
        ResourcesCompat.getDrawable(getResources(), R.drawable.ic_remove_circle_black_24dp, null));
    newIngredientButton.setIconTintResource(R.color.red);
    newIngredientButton.setTextColor(ContextCompat.getColor(this, R.color.black));

    final FlowLayout flowLayout = findViewById(R.id.flowLayout);
    flowLayout.addView(newIngredientButton);

    newIngredientButton.setOnClickListener((v) -> {
      ingredients.remove(newIngredientButton.getText());
      flowLayout.removeView(v);
      if (ingredients.size() == 0) {
        searchButton.setVisibility(View.GONE);
      }
    });

  }


  // Method that calls cloud vision api and then fetches the labels which are then passed to the method below.
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

  // This method gets the response labels list and then makes a dialog that
  // lets users pick which of them to add to the list of ingredients.
  public void handleImageLebels(List<FirebaseVisionImageLabel> labels) {
    ArrayList<String> results = new ArrayList<>();

    // Remove random labels from the list.
    for (FirebaseVisionImageLabel label : labels) {
      String text = label.getText();
      if (isRecognizedIngredient(text)) {
        results.add(text);
      }
    }

    // Make sure that are only parsing it if we did get some response from it.
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

  // Check if the response label is a valid ingredient or just a ranom object like chair.
  // It calls this api which just compares it with a list of ingredients and checks if it is in there.
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
      Toaster.toast("There was an issue connecting to the server for checking if the ingredient is valid.");
      Log.d("IngredientsRecipesActivity", "Ran into issue with checking if ingredient valid");
    }
    return validIngredient;
  }

  String uriEncode(String input) {
    return input.replaceAll(" ", "+").toLowerCase();
  }


}