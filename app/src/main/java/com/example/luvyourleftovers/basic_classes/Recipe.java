package com.example.luvyourleftovers.basic_classes;

import java.util.ArrayList;

public interface Recipe {

    public void addIngredient(Ingredient ingredient);
    public void addInstruction(String instruction);

    public ArrayList<Ingredient> getIngrediantList();

    public ArrayList<String> getInstructions();

    public void addImageLink(String image);
    
    public void addRecipeID(int id);

    public void addMissingIngredients(int missingCount);

    public void incrMissingIngredients(); //increments by 1
}
