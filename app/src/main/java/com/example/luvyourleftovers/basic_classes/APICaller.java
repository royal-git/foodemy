package com.example.luvyourleftovers.basic_classes;


public class APICaller {
    private String endpoint = "https://api.spoonacular.com/"; //spoonacular API Endpoint
    private String recipesNode = "recipes/"; //node within the spoonacular API endpoint
    private String queryConstructor = "search?query=***LIST OF INGREDIENTS FROM @royal10march 's part***";
    private String ignorePantryIngredients = "&ignorePantry=true"; //this just tells the API to ignore absence of standard pantry ingredients
    private String apiKey = "&apiKey=***REPLACE WITH YOUR API KEY***"; //your personal API Key (OAuth Request)

    public APICaller(String recipesNode, String queryConstructor, String ignorePantryIngredients, String apiKey){

    }
}
