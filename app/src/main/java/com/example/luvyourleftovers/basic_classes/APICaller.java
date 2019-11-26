package com.example.luvyourleftovers.basic_classes;


public class APICaller {
    String endpoint = "https://api.spoonacular.com/"; //spoonacular API Endpoint
    String recipesNode = "recipes/"; //node within the spoonacular API endpoint
    String queryConstructor = "search?query=***LIST OF INGREDIENTS FROM @royal10march 's part***";
    String ignorePantryIngredients = "&ignorePantry=true"; //this just tells the API to ignore absence of standard pantry ingredients
    String apiKey = "&apiKey=***REPLACE WITH YOUR API KEY***"; //your personal API Key (OAuth Request)
}
