package com.agenciaflex.ruufpi.util;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class RuUfpiSQLiteHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "ru_ufpi.db";
    private static final int DATABASE_VERSION = 1;

    private static final String CREATE_USER_CONFIG   = "CREATE TABLE user_config(\n" +
            "\tunidade_id int(10),\n" +
            "\tunidade_restaurante_id int(10),\n" +
            ");";

    private static final String CREATE_CARDAPIOS_TABLE = "CREATE TABLE cardapios(\n" +
            "\tid int PRIMARY KEY,\n" +
            "\tunidade_restaurante_id int(10) NOT NULL,\n" +
            "\ttype_id int not null,\n" +
            "\tday DATE NOT NULL,\n" +
            "\thorario VARCHAR(50) NOT NULL\n" +
            ");";

    private static final String CREATE_ITEM_CATEGORIES = "CREATE TABLE item_categories (\n" +
            "\tid int PRIMARY KEY,\n" +
            "\tname varchar(100) NOT NULL\n" +
            ");";

    private static final String CREATE_ITEM = "CREATE TABLE items (\n" +
            "\tid int PRIMARY KEY,\n" +
            "\titem_category_id int NOT NULL,\n" +
            "\tname varchar(100) NOT NULL\n" +
           // "\tFOREIGN KEY(item_category_id) references item_categories(id)\n" +
            //Não esquecer de descomentar quando implementar as categorias
            ");";

    private static final String CREATE_CARDAPIO_ITEMS = "CREATE TABLE cardapio_items(\n" +
            "\tid int PRIMARY KEY,\n" +
            "\tcardapio_id int NOT NULL,\n" +
            "\titem_id int NOT NULL,\n" +
            "\tFOREIGN KEY(cardapio_id) references cardapios(id),\n" +
            "\tFOREIGN KEY(item_id) references items(id)\n" +
            ");";

    public RuUfpiSQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL(CREATE_CARDAPIOS_TABLE);
        database.execSQL(CREATE_ITEM_CATEGORIES);
        database.execSQL(CREATE_ITEM);
        database.execSQL(CREATE_CARDAPIO_ITEMS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, int oldVersion,
                          int newVersion) {
        Log.w(RuUfpiSQLiteHelper.class.getName(),
                "Upgrading database from version " + oldVersion + " to "
                        + newVersion + ".");

        for (int i = oldVersion; i < newVersion; i++) {
            switch (i) {
                case 1:

                    break;
            }
        }
    }
}
