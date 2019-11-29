package com.example.luvyourleftovers.basic_classes;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.text.TextUtils;
import androidx.annotation.Nullable;
import com.example.luvyourleftovers.shopping_cart.CartItem;
import java.util.ArrayList;

/**
 * Class that allows other classes to access, query and remove items from the database.
 */
public class DBHelper extends SQLiteOpenHelper {

  // The database name.
  public static final String DATABASE_NAME = "Foodemy.db";

  public DBHelper(@Nullable Context context) {
    super(context, DATABASE_NAME, null, 1);
  }


  // Create tables if they do not exist.
  @Override
  public void onCreate(SQLiteDatabase sqLiteDatabase) {
    sqLiteDatabase.execSQL(
        "create table cart (id integer primary key, name text, quantity integer, imageUrl text)");
    sqLiteDatabase.execSQL(
        "create table favourites (id integer primary key, name text, ingredients text, instructions text, recipe_id int, imageUrl text)");
  }

  // Drop it and create a new one.
  @Override
  public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
    sqLiteDatabase.execSQL("DROP TABLE IF EXISTS favourites");
    sqLiteDatabase.execSQL("DROP TABLE IF EXISTS cart");
    onCreate(sqLiteDatabase);
  }

  // Insert a new recipe into the favourite table.
  public void insert(Recipe recipe) {
    SQLiteDatabase db = this.getWritableDatabase();
    ContentValues values = new ContentValues();
    // Ensure it doesn't already exist there.
    if (!exists(recipe)) {
      values.put("name", recipe.getName());
      String ingredientsList = TextUtils.join(", ", recipe.getIngrediantList());
      values.put("ingredients", ingredientsList);
      String instructionList = recipe.getInstructions();
      values.put("instructions", instructionList);
      values.put("recipe_id", recipe.getRecipeId());
      values.put("imageUrl", recipe.getImageLink());
      db.insert("favourites", null, values);
    }
  }

  // Insert a grocery into the shopping cart.
  public void insert(CartItem item) {
    SQLiteDatabase db = this.getWritableDatabase();
    ContentValues values = new ContentValues();

    // If it doesn't already exist then quantity -> 1, otherwise quantity -> quantity + 1
    if (!exists(item)) {
      values.put("name", item.getName());
      values.put("quantity", item.getQuantity());
      values.put("imageUrl", item.getImageUrl());
      db.insert("cart", null, values);
    } else {
      CartItem singleItem = fetchSingleItem(item.getName());
      values.put("quantity", singleItem.getQuantity() + 1);
      db.update("cart", values, "name='" + item.getName() + "'", null);
    }
  }

  // Remove all recipes, basically only used during testing.
  public void deleteAllRecipes() {
    SQLiteDatabase db = this.getWritableDatabase();
    db.execSQL("delete from favourites");
  }

  // Delete a certain recipe from the table
  public void delete(Recipe recipe) {
    SQLiteDatabase db = this.getWritableDatabase();
    db.delete("favourites", "recipe_id = '" + recipe.getRecipeId() + "'", null);
  }

  public void delete(CartItem item) {
    SQLiteDatabase db = this.getWritableDatabase();
    db.delete("cart", "name = '" + item.getName() + "'", null);
  }

  // Check if a given recipe exists in the table.
  public boolean exists(Recipe recipe) {
    SQLiteDatabase db = this.getWritableDatabase();
    Cursor cursor = null;
    String checkQuery =
        "SELECT recipe_id FROM favourites WHERE recipe_id = '" + recipe.getRecipeId() + "'";
    cursor = db.rawQuery(checkQuery, null);
    boolean exists = (cursor.getCount() > 0);
    cursor.close();
    return exists;
  }

  // Check if a given item exists in the cart.
  public boolean exists(CartItem item) {
    SQLiteDatabase db = this.getWritableDatabase();
    Cursor cursor = null;
    String checkQuery =
        "SELECT name FROM cart WHERE name = '" + item.getName() + "'";
    cursor = db.rawQuery(checkQuery, null);
    boolean exists = (cursor.getCount() > 0);
    cursor.close();
    return exists;
  }

  // Fetch a single cart item based on the name, used as a helper method for other methods.
  public CartItem fetchSingleItem(String name) {
    SQLiteDatabase db = this.getReadableDatabase();
    Cursor res = db.rawQuery("select * from cart", null);

    if (res != null) {
      res.moveToFirst();
    }

    CartItem item = new CartItem(res.getString(res.getColumnIndex("name")),
        res.getInt(res.getColumnIndex("quantity")),
        res.getString(res.getColumnIndex("imageUrl")));

    return item;
  }


  // Fetch all items in the shopping cart -> used to show shopping list.
  public ArrayList<CartItem> getAllCartItems() {
    ArrayList<CartItem> items = new ArrayList<CartItem>();
    SQLiteDatabase db = this.getReadableDatabase();
    Cursor res = db.rawQuery("select * from cart", null);

    res.moveToFirst();
    while (res.isAfterLast() == false) {
      items.add(new CartItem(res.getString(res.getColumnIndex("name")),
          res.getInt(res.getColumnIndex("quantity")),
          res.getString(res.getColumnIndex("imageUrl"))));
      res.moveToNext();
    }
    return items;
  }


  // Fetch all recipes in the favourites table and return as a list of recipes. 
  public ArrayList<Recipe> getAllRecipes() {
    ArrayList<Recipe> recipes = new ArrayList<Recipe>();
    SQLiteDatabase db = this.getReadableDatabase();
    Cursor res = db.rawQuery("select * from favourites", null);

    res.moveToFirst();
    while (res.isAfterLast() == false) {
      recipes.add(new Recipe(res.getString(res.getColumnIndex("name")), res.getInt(res.getColumnIndex("recipe_id"))));
      res.moveToNext();
    }
    return recipes;
  }

}
