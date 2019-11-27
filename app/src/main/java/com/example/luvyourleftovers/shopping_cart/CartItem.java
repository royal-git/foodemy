package com.example.luvyourleftovers.shopping_cart;

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

    public CartItem(String name) {
        this.name = name;
        this.quantity = 1;
    }

    private String name;
    private Integer quantity;

}
