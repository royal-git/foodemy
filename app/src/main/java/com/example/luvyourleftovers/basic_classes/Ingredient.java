package com.example.luvyourleftovers.basic_classes;

public interface Ingredient {
    public enum Types {
        VEG,
        DAIRY,
        MEAT,
        FRUIT,
        PROCESSED,
        OTHER
    }
    public String getName();
    public void setName(String name);

    public void setType(Types type);

    public void haveIngredient(boolean isInStock); //tells us whether an ingredient is in the current stock of the user.
}
