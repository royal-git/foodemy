package com.example.luvyourleftovers.basic_classes;

import androidx.core.text.HtmlCompat;

import java.util.ArrayList;

public class RecipeObject implements Recipe {
    private ArrayList<Ingredient> ingredients = new ArrayList<>();
    private String instructions;

    private String image;
    private int recipeId;
    private int missingIngredients = 0;

    @Override
    public void addIngredient(Ingredient ingredient) {
        ingredients.add(ingredient);
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
