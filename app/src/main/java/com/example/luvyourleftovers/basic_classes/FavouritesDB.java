package com.example.luvyourleftovers.basic_classes;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.text.TextUtils;
import androidx.annotation.Nullable;
import java.util.ArrayList;

public class FavouritesDB extends SQLiteOpenHelper {

  public static final String DATABASE_NAME = "FavouriteRecipes.db";

  public FavouritesDB(@Nullable Context context) {
    super(context, DATABASE_NAME, null, 1);
  }


  @Override
  public void onCreate(SQLiteDatabase sqLiteDatabase) {
    sqLiteDatabase.execSQL(
        "create table favourites (id integer primary key, name text, ingredients text, instructions text, recipe_id int, imageUrl text)");
  }

  @Override
  public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
    sqLiteDatabase.execSQL("DROP TABLE IF EXISTS favourites");
    onCreate(sqLiteDatabase);
  }

  public void insertRecipe(RecipeObject recipe) {
    SQLiteDatabase db = this.getWritableDatabase();
    ContentValues values = new ContentValues();
    if (!ifExists(recipe)) {
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

  public void deleteAllRecipes() {
    SQLiteDatabase db = this.getWritableDatabase();
    db.execSQL("delete from favourites");
  }

  public void deleteRecipe(RecipeObject recipe) {
    SQLiteDatabase db = this.getWritableDatabase();
    ContentValues values = new ContentValues();
    if (ifExists(recipe)) {
      db.execSQL("delete from favourites where recipe_id='" + recipe.getRecipeId() + "'");
    }
  }

  public boolean ifExists(RecipeObject recipe) {
    SQLiteDatabase db = this.getWritableDatabase();
    Cursor cursor = null;
    String checkQuery =
        "SELECT recipe_id FROM favourites WHERE recipe_id = '" + recipe.getRecipeId() + "'";
    cursor = db.rawQuery(checkQuery, null);
    boolean exists = (cursor.getCount() > 0);
    cursor.close();
    return exists;
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
