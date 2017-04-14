package com.example.xpack.bestbuy;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBHelper extends SQLiteOpenHelper {

    // If you change the database schema, you must increment the database version.
    static final int DATABASE_VERSION = 5;
    static final String DATABASE_NAME = "toutv.db";

    static final String[] TABLES = new String[]{
            "favorites"
    };
    static String salePrice="salePrice";
    static String ratings="ratings";
    static String available="available";

    static final String[] SQL_CREATE_DB = new String[]{
            "CREATE TABLE `favorites` (" +
                    " `_id` INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT," +
                    " `key` TEXT NOT NULL," +
                    " `added` INTEGER NOT NULL," +
                    " `name` TEXT NOT NULL,"+
                    " `image` TEXT NOT NULL,"+
                     salePrice +" TEXT NOT NULL,"+
                     ratings+" TEXT NOT NULL,"+
                     available+" TEXT NOT NULL,"+
                    " `category.name` TEXT NOT NULL"+
                    ")",




    };

    private static SQLiteDatabase db = null;

    public DBHelper(Context context) {
        super(context, DBHelper.DATABASE_NAME, null, DBHelper.DATABASE_VERSION);

        if (DBHelper.db == null)
            DBHelper.db = getWritableDatabase();
    }

    public SQLiteDatabase getDB() {
        return DBHelper.db;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        for (String statement : DBHelper.SQL_CREATE_DB) {
            db.execSQL(statement);
        }
        Log.i("DB", "Création de la base de données -- v" + DATABASE_VERSION);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Delete all tables
        for (String table : DBHelper.TABLES) {
            db.execSQL("DROP TABLE IF EXISTS `" + table + "`");
        }

        // Re-create the database with the new scheme
        onCreate(db);
    }
}
