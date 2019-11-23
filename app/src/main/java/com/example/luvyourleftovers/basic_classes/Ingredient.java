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
}
