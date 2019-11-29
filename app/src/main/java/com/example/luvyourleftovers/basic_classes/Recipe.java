/**
 * This is the Recipe class where a recipe object is made using the details
 * gathered from spoonacular or the FavouritesDB. This class is made
 * for Foodemy services to send each other recipe data in a compact and architecture-wide
 * understanding.
 */


package com.example.luvyourleftovers.basic_classes;

import androidx.core.text.HtmlCompat;
import java.io.Serializable;
import java.util.ArrayList;

public class Recipe implements Serializable {
    /*
     *  declare and initialize the variables of Recipe. 
     */
    //List of ingredients.
    private ArrayList<Ingredient> ingredients = new ArrayList<>();
    //List of ingredients not available by user (not mentioned by user-client).
    private ArrayList<Ingredient> missedIngredients = new ArrayList<>();
    //returns list of usedINgredients.
    public ArrayList<Ingredient> getUsedIngredients() {
        return usedIngredients;
    }

    public void setUsedIngredients(
        ArrayList<Ingredient> usedIngredients) {
        this.usedIngredients = usedIngredients;
    }

    private ArrayList<Ingredient> usedIngredients = new ArrayList<>();
    //List of Spoonacular data declared.
    private String instructions;
    private String name;
    private String image ="";
    private Boolean isVegan;
    private int timeToCook;
    private int recipeId;
    private int servings;
    private boolean cheap;

    public boolean isCheap() {
        return cheap;
    }

    public void setCheap(boolean cheap) {
        this.cheap = cheap;
    }

    /**
     * @param name name of recipe
     * @param id recipeId used to identify recipe in spoonacular.
     * @param imageUrl string path to image of the recipe.
     */
    public Recipe(String name, Integer id, String imageUrl) {
        this.image = imageUrl;
        this.name = name;
        this.recipeId = id;
    }

    /**
     * @param name name of recipe
     * @param id recipeId used for recipe identification with spoonacular system
     */
    public Recipe(String name, Integer id) {
        this.name = name;
        this.recipeId = id;
    }

    public Recipe() {
    }
    //Below are basic setters and getters of Recipe attributes.

    public void addIngredient(Ingredient ingredient) {
        ingredients.add(ingredient);
    }

    public void setIsVegan(Boolean value) {
        this.isVegan = value;
    }

    public Boolean isVegan() {
        return isVegan;
    }

    public void setTimeToCook(Integer value) {
        this.timeToCook = value;
    }

    public Integer getTimeToCook() {
        return timeToCook;
    }

    /**
     * @param instruction is a String with HTML attributes 
     * cleaned using the HtmlCompat.
     */
    public void setInstructions(String instruction) {
        instructions = HtmlCompat.fromHtml(instruction, HtmlCompat.FROM_HTML_MODE_LEGACY).toString();
    }

    public ArrayList<Ingredient> getIngrediantList() {
        return ingredients;
    }

    public String getInstructions() {
        //returns empty string if null else the instruction string.
        return instructions == null ? "" : instructions;
    }


    public void addImageLink(String image) {
        this.image = image;
    }


    public void addRecipeID(int id) {
        this.recipeId = id;
    }


    public String getImageLink() {
        return this.image;
    }

    public int getRecipeId() {
        return this.recipeId;
    }

    public String getName() {
        return this.name;
    }

    public int getCountOfMissingIngredients() {
        return missedIngredients.size();
    }

    public String toString() {
        String ingredient_delimited = "";
        int i = 0;
        for (Ingredient ingredient : ingredients) {
            if (i - 1 == ingredients.size())
                ingredient_delimited += ingredient.getName();
            else
                ingredient_delimited += ingredient.getName() + ",";
            i++;
        }

        return ingredient_delimited + "@@" + getInstructions() + "\n";
    }

    public int getServings() {
        return servings;
    }

    public void setServings(int servings) {
        this.servings = servings;
    }

    public ArrayList<Ingredient> getMissedIngredients() {
        return missedIngredients;
    }

    public void setMissedIngredients(ArrayList<Ingredient> missedIngredients) {
        this.missedIngredients = missedIngredients;
    }
}
