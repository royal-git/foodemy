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

    public void insertItem(CartItem item) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", item.getName());
        values.put("quantity", item.getQuantity());
        values.put("imageUrl", item.getImageUrl());
        db.insert("cart", null, values);
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
