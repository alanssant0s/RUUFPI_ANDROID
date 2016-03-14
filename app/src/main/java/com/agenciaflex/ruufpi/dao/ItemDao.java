package com.agenciaflex.ruufpi.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.agenciaflex.ruufpi.model.Item;
import com.agenciaflex.ruufpi.util.RuUfpiSQLiteHelper;

import java.util.ArrayList;

/**
 * Created by alanssantos on 4/1/15.
 */
public class ItemDao {

    public String TABLE = "Items";
    public final String TABLE_ID = "id";
    public final String TABLE_ITEM_CATEGORY_ID = "item_category_id";
    public final String TABLE_NAME = "name";

    public String[] ALL_COLUMNS = {TABLE_ID, TABLE_ITEM_CATEGORY_ID, TABLE_NAME};

    public final String TABLE_CARDAPIO_ID = "cardapio_id";
    public final String TABLE_ITEM_ID = "item_id";
    public String[] ALL_COLUMNS_CI = {TABLE_ID, TABLE_CARDAPIO_ID, TABLE_ITEM_ID};

    protected SQLiteDatabase database;
    protected RuUfpiSQLiteHelper dbHelper;

    public ItemDao(Context context) {
        dbHelper = new RuUfpiSQLiteHelper(context);
    }

    public long create(Item item) {
        return database.insert(TABLE, null, buildArguments(item));
    }

    public int update(Item item) {
        ContentValues values = buildArguments(item);
        return database.update(TABLE, values,
                TABLE_ID + " = '" + 1 + "'", null);
    }

    public void delete(long id) {
        database.delete(TABLE, TABLE_ID + " = '" + id + "'", null);
    }

    public void deleteAll() {
        database.delete(TABLE, null, null);
    }

    public Item selectById(long id) {
        Cursor cursor = database.query(TABLE, ALL_COLUMNS, "id = " + id, null,
                null, null, null);

        Item item = null;
        if (cursor.moveToFirst()) {
            item = cursorToList(cursor);
        }
        cursor.close();
        return item;
    }

    public ArrayList<Item> selectAll() {
        ArrayList<Item> items = new ArrayList<Item>();

        Cursor cursor = database.query(TABLE, ALL_COLUMNS, null, null, null,
                null, null);
        cursor.moveToFirst();

        while (!cursor.isAfterLast()) {
            Item item = cursorToList(cursor);
            items.add(item);
            cursor.moveToNext();
        }
        cursor.close();

        return items;
    }

    public ArrayList<Item> selectAllItemsByCardapio(long id) {
        ArrayList<Item> items = new ArrayList<Item>();

        Cursor cursor = database.query("cardapio_items", ALL_COLUMNS_CI, "cardapio_id = "+id, null, null,
                null, null);
        cursor.moveToFirst();

        while (!cursor.isAfterLast()) {

            items.add(selectById(cursor.getInt(2)));
            cursor.moveToNext();
        }
        cursor.close();

        return items;
    }

    protected ContentValues buildArguments(Item item) {
        ContentValues values = new ContentValues();
        values.put(TABLE_ID, item.getId());
        values.put(TABLE_ITEM_CATEGORY_ID, item.getItem_category_id());
        values.put(TABLE_NAME, item.getName());

        return values;
    }

    protected Item cursorToList(Cursor cursor) {
        Item item = new Item();

        item.setId(cursor.getInt(0));
        item.setItem_category_id(cursor.getInt(1));
        item.setName(cursor.getString(2));


        return item;
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
        database.execSQL("PRAGMA foreign_keys=ON;");
    }

    public void close() {
        dbHelper.close();
    }
}
