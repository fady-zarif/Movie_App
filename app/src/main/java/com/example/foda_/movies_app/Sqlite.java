/*

/// this file i was using to create the sqlite

package com.example.foda_.movies_app;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

*/
/**
 * Created by foda_ on 2016-04-17.
 *//*

public class Sqlite extends SQLiteOpenHelper {
    public static final String database_name = "Favoriet.db";
    public static final String table_name="Mytable";
    public static final String Col1 = "Id";
    public static final String Col2 = "poster";
    public static final String Col3 = "original_title";
    public static final String Col4 = "rate";
    public static final String Col5 = "date";
    public static final String Col6 = "overview";

    public static final int version = 1;
    public Sqlite(Context context) {
        super(context, database_name, null, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        String Sql_query=" CREATE TABLE " + table_name + "( "
        +Col1 + " INTEGER PRIMARY KEY AUTOINCREMENT ,"
        +Col2+" TEXT ,"
        +Col3+" TEXT ,"
        +Col4+" TEXT ,"
        +Col5+" TEXT ,"
        +Col6+" TEXT "
        +")";
        db.execSQL(Sql_query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String query = "DROP TABLE IF EXISTS " + table_name;
        db.execSQL(query);
        onCreate(db);
    }


     public boolean insert_data(Movies movies)
   {
       SQLiteDatabase db = this.getWritableDatabase();
       ContentValues contentValues = new ContentValues();
       contentValues.put(Col2,movies.JsonPath );
       contentValues.put(Col3,movies.original_title );
       contentValues.put(Col4,movies.vote_average );
       contentValues.put(Col5,movies.release_date );
       contentValues.put(Col6, movies.overview );
       long x = db.insert(table_name, null, contentValues);
        if (x==-1)
        return false;
        else
        return true;

   }
    public Cursor select()
    {
        SQLiteDatabase db=this.getWritableDatabase();
        String query="SELECT * FROM "+table_name;
        Cursor x=db.rawQuery(query, null);
        return x;

    }
}
*/
