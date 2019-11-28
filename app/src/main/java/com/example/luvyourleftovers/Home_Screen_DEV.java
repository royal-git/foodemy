package com.example.luvyourleftovers;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;

import com.example.luvyourleftovers.basic_classes.RecipeObject;
import com.example.luvyourleftovers.shopping_cart.ShoppingCart;

public class Home_Screen_DEV extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home__screen__dev);


    }


    public void onMakeRecipe(View view) {
        Intent intent = new Intent(this, IngredientsRecipesActivity.class);
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
        intent.putExtra("recipe", new RecipeObject("Toast", 3, "https://spoonacular.com/recipeImages/Grandmas-Apple-Crisp-645152.jpg"));
        startActivity(intent);
    }

    public void onOpenShoppingCart(View view){

        Intent intent = new Intent(this, ShoppingCart.class);
        startActivity(intent);

    }
}
