package com.example.luvyourleftovers.basic_classes;

import androidx.core.text.HtmlCompat;
import java.io.Serializable;
import java.util.ArrayList;

public class RecipeObject implements Recipe, Serializable {
    private ArrayList<Ingredient> ingredients = new ArrayList<>();
    private ArrayList<IngredientObject> missedIngredients = new ArrayList<>();
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


    public RecipeObject(String name, Integer id, String imageUrl) {
        this.image = imageUrl;
        this.name = name;
        this.recipeId = id;
    }

    public RecipeObject(String name, Integer id) {
        this.name = name;
        this.recipeId = id;
    }

    public RecipeObject() {
    }

    @Override
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

    public void setInstructions(String instruction) {
        instructions = HtmlCompat.fromHtml(instruction, HtmlCompat.FROM_HTML_MODE_LEGACY).toString();
    }

    @Override
    public ArrayList<Ingredient> getIngrediantList() {
        return ingredients;
    }

    @Override
    public String getInstructions() {
        return instructions == null ? "" : instructions;
    }

    @Override
    public void addImageLink(String image) {
        this.image = image;
    }

    @Override
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

    public ArrayList<IngredientObject> getMissedIngredients() {
        return missedIngredients;
    }

    public void setMissedIngredients(ArrayList<IngredientObject> missedIngredients) {
        this.missedIngredients = missedIngredients;
    }
}
