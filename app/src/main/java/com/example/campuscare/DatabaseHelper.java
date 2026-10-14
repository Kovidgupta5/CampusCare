package com.example.campuscare;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "CampusCare.db";

    // Complaints Table
    public static final String TABLE_COMPLAINTS = "complaints_table";
    public static final String COL_ID = "ID";
    public static final String COL_NAME = "NAME";
    public static final String COL_LOCATION = "LOCATION";
    public static final String COL_CATEGORY = "CATEGORY";
    public static final String COL_SEVERITY = "SEVERITY";

    // Users Table (New for Phase 4)
    public static final String TABLE_USERS = "users_table";
    public static final String COL_USER_ID = "USER_ID";
    public static final String COL_USERNAME = "USERNAME";
    public static final String COL_PASSWORD = "PASSWORD";
    public static final String COL_ROLE = "ROLE";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 2); // Increased version to 2 for upgrade
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create Complaints Table
        db.execSQL("CREATE TABLE " + TABLE_COMPLAINTS + " (ID INTEGER PRIMARY KEY AUTOINCREMENT, NAME TEXT, LOCATION TEXT, CATEGORY TEXT, SEVERITY TEXT)");

        // Create Users Table
        db.execSQL("CREATE TABLE " + TABLE_USERS + " (USER_ID INTEGER PRIMARY KEY AUTOINCREMENT, USERNAME TEXT, PASSWORD TEXT, ROLE TEXT)");

        // Insert Default Test Accounts
        db.execSQL("INSERT INTO " + TABLE_USERS + " (USERNAME, PASSWORD, ROLE) VALUES ('student1', '123', 'Student')");
        db.execSQL("INSERT INTO " + TABLE_USERS + " (USERNAME, PASSWORD, ROLE) VALUES ('faculty1', '123', 'Faculty')");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_COMPLAINTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        onCreate(db);
    }

    public boolean insertData(String name, String location, String category, String severity) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_NAME, name);
        contentValues.put(COL_LOCATION, location);
        contentValues.put(COL_CATEGORY, category);
        contentValues.put(COL_SEVERITY, severity);
        long result = db.insert(TABLE_COMPLAINTS, null, contentValues);
        return result != -1;
    }

    public Cursor getAllData() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_COMPLAINTS, null);
    }

    // Verify login and return role ("Student" or "Faculty")
    public String checkUserRole(String username, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT " + COL_ROLE + " FROM " + TABLE_USERS + " WHERE " + COL_USERNAME + "=? AND " + COL_PASSWORD + "=?", new String[]{username, password});

        if (cursor.moveToFirst()) {
            String role = cursor.getString(0);
            cursor.close();
            return role;
        }
        cursor.close();
        return null;
    }
}