package com.example.luvyourleftovers.basic_classes;


import android.content.Context;

import com.example.luvyourleftovers.R;
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
        Request request = new Request.Builder()
            .url("https://spoonacular-recipe-food-nutrition-v1.p.rapidapi.com/recipes/" + id
                + "/information")
            .get()
            .addHeader("x-rapidapi-host", "spoonacular-recipe-food-nutrition-v1.p.rapidapi.com")
            .addHeader("x-rapidapi-key", "d***REMOVED***")
            .build();

        try {
          Response response = client.newCall(request).execute();
          JsonElement responseJson = new JsonParser().parse(response.body().string());
          if (responseJson.isJsonObject()) {
            JsonObject element = responseJson.getAsJsonObject();
            System.out.println(element);
            recipe.setIsVegan(element.get("vegan").getAsBoolean());
            recipe.setTimeToCook(element.get("readyInMinutes").getAsInt());
            recipe.setInstructions(element.get("instructions").getAsString());
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
            .url(
                "https://spoonacular-recipe-food-nutrition-v1.p.rapidapi.com/recipes/findByIngredients?number=" + limit + "&ranking=" + ranking
                    + "&ignorePantry" +
                    "=false&ingredients=" + ingredients)
            .get()
            .addHeader("x-rapidapi-host", "spoonacular-recipe-food-nutrition-v1.p.rapidapi.com")
            .addHeader("x-rapidapi-key", "d***REMOVED***")
            .build();

        try {
          Response response = client.newCall(request).execute();
          JsonElement responseJson = new JsonParser().parse(response.body().string());
          System.out.println(responseJson);
          if (responseJson.isJsonArray()) {
            responseJson.getAsJsonArray().forEach((element) -> {
              JsonObject returnObject = element.getAsJsonObject();
              String name = returnObject.get("title").toString();
              Integer id = Integer.parseInt(returnObject.get("id").toString());
              String image = returnObject.get("image").toString();
              recipes.add(new RecipeObject(name, id, image));
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


}
