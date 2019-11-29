package com.example.luvyourleftovers.basic_classes;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

public class Ingredient implements Serializable {

    public enum Types {
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
        POWDER,
        OTHER,
    }

    private String name;
    private Types type;
    private String imageUrl;
    private boolean isInStock;

    public Ingredient(String name, String type, String imageUrl) {
        this.name = name;
        setType(type);
        this.imageUrl = imageUrl;
        isInStock = true;
    }


    public String getName() {
        return this.name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }


    public void setName(String name) {
        this.name = name;
    }


    public void setType(String type) {
        if (contains(type, new String[]{"Alcohol"})) {
            this.type = Types.ALCOHOL_BEVERAGES;
        } else if (contains(type, new String[]{"Beverages"})) {
            this.type = Types.BEVERAGES;
        } else if (contains(type, new String[]{"Cheese", "Milk", "Eggs"})) {
            this.type = Types.DAIRY;
        } else if (contains(type, new String[]{"Nut Butters", "Jams", "Honey"})) {
            this.type = Types.PROCESSED;
        } else if (contains(type, new String[]{"Refrigerated", "Frozen"}) || name.toLowerCase()
            .contains("refrigerated")) {
            this.type = Types.REFRIGERATED;
        } else if (contains(type, new String[]{"Ethnic", "Pasta", "Rice", "Health Foods"})) {
            this.type = Types.HEALTHY;
        } else if (contains(type, new String[]{"Oil", "Vinegar", "Salad Dressing"}) || name
            .toLowerCase().contains("oil")) {
            this.type = Types.OIL;
        } else if (contains(type, new String[]{"Meat"})) {
            this.type = Types.MEAT;
        } else if (contains(type, new String[]{"Cereal"})) {
            this.type = Types.CEREAL;
        } else if (contains(type, new String[]{"Produce"})) {
            this.type = Types.PRODUCE;
        } else if (contains(type, new String[]{"Spices"})) {
            this.type = Types.SPICE;
        } else if (name.toLowerCase().equals("salt")) {
            this.type = Types.SALT_SUGAR;
        } else if (name.toLowerCase().contains("powder")) {
            this.type = Types.POWDER;
        } else{
            this.type = Types.OTHER;
        }
    }

    public String getType() {
        return this.type.name().toLowerCase();
    }

    public boolean contains(String input, String[] items) {
        List<String> itemsList = Arrays.asList(items);
        return itemsList.contains(input);
    }


    public void haveIngredient(boolean isInStock) {
        this.isInStock = isInStock;
    }
}
