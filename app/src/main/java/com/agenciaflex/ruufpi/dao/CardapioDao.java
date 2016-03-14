package com.agenciaflex.ruufpi.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.agenciaflex.ruufpi.model.Cardapio;
import com.agenciaflex.ruufpi.model.Item;
import com.agenciaflex.ruufpi.util.RuUfpiSQLiteHelper;

import java.util.ArrayList;

/**
 * Created by alanssantos on 3/18/15.
 */
public class CardapioDao {

    public String TABLE = "cardapios";
    public final String TABLE_ID = "id";
    public final String TABLE_UNIDADE_ID = "unidade_restaurante_id";
    public final String TABLE_TYPE_ID = "type_id";
    public final String TABLE_DAY = "day";
    public final String TABLE_HORARIO = "horario";

    public String[] ALL_COLUMNS = {TABLE_ID, TABLE_UNIDADE_ID, TABLE_TYPE_ID, TABLE_DAY, TABLE_HORARIO};
    protected SQLiteDatabase database;
    protected RuUfpiSQLiteHelper dbHelper;

    private Context context;

    public CardapioDao(Context context) {
        dbHelper = new RuUfpiSQLiteHelper(context);
        this.context = context;
    }

    public long create(Cardapio cardapio) {
        database.insert(TABLE, null, buildArguments(cardapio));

        ItemDao itemDao = new ItemDao(context);
        for (Item item : cardapio.getItems()) {
            database.insert("cardapio_items", null, buildArguments(cardapio, item));
            if (itemDao.selectById(item.getId()) == null)
                itemDao.create(item);
        }

        return cardapio.getId();
    }

    public int update(Cardapio cardapio) {
        ContentValues values = buildArguments(cardapio);
        return database.update(TABLE, values,
                TABLE_ID + " = '" + 1 + "'", null);
    }

    public void delete(long id) {
        database.delete(TABLE, TABLE_ID + " = '" + id + "'", null);
    }

    public void deleteAll() {
        database.delete(TABLE, null, null);
    }

    public Cardapio selectById(long id) {
        Cursor cursor = database.query(TABLE, ALL_COLUMNS, "id = " + id, null,
                null, null, null);

        Cardapio cardapio = null;
        if (cursor.moveToFirst()) {
            cardapio = cursorToList(cursor);
        }
        cursor.close();
        return cardapio;
    }

    public Cardapio selectByDayRestaurant(String day, int restaurante_id, int type_id) {
        Cursor cursor = database.query(TABLE, ALL_COLUMNS,  "day = '"+day+"' and unidade_restaurante_id = " + restaurante_id + " and type_id = "+type_id, null,
                null, null, null);

        Cardapio cardapio = null;
        if (cursor.moveToFirst()) {
            cardapio = cursorToList(cursor);
        }
        cursor.close();
        return cardapio;
    }

    public ArrayList<Cardapio> selectAll() {
        ArrayList<Cardapio> cardapios = new ArrayList<Cardapio>();

        Cursor cursor = database.query(TABLE, ALL_COLUMNS, null, null, null,
                null, null);
        cursor.moveToFirst();

        while (!cursor.isAfterLast()) {
            Cardapio cardapio = cursorToList(cursor);
            cardapios.add(cardapio);
            cursor.moveToNext();
        }
        cursor.close();

        return cardapios;
    }

    protected ContentValues buildArguments(Cardapio cardapio) {
        ContentValues values = new ContentValues();
        values.put(TABLE_ID, cardapio.getId());
        values.put(TABLE_UNIDADE_ID, cardapio.getUnidade_id());
        values.put(TABLE_TYPE_ID, cardapio.getType_id());
        values.put(TABLE_DAY, cardapio.getDay());
        values.put(TABLE_HORARIO, cardapio.getHorario());

        return values;
    }

    protected ContentValues buildArguments(Cardapio model, Item item) {
        ContentValues values = new ContentValues();
        Cardapio cardapio = new Cardapio();
        values.put("cardapio_id", cardapio.getId());
        values.put("item_id", item.getId());

        String coluns[] = {"item_id"};
        Cursor cursor = database.query("cardapio_items", coluns, "cardapio_id = " + cardapio.getId(), null, null,
                null, null);

        return values;
    }

    protected Cardapio cursorToList(Cursor cursor) {
        Cardapio cardapio = new Cardapio();

        cardapio.setId(cursor.getInt(0));
        cardapio.setUnidade_id(cursor.getInt(1));
        cardapio.setType_id(cursor.getInt(2));
        cardapio.setDay(cursor.getString(3));
        cardapio.setHorario(cursor.getString(4));


        return cardapio;
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
        database.execSQL("PRAGMA foreign_keys=ON;");
    }

    public void close() {
        dbHelper.close();
    }
}
