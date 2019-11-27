package com.example.luvyourleftovers.basic_classes;


import android.content.Context;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.Future;
import com.koushikdutta.ion.Ion;

import java.util.ArrayList;

/**
 * Class to call the Spoonacular API
 */
public class APICaller {
    private String endpoint = "https://api.spoonacular-recipe-food-nutrition-v1.p.rapidapi.com/"; //spoonacular API Endpoint
    private String apiKey = "&apiKey=d***REMOVED***"; //your personal API Key (OAuth Request)
    private Context context;

    public APICaller(Context context) {
        this.context = context;
    }


    public ArrayList<Recipe> fetchRecipes(String ingredients, int limit, int ranking) {
        String path = "recipes/findByIngredients";
        Future<JsonArray> result = Ion.with(context)
                .load(endpoint + path)
                .setHeader("x-rapidapi-key", "d***REMOVED***")
                .setHeader("x-rapidapi-host", "spoonacular-recipe-food-nutrition-v1.p.rapidapi.com")
                .setBodyParameter("number", String.valueOf(limit))
                .setBodyParameter("ranking", String.valueOf(ranking))
                .setBodyParameter("ignorePantry", "false")
                .setBodyParameter("ingredients", ingredients)
                .asJsonArray();

        try {
            System.out.println(result.get().toString());
        }catch (Exception ex){
            ex.printStackTrace();
        }

        return null;
    }



}
