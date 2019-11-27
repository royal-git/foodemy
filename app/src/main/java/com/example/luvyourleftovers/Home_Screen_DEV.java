package com.example.luvyourleftovers;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.luvyourleftovers.shopping_cart.ShoppingCart;

public class Home_Screen_DEV extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home__screen__dev);
    }


    public void onMakeRecipe(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void onSavedRecipes(View view) {
        Intent intent = new Intent(this, SavedRecipesDev.class);
        startActivity(intent);
    }
    public void onMapsActivity(View view){
        Intent intent = new Intent(this, FindShops.class);
        startActivity(intent);
    }
    public void onDisplayRecipe(View view) {
        Intent intent = new Intent(this, ViewRecipe.class);
        intent.putExtra("id", 324694);
        intent.putExtra("title", "Silver Dollar Buttermilk-Pecan Pancakes with Bourbon Molasses Butter and Maple Syrup");
        startActivity(intent);
    }

    public void onOpenShoppingCart(View view){

        Intent intent = new Intent(this, ShoppingCart.class);
        startActivity(intent);

    }
}
