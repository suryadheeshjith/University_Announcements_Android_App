package com.example.javaprojone;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHelper_CE extends SQLiteOpenHelper {

    private static DatabaseHelper_CE sInstance;
    public static final String DATABASE_NAME = "AppDatabase.db";

    public static final String TABLE_NAMES[] = {"REGISTRAR",    //1
            "CLUB_ADMIN",   //2
            "CLUB",         //3
            "CLUB_EVENT",   //4
            "CLUB_EVENT_SCHEDULE1", //5
            "CLUB_EVENT_VENUE_CAPACITY", //6
            "CLUB_EVENT_DURATION",  //7
            "EVENT",           //8
            "EVENT_SCHEDULE1",  //9
            "EVENT_VENUE_CAPACITY",   //10
            "EVENT_DURATION"//11
    };



    public static final String COLS[][] = {

            {"REGISTRAR_ID", "NAME", "DESIGNATION"},
            {"ADM_ID", "ADM_NAME", "PASSWORD", "ADM_DEPARTMENT", "ADM_EMAIL", "ADM_USN", "MNGD_CLUB_ID", "MGR_REGISTRAR_ID"},
            {"CL_ID", "CL_NAME", "CL_DESCRIPTION"},


            {"CL_EV_NAME","CL_EV_CRD1_NAME","CL_EV_CRD1_CONTACT","CL_EV_CRD2_NAME","CL_EV_CRD2_CONTACT","ORG_CLUB_ID"},
            {"CL_EV_START_DATE","CL_EV_END_DATE","CL_EV_VENUE","CL_EV_TARGET_AUDIENCE","CL_EV_ID"},
            {"CL_EV_VENUE","CL_EV_SEATING_CAPACITY"},
            {"CL_EV_START_DATE","CL_EV_END_DATE","CL_EV_DURATION"},


            {"EV_NAME","EV_CRD1_NAME","EV_CRD1_CONTACT","EV_CRD2_NAME","EV_CRD2_CONTACT","MGR_REGISTRAR_ID"},



            {"EV_START_DATE","EV_END_DATE","EV_VENUE","EV_TARGET_AUDIENCE","EV_ID"},
            {"EV_VENUE","EV_SEATING_CAPACITY"},
            {"EV_START_DATE","EV_END_DATE","EV_DURATION"}
    };



//    public static final String COLS1[] = {"REGISTRAR_ID","NAME","DESIGNATION"};
//    public static final String COLS2[] = {"ADM_ID","ADM_NAME","PASSWORD","ADM_DEPARTMENT","ADM_EMAIL","ADM_USN","MNGD_CLUB_ID","MGR_REGISTRAR_ID"};
//    public static final String COLS3[] = {"CL_ID","CL_NAME","CL_DESCRIPTION"};
//    public static final String COLS4[] = {"CL_EV_ID","CL_EV_NAME","CL_EV_CRD1_NAME","CL_EV_CRD1_CONTACT","CL_EV_CRD2_NAME","CL_EV_CRD2_CONTACT","ORG_CLUB_ID"};
//    public static final String COLS5[] = {"CL_EV_SCHEDULE_ID","CL_EV_START_DATE","CL_EV_END_DATE","CL_EV_VENUE","CL_EV_TARGET_AUDIENCE","CL_EV_ID"};
//    public static final String COLS6[] = {"CL_EV_VENUE","CL_EV_SEATING_CAPACITY"};
//    public static final String COLS7[] = {"CL_EV_START_DATE","CL_EV_END_DATE","CL_EV_DURATION"};
//    public static final String COLS8[] = {"EV_ID","EV_NAME","EV_CRD1_NAME","EV_CRD1_CONTACT","EV_CRD2_NAME","EV_CRD2_CONTACT","MGR_REGISTRAR_ID"};
//    public static final String COLS9[] = {"EV_SCHEDULE_ID","EV_START_DATE","EV_END_DATE","EV_VENUE","EV_TARGET_AUDIENCE","EV_ID"};
//    public static final String COLS10[] = {"EV_VENUE","EV_SEATING_CAPACITY"};
//    public static final String COLS11[] = {"EV_START_DATE","EV_END_DATE","EV_DURATION"};

    public static  DatabaseHelper_CE getInstance(Context context) {

        // Use the application context, which will ensure that you
        // don't accidentally leak an Activity's context.
        // See this article for more information: http://bit.ly/6LRzfx
        if (sInstance == null) {
            Log.i("Only once","Only once");
            sInstance = new DatabaseHelper_CE(context.getApplicationContext());
        }
        return sInstance;
    }

    private DatabaseHelper_CE(Context context) {


        super(context, DATABASE_NAME, null,8);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("create table REGISTRAR (REGISTRAR_ID VAR_CHAR(20) primary key,NAME VAR_CHAR(20),DESIGNATION VAR_CHAR(20))"); //1
        db.execSQL("create table CLUB_ADMIN (ADM_ID VAR_CHAR(20) primary key,ADM_NAME VAR_CHAR(20),PASSWORD VAR_CHAR(20),ADM_DEPARTMENT VAR_CHAR(20),ADM_EMAIL VAR_CHAR(20),ADM_USN VAR_CHAR(20),MNGD_CLUB_ID VAR_CHAR(20),MGR_REGISTRAR_ID VAR_CHAR(20), FOREIGN KEY(MGR_REGISTRAR_ID) REFERENCES REGISTRAR (REGISTRAR_ID) ON DELETE SET NULL, FOREIGN KEY(MNGD_CLUB_ID) REFERENCES CLUB (CL_ID) ON DELETE CASCADE)"); //2
        db.execSQL("create table CLUB (CL_ID VAR_CHAR(20) primary key,CL_NAME VAR_CHAR(20),CL_DESCRIPTION VAR_CHAR(20))"); //3




        db.execSQL("create table CLUB_EVENT (CL_EV_ID INTEGER primary key autoincrement,CL_EV_NAME VAR_CHAR(20),CL_EV_CRD1_NAME VAR_CHAR(20),CL_EV_CRD1_CONTACT VAR_CHAR(20),CL_EV_CRD2_NAME VAR_CHAR(20),CL_EV_CRD2_CONTACT VAR_CHAR(20),ORG_CLUB_ID VAR_CHAR(20))"); //4
        db.execSQL("create table CLUB_EVENT_SCHEDULE1 (CL_EV_SCHEDULE_ID INTEGER primary key autoincrement,CL_EV_START_DATE TEXT,CL_EV_END_DATE TEXT,CL_EV_VENUE VAR_CHAR(20),CL_EV_TARGET_AUDIENCE VAR_CHAR(20),CL_EV_ID VAR_CHAR(20), FOREIGN KEY(CL_EV_ID) REFERENCES CLUB_EVENT (CL_EV_ID) ON DELETE CASCADE)");//5



        db.execSQL("create table CLUB_EVENT_VENUE_CAPACITY (CL_EV_VENUE VAR_CHAR(20) primary key,CL_EV_SEATING_CAPACITY VAR_CHAR(20))"); //6


        db.execSQL("create table CLUB_EVENT_DURATION (CL_EV_START_DATE TEXT,CL_EV_END_DATE TEXT ,CL_EV_DURATION INT)");// 7




        db.execSQL("create table EVENT (EV_ID INTEGER primary key autoincrement,EV_NAME VAR_CHAR(20),EV_CRD1_NAME VAR_CHAR(20),EV_CRD1_CONTACT VAR_CHAR(20),EV_CRD2_NAME VAR_CHAR(20),EV_CRD2_CONTACT VAR_CHAR(20),MGR_REGISTRAR_ID VAR_CHAR(20))"); //8
        db.execSQL("create table EVENT_SCHEDULE1 (EV_SCHEDULE_ID INTEGER primary key autoincrement,EV_START_DATE TEXT,EV_END_DATE TEXT,EV_VENUE VAR_CHAR(20),EV_TARGET_AUDIENCE VAR_CHAR(20),EV_ID VAR_CHAR(20), FOREIGN KEY(EV_ID) REFERENCES EVENT (EV_ID) ON DELETE CASCADE)");// 9


        db.execSQL("create table EVENT_VENUE_CAPACITY (EV_VENUE VAR_CHAR(20) primary key,EV_SEATING_CAPACITY VAR_CHAR(20))"); //10



        db.execSQL("create table EVENT_DURATION (EV_START_DATE TEXT ,EV_END_DATE TEXT,EV_DURATION INT)");// 11

        db.execSQL("UPDATE sqlite_sequence SET seq = 214 WHERE NAME = 'EVENT_SCHEDULE1'");
        db.execSQL("UPDATE sqlite_sequence SET seq = 724 WHERE NAME = 'CLUB_EVENT_SCHEDULE1'");


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        for(String TABLE_NAME: TABLE_NAMES)
            db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME);
        onCreate(db);
    }

    public boolean insertData(String vals[],int table_no) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        for(int i=0;i<vals.length;i++)
        {
            contentValues.put(COLS[table_no][i],vals[i]);
        }

        long result = db.insert(TABLE_NAMES[table_no],null ,contentValues);
        if(result == -1)
            return false;
        else
            return true;
    }

    public Cursor getData(String TABLE_NAME,String searchField,String val)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from "+TABLE_NAME+" where "+searchField+" =?",new String[] {val});
        return res;
    }

    public boolean deleteEvent( String name) {
        SQLiteDatabase db = this.getWritableDatabase();
        if(db.delete("EVENT", "EV_ID = ?",new String[] {name}) == 1)
            return true;
        else
            return false;
    }

    public boolean deleteClubEvent(String name) {
        SQLiteDatabase db = this.getWritableDatabase();
        if(db.delete("CLUB_EVENT", "CL_EV_ID = ?",new String[] {name}) == 1)
            return true;
        else
            return false;
    }

    public Cursor getAllData(String TABLE_NAME) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from "+TABLE_NAME,null);
        return res;
    }

    public boolean deleteAllData(String TABLE_NAME) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from "+TABLE_NAME);
        return true;

    }

}
