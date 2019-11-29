package com.example.luvyourleftovers.basic_classes;

import java.io.Serializable;

public class Ingredient implements Serializable {


    private String name;
    private String imageUrl;
    private boolean isInStock;

    public Ingredient(String name, String type, String imageUrl) {
        this.name = name;
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




    public void haveIngredient(boolean isInStock) {
        this.isInStock = isInStock;
    }
}
