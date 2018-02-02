package com.example.sagar.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Sagar on 11/18/2017.
 */

public class DataStoreUser {

    private Context mContext;

    public DataStoreUser(Context context) {
        this.mContext = context;
    }

    private SQLiteDatabase open() {
        SQLiteOpenHelper DataBaseSchema = new DataBaseSchema(mContext);
        return DataBaseSchema.getWritableDatabase();
    }

    public void storeCredentials(String token, String user_type, String email,String user_name,boolean delete_table, boolean notclear) {

        SQLiteDatabase db = open();
        if (delete_table) {
            db.delete("login_info", null, null);
            db.execSQL("DELETE FROM SQLITE_SEQUENCE WHERE NAME = '" + "login_info" + "'");

        }
        if (!notclear) {
            ContentValues user_data = new ContentValues();
            user_data.put("Token", token);
            user_data.put("user_type", user_type);
            user_data.put("email", email);
            user_data.put("user_name", user_name);

            db.insert("login_info", null, user_data);
        }
    }
}
