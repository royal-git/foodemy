package com.example.luvyourleftovers.basic_classes;

import java.util.ArrayList;

public interface Recipe {

    public void addIngredient(Ingredient ingredient);
    public void addInstruction(String instruction);

    public ArrayList<Ingredient> getIngrediantList();

    public ArrayList<String> getInstructions();
}
