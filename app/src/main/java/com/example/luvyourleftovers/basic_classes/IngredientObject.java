package com.example.luvyourleftovers.basic_classes;

import java.util.Arrays;
import java.util.List;

public class IngredientObject implements Ingredient {

    private String name;
    private Types type;

    private boolean isInStock;


    public IngredientObject(String name, int type){
        this.name = name;
        this.type = Types.values()[5];
        isInStock = true;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public void setType(String type) {
        if (contains(type, new String[]{"Beverages", "Alcoholic"})) {
            this.type = Types.BEVERAGES;
        } else if (contains(type, new String[]{"Cheese", "Milk", "Eggs"})) {
            this.type = Types.DAIRY;
        } else if (contains(type, new String[]{"Nut Butters", "Jams", "Honey"})) {
            this.type = Types.PROCESSED;
        } else if (contains(type, new String[]{"Refrigerated", "Frozen"})) {
            this.type = Types.REFRIGERATED;
        } else if (contains(type, new String[]{"Ethnic", "Pasta", "Rice", "Health Foods"})) {
            this.type = Types.HEALTHY;
        } else if (contains(type, new String[]{"Oil", "Vinegar", "Salad Dressing"})) {
            this.type = Types.OIL;
        } else if (contains(type, new String[]{"Meat"})) {
            this.type = Types.MEAT;
        } else if (contains(type, new String[]{"Cereal"})) {
            this.type = Types.CEREAL;
        } else if (contains(type, new String[]{"Produce"})) {
            this.type = Types.PRODUCE;
        } else if (contains(type, new String[]{"Spices"})) {
            this.type = Types.SPICE;
        } else{
            this.type = Types.OTHER;
        }

    }

    public boolean contains(String input, String[] items) {
        List<String> itemsList = Arrays.asList(items);
        return itemsList.contains(input);
    }

    @Override
    public void haveIngredient(boolean isInStock) {
        this.isInStock = isInStock;
    }
}
