package com.example.campuscare;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "CampusCare.db";

    public static final String TABLE_COMPLAINTS = "complaints_table";
    public static final String COL_ID = "ID";
    public static final String COL_NAME = "NAME";
    public static final String COL_LOCATION = "LOCATION";
    public static final String COL_CATEGORY = "CATEGORY";
    public static final String COL_SEVERITY = "SEVERITY";
    public static final String COL_IMAGE = "IMAGE";
    public static final String COL_STATUS = "STATUS";
    public static final String COL_RES_IMAGE = "RESOLUTION_IMAGE";

    public static final String TABLE_USERS = "users_table";
    public static final String COL_USER_ID = "USER_ID";
    public static final String COL_USERNAME = "USERNAME";
    public static final String COL_PASSWORD = "PASSWORD";
    public static final String COL_ROLE = "ROLE";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 6);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_COMPLAINTS + " (" +
                COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_NAME + " TEXT, " +
                COL_LOCATION + " TEXT, " +
                COL_CATEGORY + " TEXT, " +
                COL_SEVERITY + " TEXT, " +
                COL_IMAGE + " TEXT, " +
                COL_STATUS + " TEXT, " +
                COL_RES_IMAGE + " TEXT)");

        db.execSQL("CREATE TABLE " + TABLE_USERS + " (" +
                COL_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_USERNAME + " TEXT, " +
                COL_PASSWORD + " TEXT, " +
                COL_ROLE + " TEXT)");

        db.execSQL("INSERT INTO " + TABLE_USERS + " (USERNAME, PASSWORD, ROLE) VALUES ('student1', '123', 'Student')");
        db.execSQL("INSERT INTO " + TABLE_USERS + " (USERNAME, PASSWORD, ROLE) VALUES ('faculty1', '123', 'Faculty')");
        db.execSQL("INSERT INTO " + TABLE_USERS + " (USERNAME, PASSWORD, ROLE) VALUES ('resolver1', '123', 'Resolver')");
        db.execSQL("INSERT INTO " + TABLE_USERS + " (USERNAME, PASSWORD, ROLE) VALUES ('admin1', '123', 'Admin')");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_COMPLAINTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        onCreate(db);
    }

    public boolean insertData(String name, String location, String category, String severity, String imageUri) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_NAME, name);
        contentValues.put(COL_LOCATION, location);
        contentValues.put(COL_CATEGORY, category);
        contentValues.put(COL_SEVERITY, severity);
        contentValues.put(COL_IMAGE, imageUri);
        contentValues.put(COL_STATUS, "Pending");
        contentValues.put(COL_RES_IMAGE, "");
        long result = db.insert(TABLE_COMPLAINTS, null, contentValues);
        return result != -1;
    }

    public Cursor getAllData() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_COMPLAINTS, null);
    }

    public boolean updateByResolver(int id, String status, String resImageUri) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_STATUS, status);
        contentValues.put(COL_RES_IMAGE, resImageUri);
        long result = db.update(TABLE_COMPLAINTS, contentValues, COL_ID + " = ?", new String[]{String.valueOf(id)});
        return result > 0;
    }

    public boolean acceptResolution(int id, String finalStatus) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_STATUS, finalStatus);
        long result = db.update(TABLE_COMPLAINTS, contentValues, COL_ID + " = ?", new String[]{String.valueOf(id)});
        return result > 0;
    }

    public boolean deleteComplaint(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        long result = db.delete(TABLE_COMPLAINTS, COL_ID + " = ?", new String[]{String.valueOf(id)});
        return result > 0;
    }

    public boolean addUser(String username, String password, String role) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COL_USERNAME, username);
        cv.put(COL_PASSWORD, password);
        cv.put(COL_ROLE, role);
        long result = db.insert(TABLE_USERS, null, cv);
        return result != -1;
    }

    public Cursor getAllUsers() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_USERS, null);
    }

    public boolean deleteUser(int userId) {
        SQLiteDatabase db = this.getWritableDatabase();
        long result = db.delete(TABLE_USERS, COL_USER_ID + " = ?", new String[]{String.valueOf(userId)});
        return result > 0;
    }

    public boolean updatePassword(String username, String newPassword) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COL_PASSWORD, newPassword);
        long result = db.update(TABLE_USERS, cv, COL_USERNAME + " = ?", new String[]{username});
        return result > 0;
    }

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