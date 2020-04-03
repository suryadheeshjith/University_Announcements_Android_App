package com.example.javaprojone;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class DatabaseHelper extends SQLiteOpenHelper {


    public static final String DATABASE_NAME = "Users.db";
    public static final String TABLE_NAME = "nav_drawer";
    public static final String COL_1 = "Name";
    public static final String COL_2 = "Value";
    public static final String COL_3 = "Type";


    public DatabaseHelper(Context context) {


        super(context, DATABASE_NAME, null,15);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {


        db.execSQL("create table " + TABLE_NAME +" (NAME TEXT primary key,Value TEXT,Type TEXT)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME);
        onCreate(db);
    }
    public boolean insertData(String name,String value,String type) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_1,name);
        contentValues.put(COL_2,value);
        contentValues.put(COL_3,type);
        long result = db.insert(TABLE_NAME,null ,contentValues);
        if(result == -1)
            return false;
        else
            return true;
    }

    public Cursor getAllData() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from "+TABLE_NAME,null);
        return res;
    }

    public boolean updateData(String name,String value,String type) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_1,name);
        contentValues.put(COL_2,value);
        contentValues.put(COL_3,type);
        db.update(TABLE_NAME, contentValues, "name = ?",new String[] { name });
        return true;
    }

    public Integer deleteData (String name) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_NAME, "name = ?",new String[] {name});
    }

    public boolean findData(String name)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        String selectString = "SELECT * FROM " + TABLE_NAME + " WHERE " + COL_1 + " =?";
        Cursor res = db.rawQuery(selectString,new String[] {name});
        if(res.getCount()<=0)
            return false;

        return true;
    }

}
