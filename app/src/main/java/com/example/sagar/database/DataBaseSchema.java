package com.example.sagar.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Sagar on 11/14/2017.
 */

public class DataBaseSchema extends SQLiteOpenHelper {

    private static String DB_NAME = "SickLabDatabase";
    private static String TABLE_LOGIN_INFO = "login_info";
    private static String TABLE_DISEASE_INFO = "disease_info";
    private static String TABLE_DISEASE_INFO_PREVIOUS = "disease_info_previously_seen";
    private static String TABLE_DISEASE_VISUALIZE = "disease_info_data_visualize";
    private static int DB_VERSION = 1;


    DataBaseSchema(Context context) {
        super(context, DB_NAME, null, DB_VERSION);//null is for cursors//sqlite helper classes constructor is being called

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //Sqlitedatabase class gives us access to database

        db.execSQL("CREATE TABLE " + TABLE_LOGIN_INFO + "("
                + "_id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "Token TEXT,"
                + "user_type TEXT,"
                + "email TEXT,"
                + "user_name TEXT);");


        db.execSQL("CREATE TABLE " + TABLE_DISEASE_INFO + "("
                + "_id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "disease_name TEXT,"
                + "district TEXT,"
                + "no_of_reports TEXT,"
                + "first_reported TEXT,"
                + "last_reported TEXT,"
                + "image_link TEXT,"
                + "description TEXT);");

        db.execSQL("CREATE TABLE " + TABLE_DISEASE_INFO_PREVIOUS + "("
                + "_id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "disease_name TEXT,"
                + "district TEXT,"
                + "no_of_reports TEXT,"
                + "first_reported TEXT,"
                + "last_reported TEXT,"
                + "image_link TEXT,"
                + "description TEXT);");

        db.execSQL("CREATE TABLE " + TABLE_DISEASE_VISUALIZE + "("
                + "_id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "disease_name TEXT,"
                + "age_pie_chart TEXT,"
                + "disease_bargraph TEXT,"
                + "gender_bar_graph TEXT);");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
