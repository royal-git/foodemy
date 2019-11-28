package com.example.luvyourleftovers.basic_classes;

import androidx.core.text.HtmlCompat;

import java.util.ArrayList;

public class RecipeObject implements Recipe {
    private ArrayList<Ingredient> ingredients = new ArrayList<>();
    private String servings;
    private String timeToCook;
    private boolean isVegan;

    private String instructions;
    private String name;
    private String image;
    private Boolean isVegan;
    private int timeToCook;
    private int recipeId;
    private int missingIngredients = 0;

    public RecipeObject(String name, Integer id, String imageUrl) {
        this.image = imageUrl;
        this.name = name;
        this.recipeId = id;
    }

    public RecipeObject() {
    

    public RecipeObject(){

    }

    public void setIsVegan(boolean v){this.isVegan = v;}
    public boolean isVegan(){return this.isVegan;}

    public void setServings(String servings){
        this.servings = servings;
    }
    public String getServings(){return servings;}

    public void setTimeToCook(String timeToCook){
        this.timeToCook = timeToCook;
    }

    public String getTimeToCook(){return this.timeToCook;}
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
    public String getRecipeName() {
        return this.recipeName;
    }

    @Override
    public ArrayList<Ingredient> getIngrediantList() {
        return ingredients;
    }

    @Override
    public String getInstructions() {
        return instructions;
    }

    @Override
    public void addImageLink(String image) {
        this.image = image;
    }

    @Override
    public void addRecipeID(int id) {
        this.recipeId = id;
    }

    @Override
    public void addMissingIngredients(int missingCount) {
        missingIngredients = missingCount;
    }

    @Override
    public void incrMissingIngredients() {
        missingIngredients++;
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
        return this.missingIngredients;
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
}
