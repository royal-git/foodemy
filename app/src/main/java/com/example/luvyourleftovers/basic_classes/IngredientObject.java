package com.example.luvyourleftovers.basic_classes;

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
    public void setType(Types type) {
        this.type = Types.values()[5];
    }

    @Override
    public void haveIngredient(boolean isInStock) {
        this.isInStock = isInStock;
    }
}
