package com.example.luvyourleftovers;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener
{
    private DrawerLayout drawer;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // Setting the tool bar per activity rather than using one for all of them for more flexibility.
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.drawer_layout);

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // Automatically adds the button in the toolbar to open the navigation drawer.
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);

        drawer.addDrawerListener(toggle);
        toggle.syncState();

        // Setting the default tab/fragment on first open, avoiding the opening of the fragment each time there's an interruption.
        if(savedInstanceState == null)
        {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new InputFragment()).commit();
            navigationView.setCheckedItem(R.id.nav_home);
        }

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem)
    {
        Intent i;

        switch (menuItem.getItemId())
        {
//            1
            case R.id.nav_home:
                Intent home = new Intent(this, Home_Screen_DEV.class);
                startActivity(home);
                break;

//                2
            case R.id.nav_recipes:
                Intent ingredientRecipes = new Intent(this, IngredientsRecipesActivity.class);
                startActivity(ingredientRecipes);
                break;

//                3
            case R.id.nav_nearby_shops:
                Intent shops = new Intent(this, FindShops.class);
                startActivity(shops);
                break;

//                4
            case R.id.nav_shoppingList:
                Intent shoppingList = new Intent(this, ShoppingListFragment.class);
                startActivity(shoppingList);
                break;

//                5
            case R.id.nav_favourites:
                Intent favourites = new Intent(this, FavouritesFragment.class);
                startActivity(favourites);
                break;
        }

        drawer.closeDrawer(GravityCompat.START);

        return true;
    }

    @Override
    public void onBackPressed()
    {
        if (drawer.isDrawerOpen(GravityCompat.START))
        {
            drawer.closeDrawer(GravityCompat.START);
        }
        else
        {
            super.onBackPressed();
        }
    }
}