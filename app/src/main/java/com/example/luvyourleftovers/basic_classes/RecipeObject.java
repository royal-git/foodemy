package com.example.luvyourleftovers.basic_classes;

import java.util.ArrayList;

public class RecipeObject implements Recipe {
    private ArrayList<Ingredient> ingredients = new ArrayList<>();
    private ArrayList<String> instructions = new ArrayList<>();

    private String recipeName;
    private String servings;
    private String timeToCook;
    private boolean isVegan;
    private String image;
    private int recipeId;
    private int missingIngredients=0;

    public RecipeObject(String recipeName, int recipeId){
        this.recipeName = recipeName;
        this.recipeId = recipeId;
    }

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

    @Override
    public void addInstruction(String instruction) {
        instructions.add(instruction);
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
    public ArrayList<String> getInstructions() {
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


    public String getImageLink(){return this.image;}
    public int getRecipeId(){return this.recipeId;}
    public int getCountOfMissingIngredients(){return this.missingIngredients;}

    public String toString(){
        String ingredient_delimited = "";
        int i =0;
        for(Ingredient ingredient: ingredients){
            if(i-1 == ingredients.size())
                ingredient_delimited+=ingredient.getName();
            else
                ingredient_delimited+=ingredient.getName()+",";
            i++;
        }

        String instruction_delimited = "";
        i =0;
        for(String instruction: instructions){
            if(i-1 == ingredients.size())
                instruction_delimited+=instruction;
            else
                instruction_delimited+=instruction+",";
            i++;
        }
        return ingredient_delimited+"@@"+instruction_delimited+"\n";
    }
}
