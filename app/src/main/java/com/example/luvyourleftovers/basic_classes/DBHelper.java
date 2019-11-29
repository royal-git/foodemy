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

public class DBHelper extends SQLiteOpenHelper {

  public static final String DATABASE_NAME = "Foodemy.db";

  public DBHelper(@Nullable Context context) {
    super(context, DATABASE_NAME, null, 1);
  }


  @Override
  public void onCreate(SQLiteDatabase sqLiteDatabase) {
    sqLiteDatabase.execSQL(
        "create table cart (id integer primary key, name text, quantity integer, imageUrl text)");
    sqLiteDatabase.execSQL(
        "create table favourites (id integer primary key, name text, ingredients text, instructions text, recipe_id int, imageUrl text)");
  }

  @Override
  public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
    sqLiteDatabase.execSQL("DROP TABLE IF EXISTS favourites");
    sqLiteDatabase.execSQL("DROP TABLE IF EXISTS cart");
    onCreate(sqLiteDatabase);
  }

  public void insert(RecipeObject recipe) {
    SQLiteDatabase db = this.getWritableDatabase();
    ContentValues values = new ContentValues();
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

  public void insert(CartItem item) {
    SQLiteDatabase db = this.getWritableDatabase();
    ContentValues values = new ContentValues();

    if (!exists(item)) {
      values.put("name", item.getName());
      values.put("quantity", item.getQuantity());
      values.put("imageUrl", item.getImageUrl());
      db.insert("cart", null, values);
    } else {
      System.out.println("FOUND ITEM -> " + item.getName());
      CartItem singleItem = fetchSingleItem(item.getName());
      values.put("quantity", singleItem.getQuantity() + 1);
      db.update("cart", values, "name='" + item.getName() + "'", null);
    }
  }

  public void deleteAllRecipes() {
    SQLiteDatabase db = this.getWritableDatabase();
    db.execSQL("delete from favourites");
  }

  public void delete(RecipeObject recipe) {
    SQLiteDatabase db = this.getWritableDatabase();
    db.delete("favourites", "recipe_id = '" + recipe.getRecipeId() + "'", null);
  }

  public void delete(CartItem item) {
    SQLiteDatabase db = this.getWritableDatabase();
    db.delete("cart", "name = '" + item.getName() + "'", null);
  }

  public boolean exists(RecipeObject recipe) {
    SQLiteDatabase db = this.getWritableDatabase();
    Cursor cursor = null;
    String checkQuery =
        "SELECT recipe_id FROM favourites WHERE recipe_id = '" + recipe.getRecipeId() + "'";
    cursor = db.rawQuery(checkQuery, null);
    boolean exists = (cursor.getCount() > 0);
    cursor.close();
    return exists;
  }

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


  public ArrayList<RecipeObject> getAllRecipes() {
    ArrayList<RecipeObject> recipes = new ArrayList<RecipeObject>();
    SQLiteDatabase db = this.getReadableDatabase();
    Cursor res = db.rawQuery("select * from favourites", null);

    res.moveToFirst();
    while (res.isAfterLast() == false) {
      recipes.add(new RecipeObject(res.getString(res.getColumnIndex("name")), res.getInt(res.getColumnIndex("recipe_id"))));
      res.moveToNext();
    }
    return recipes;
  }

}
