package com.pacsal.yoursms;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by pacsal on 5/1/2015.
 */
public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "phonebooks.db";
    public static final String TABLE_NAME = "phone_table";
    public static final String COL_1 = "id";
    public static final String COL_2 = "phone";
    public static final String COL_3 = "status";
    public static final String TABLE_SMS = "sms_table";
    public static final String COL_11 = "id";
    public static final String COL_22 = "number";
    public static final String COL_33 = "latlng";
    public static final String COL_44 = "speed";
    public static final String COL_55 = "volume";
    public static final String COL_66 = "date";
    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_NAME + " (" + COL_1 + " INTEGER PRIMARY KEY AUTOINCREMENT," + COL_2 + " TEXT," + COL_3 + " TEXT)");
        //db.execSQL("create table " + TABLE_SMS + " (" + COL_11 + " INTEGER PRIMARY KEY AUTOINCREMENT," + COL_22 + " TEXT," + COL_33 + " TEXT," + COL_44 + " TEXT," + COL_55 + " TEXT," + COL_66 + " TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXIST"+TABLE_NAME);
        //db.execSQL("DROP TABLE IF EXIST"+TABLE_SMS);
        onCreate(db);
    }

    public boolean insertData(String phone, int status){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_2, phone);
        contentValues.put(COL_3, status);
        long result = db.insert(TABLE_NAME,null ,contentValues);
        if(result == -1)
            return false;
        else
            return true;
    }

    public boolean insertSMS(String number, String latlng, String speed, String volume, String date){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_22, number);
        contentValues.put(COL_33, latlng);
        contentValues.put(COL_44, speed);
        contentValues.put(COL_55, volume);
        contentValues.put(COL_66, date);
        long result = db.insert(TABLE_SMS,null ,contentValues);
        if(result == -1)
            return false;
        else
            return true;
    }


    public Cursor getAllData(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from "+TABLE_NAME,null);
        return res;
    }

    public Cursor getAllSMS(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from "+TABLE_SMS,null);
        return res;
    }

    public boolean updateData(String id,int status){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_1,id);
        contentValues.put(COL_3,status);
        db.update(TABLE_NAME,contentValues, "id = ?", new String[] {id});
        return true;
    }

    public Integer deleteData(String id){
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_NAME, "id = ?", new String[] {id});
    }

    public Integer deleteSMS(String id){
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_SMS, "id = ?", new String[] {id});
    }
}
