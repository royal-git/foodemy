package com.example.luvyourleftovers.shopping_cart;

public class CartItem {
    public String getName() {
        return name;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public CartItem(Integer id, String name, Integer quantity) {
        this.name = name;
        this.id = id;
        this.quantity = quantity;
    }

    public CartItem(String name) {
        this.name = name;
        this.quantity = 1;
        this.id = 0;
    }

    private String name;
    private Integer quantity;
    private Integer id;

    public Integer getId() {
        return this.id == null ? 1 : this.id;
    }
}
