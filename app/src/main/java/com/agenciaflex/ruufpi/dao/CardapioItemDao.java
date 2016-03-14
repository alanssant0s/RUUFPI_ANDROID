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
 * Created by alanssantos on 3/18/15.
 */
public class CardapioItemDao {

    public String TABLE = "cardapio_items";
    public final String TABLE_ID = "id";
    public final String TABLE_CARDAPIO_ID = "cardapio_id";
    public final String TABLE_ITEM_ID = "item_id";

    public String[] ALL_COLUMNS = {TABLE_ID, TABLE_CARDAPIO_ID, TABLE_ITEM_ID};
    protected SQLiteDatabase database;
    protected RuUfpiSQLiteHelper dbHelper;

    private ItemDao itemDao;

    public CardapioItemDao(Context context) {
        dbHelper = new RuUfpiSQLiteHelper(context);
        itemDao = new ItemDao(context);
    }

    public long create(long cardapio_id, long item_id) {
        return database.insert(TABLE, null, buildArguments(cardapio_id, item_id));
    }

    public void delete(long id) {
        database.delete(TABLE, TABLE_ID + " = '" + id + "'", null);
    }

    public void deleteAll() {
        database.delete(TABLE, null, null);
    }

    public ArrayList<Item> selectAllItemsByCardapio(long id) {
        ArrayList<Item> items = new ArrayList<Item>();

        Cursor cursor = database.query(TABLE, ALL_COLUMNS, null, null, null,
                null, null);
        cursor.moveToFirst();

        while (!cursor.isAfterLast()) {

            items.add(itemDao.selectById(cursor.getInt(2)));
            cursor.moveToNext();
        }
        cursor.close();

        return items;
    }

    protected ContentValues buildArguments(long cardapio_id, long item_id) {
        ContentValues values = new ContentValues();

        values.put(TABLE_CARDAPIO_ID, cardapio_id);
        values.put(TABLE_ITEM_ID, item_id);

        return values;
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
        database.execSQL("PRAGMA foreign_keys=ON;");
    }

    public void close() {
        dbHelper.close();
    }
}
