package com.example.login.ui.login;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {



    public DBHelper(Context context) {
        super(context, "login.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //db.execSQL("CREATE TABLE users(name TEXT NOT NULL, email TEXT NOT NULL PRIMARY KEY, password TEXT NOT NULL, profile_pic BLOB)");
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS users");
        onCreate(db);
    }

    // insert data function
    public boolean insertData(String name, String email, String password, byte[] profile_pic) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", name);
        values.put("email", email);
        values.put("password", password);
        values.put("profile_pic", profile_pic);
        long result = db.insert("users", null, values);
        return result != -1;
    }


    // check email exists function
    public boolean checkEmailExists(String email) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM users WHERE email=?", new String[] { email });
        return cursor.getCount() > 0;
    }

    // check email and password function
    public boolean checkEmailAndPassword(String email, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM users WHERE email=? AND password=?", new String[] { email, password });
        return cursor.getCount() > 0;
    }
}
