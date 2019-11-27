package com.example.luvyourleftovers.basic_classes;

import java.util.ArrayList;

public class Recipe
{
    private ArrayList<Ingredient> ingredients = new ArrayList<>();
    private ArrayList<String> instructions = new ArrayList<>();

    public ArrayList<Ingredient> getIngrediantList()
    {
        return ingredients;
    }

    public ArrayList<String> getInstructions()
    {
        return instructions;
    }

    public void addIngredient(Ingredient ingredient)
    {
        ingredients.add(ingredient);
    }

    public void addInstruction(String instruction)
    {
        instructions.add(instruction);
    }
}
