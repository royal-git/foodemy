package com.example.luvyourleftovers;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.luvyourleftovers.basic_classes.APICaller;
import com.example.luvyourleftovers.basic_classes.FavouritesDB;
import com.example.luvyourleftovers.basic_classes.Recipe;
import com.example.luvyourleftovers.basic_classes.RecipeObject;
import com.example.luvyourleftovers.shopping_cart.ShoppingCart;

import java.util.ArrayList;

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
    public void onMapsActivity(View view){
        Intent intent = new Intent(this, FindShops.class);
        startActivity(intent);
    }
    public void onDisplayRecipe(View view) {
        Intent intent = new Intent(this, ViewRecipe.class);
        intent.putExtra("id", 324694);
        intent.putExtra("recipe", new RecipeObject("Toast", 479101, "https://spoonacular.com/recipeImages/Grandmas-Apple-Crisp-645152.jpg"));
        startActivity(intent);
    }

    public void onOpenShoppingCart(View view){

        Intent intent = new Intent(this, ShoppingCart.class);
        startActivity(intent);

    }

    public void openFavouritesActivity(View view){
        FavouritesDB db = new FavouritesDB(this);

        ArrayList<RecipeObject> data = db.getAllRecipes();
        ArrayList<RecipeObject> recipeList = new ArrayList<>();

        Intent intent = new Intent(this, RecipeList.class);
        intent.putExtra("recipeHeaders", data);
        intent.putExtra("RecipeTypes","Favourites");
        startActivity(intent);

    }
}
