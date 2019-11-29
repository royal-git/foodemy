package com.example.luvyourleftovers.shopping_cart;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import androidx.annotation.Nullable;
import java.util.ArrayList;

public class CartDBHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "ShoppingCartDB.db";

    public CartDBHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, 1);
    }


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(
            "create table cart (id integer primary key, name text, quantity integer, imageUrl text)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS cart");
        onCreate(sqLiteDatabase);
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

    public void insertItem(CartItem item) {
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
            db.update("cart", values, "name='" + item.getName()+"'", null);
        }
    }


    public void removeItem(CartItem item) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("cart", "name = '" + item.getName() + "'", null);
    }

    public ArrayList<CartItem> getAllItems() {
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

}
