package com.example.luvyourleftovers.basic_classes;


/**
 * Class to call the Spoonacular API
 */
public class APICaller {
    private String endpoint = "https://api.spoonacular-recipe-food-nutrition-v1.p.rapidapi.com/"; //spoonacular API Endpoint
    private String recipesNode = "recipes/"; //node within the spoonacular API endpoint
    private String queryConstructor = "search?query=***LIST OF INGREDIENTS FROM @royal10march 's part***";
    private String ignorePantryIngredients = "&ignorePantry=true"; //this just tells the API to ignore absence of standard pantry ingredients
    private String apiKey = "&apiKey=***REPLACE WITH YOUR API KEY***"; //your personal API Key (OAuth Request)

    /**
     * Constructor to add the query to the DB
     * @param queryConstructor
     */
    public APICaller(String queryConstructor){
        this.queryConstructor = queryConstructor;
    }

    public void postRequest(){

    }


}
