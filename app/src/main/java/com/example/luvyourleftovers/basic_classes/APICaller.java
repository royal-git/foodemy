package com.example.luvyourleftovers.basic_classes;


import android.content.Context;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.util.ArrayList;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Request.Builder;
import okhttp3.Response;
//import xdroid.toaster.Toaster;

/**
 * Class to call the Spoonacular API
 */
public class APICaller {

  private Context context;
  private static final String API_KEY = "d***REMOVED***";
  private static final String API_HOST = "spoonacular-recipe-food-nutrition-v1.p.rapidapi.com";

  public APICaller(Context context) {
    this.context = context;
  }

  public interface OnReturnRecipeList {
    void onSuccess(ArrayList<RecipeObject> value);
  }

  public interface OnFetchRecipeDetails {
    void onSuccess(Boolean result);
  }


  public void getRecipeInformation(RecipeObject recipe, OnFetchRecipeDetails callback) {
    int id = recipe.getRecipeId();
    new Thread(new Runnable() {
      @Override
      public void run() {
        OkHttpClient client = new OkHttpClient();
        Request request = new Builder()
//            .url("https://spoonacular-recipe-food-nutrition-v1.p.rapidapi.com/recipes/" + id
//                + "/information")
//            .get()
//            .addHeader("x-rapidapi-host", API_HOST)
//            .addHeader("x-rapidapi-key", API_KEY)
//            .build();

            .url("https://pastebin.com/raw/wTuQ3KVX?" + id
                + "/information")
            .get()
            .addHeader("x-rapidapi-host", API_HOST)
            .addHeader("x-rapidapi-key", API_KEY)
            .build();

        try {
          Response response = client.newCall(request).execute();
          JsonElement responseJson = new JsonParser().parse(response.body().string());
          if (responseJson.isJsonObject()) {
            JsonObject element = responseJson.getAsJsonObject();
            recipe.setIsVegan(element.get("vegan").getAsBoolean());
            recipe.setTimeToCook(element.get("readyInMinutes").getAsInt());
            if (!element.get("instructions").isJsonNull()) {
              recipe.setInstructions(element.get("instructions").getAsString());
            } else {
              recipe.setInstructions(
                  "Server does not have instructions for this recipe. Seems quite simple though, "
                      + "doesn't it? Look at the image, the ingredients and do it - just do it!");
            }
            recipe.setServings(element.get("servings").getAsInt());
            recipe.setCheap(element.get("cheap").getAsBoolean());
            callback.onSuccess(true);
          }
        } catch (Exception ex) {
          ex.printStackTrace();
        }
      }
    }).start();
  }


  public void fetchRecipes(String ingredients, int limit, int ranking,
      OnReturnRecipeList callback) {
    ArrayList<RecipeObject> recipes = new ArrayList<>();

    new Thread(new Runnable() {
      @Override
      public void run() {
        OkHttpClient client = new OkHttpClient();

        Request request = new Builder()
//            .url(
//                "https://spoonacular-recipe-food-nutrition-v1.p.rapidapi.com/recipes/findByIngredients?number=" + limit + "&ranking=" + ranking
//                    + "&ignorePantry" +
//                    "=true&ingredients=" + ingredients)
//            .get()
//            .addHeader("x-rapidapi-host", API_HOST)
//            .addHeader("x-rapidapi-key", API_KEY)
//            .build();

            .url(
                "https://pastebin.com/raw/RCMnLHjf?number=" + limit + "&ranking="
                    + ranking
                    + "&ignorePantry" +
                    "=false&ingredients=" + ingredients)
            .get()
            .addHeader("x-rapidapi-host", API_HOST)
            .addHeader("x-rapidapi-key", API_KEY)
            .build();

        try {
          Response response = client.newCall(request).execute();
          JsonElement responseJson = new JsonParser().parse(response.body().string());
          System.out.println(responseJson);
          if (responseJson.isJsonArray()) {
            responseJson.getAsJsonArray().forEach((element) -> {
              recipes.add(buildRecipe(element.getAsJsonObject()));
            });
            callback.onSuccess(recipes);
          }
        } catch (Exception ex) {
          ex.printStackTrace();
          //Toaster.toast(context.getString(R.string.error_api_message));
        }
      }
    }).start();
  }


  private RecipeObject buildRecipe(JsonObject element) {
    JsonObject returnObject = element.getAsJsonObject();
    String name = returnObject.get("title").getAsString();
    Integer id = Integer.parseInt(returnObject.get("id").toString());
    String image = returnObject.get("image").getAsString();

    JsonArray missingIngredientsArray = returnObject.get("missedIngredients").getAsJsonArray();
    System.out.println(missingIngredientsArray);
    ArrayList<IngredientObject> missingIngredients = new ArrayList<>();
    for (JsonElement ingredient : missingIngredientsArray) {
      missingIngredients.add(new IngredientObject(ingredient.getAsJsonObject().get("name").getAsString(), ingredient.getAsJsonObject().get("aisle").getAsString()));
    }

    for (IngredientObject ingredient : missingIngredients) {
      System.out.println(ingredient.getName() + "->" + ingredient.getType());
    }

    RecipeObject recipe = new RecipeObject(name, id, image);
    recipe.setMissedIngredients(missingIngredients);
    return recipe;
  }


}
