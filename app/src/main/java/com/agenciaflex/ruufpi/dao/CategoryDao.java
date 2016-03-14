package com.agenciaflex.ruufpi.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.agenciaflex.ruufpi.model.Category;
import com.agenciaflex.ruufpi.util.RuUfpiSQLiteHelper;

import java.util.ArrayList;

/**
 * Created by alanssantos on 4/1/15.
 */
public class CategoryDao {

    public String TABLE = "Categories";
    public final String TABLE_ID = "id";
    public final String TABLE_NAME = "name";

    public String[] ALL_COLUMNS = {TABLE_ID, TABLE_NAME};
    protected SQLiteDatabase database;
    protected RuUfpiSQLiteHelper dbHelper;

    public CategoryDao(Context context) {
        dbHelper = new RuUfpiSQLiteHelper(context);
    }

    public long create(Category category) {
        return database.insert(TABLE, null, buildArguments(category));
    }

    public int update(Category category) {
        ContentValues values = buildArguments(category);
        return database.update(TABLE, values,
                TABLE_ID + " = '" + 1 + "'", null);
    }

    public void delete(long id) {
        database.delete(TABLE, TABLE_ID + " = '" + id + "'", null);
    }

    public void deleteAll(int id) {
        database.delete(TABLE, null, null);
    }

    public Category selectById(long id) {
        Cursor cursor = database.query(TABLE, ALL_COLUMNS, "id = " + id, null,
                null, null, null);

        Category category = null;
        if (cursor.moveToFirst()) {
            category = cursorToList(cursor);
        }
        cursor.close();
        return category;
    }

    public ArrayList<Category> selectAll() {
        ArrayList<Category> categorys = new ArrayList<Category>();

        Cursor cursor = database.query(TABLE, ALL_COLUMNS, null, null, null,
                null, null);
        cursor.moveToFirst();

        while (!cursor.isAfterLast()) {
            Category category = cursorToList(cursor);
            categorys.add(category);
            cursor.moveToNext();
        }
        cursor.close();

        return categorys;
    }

    protected ContentValues buildArguments(Category model) {
        ContentValues values = new ContentValues();
        Category category = new Category();
        values.put(TABLE_ID, category.getId());
        values.put(TABLE_NAME, category.getName());

        return values;
    }

    protected Category cursorToList(Cursor cursor) {
        Category category = new Category();

        category.setId(cursor.getInt(0));
        category.setName(cursor.getString(1));


        return category;
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
        database.execSQL("PRAGMA foreign_keys=ON;");
    }

    public void close() {
        dbHelper.close();
    }
}
