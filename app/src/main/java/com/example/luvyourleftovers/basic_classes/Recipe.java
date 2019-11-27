package com.example.luvyourleftovers.basic_classes;

import java.util.ArrayList;

public interface Recipe
{
    //private ArrayList<Ingredient> ingredients = new ArrayList<>();
    //private ArrayList<String> instructions = new ArrayList<>();

    public ArrayList<Ingredient> getIngrediantList();

    public ArrayList<String> getInstructions();
    public void addIngredient(Ingredient ingredient);

    public void addInstruction(String instruction);

    public void addImageLink(String image);
    
    public void addRecipeID(int id);

    public void addMissingIngredients(int missingCount);

    public void incrMissingIngredients(); //increments by 1
}
