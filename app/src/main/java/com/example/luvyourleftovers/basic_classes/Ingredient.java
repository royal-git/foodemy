package com.example.luvyourleftovers.basic_classes;

public interface Ingredient
{


    public enum Types
    {
        VEG,
        DAIRY,
        MEAT,
        BEVERAGES,
        ALCOHOL_BEVERAGES,
        BAKING,
        HEALTHY,
        CEREAL,
        REFRIGERATED,
        OIL,
        PRODUCE,
        SPICE,
        SWEET,
        FRUIT,
        SALT_SUGAR,
        PROCESSED,
        OTHER,
    }

    /*
    private String name;
    private Types type;
    private String icon;
    */

    public String getName();

    public void setName(String name);

    public void setType(String type);


    public void haveIngredient(boolean isInStock); //tells us whether an ingredient is in the current stock of the user.
}
