package com.example.luvyourleftovers.shopping_cart;

/**
 * Class to hold each item inside the shopping cart, the things here are pretty much self
 * explanatory.
 */
public class CartItem {

    public String getName() {
        return name;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public CartItem(String name, Integer quantity) {
        this.name = name;
        this.quantity = quantity;
    }


    public String getImageUrl() {
        return imageUrl;
    }

    public CartItem(String name, Integer quantity, String imageUrl) {
        this.name = name;
        this.quantity = quantity;
        this.imageUrl = imageUrl;
    }

    public CartItem(String name) {
        this.name = name;
        this.quantity = 1;
    }

    private String name;
    private Integer quantity;
    private Integer id;
    private String imageUrl;

    //return an id of 1 if id is null, else return the objects id.
    public Integer getId() {
        return this.id == null ? 1 : this.id;
    }
}
