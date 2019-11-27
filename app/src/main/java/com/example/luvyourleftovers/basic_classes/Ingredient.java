package com.example.luvyourleftovers.basic_classes;

public class Ingredient
{
    public enum Type
    {
        VEG,
        DAIRY,
        MEAT,
        FRUIT,
        PROCESSED,
        OTHER
    }

    private String name;
    private Type type;
    private String icon;

    public Ingredient(String name, Type type, String icon)
    {
        this.name = name;
        this.type = type;
        this.icon = icon;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public void setType(Type type)
    {
        this.type = type;
    }
}
