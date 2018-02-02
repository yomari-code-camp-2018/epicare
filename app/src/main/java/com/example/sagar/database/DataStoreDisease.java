package com.example.sagar.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Sagar on 11/19/2017.
 */

public class DataStoreDisease {

    private Context mContext;

    public DataStoreDisease(Context context) {
        this.mContext = context;
    }

    private SQLiteDatabase open() {
        SQLiteOpenHelper DataBaseSchema = new DataBaseSchema(mContext);
        return DataBaseSchema.getWritableDatabase();
    }

    public void storeDiseaseInfo(String disease_name, String district,
                                 String no_of_reports, String first_reported, String last_reported,
                                String image_link,String description,boolean delete_table,boolean notclear) {
        SQLiteDatabase db = open();
        if (delete_table) {
            db.delete("disease_info", null, null);
            db.execSQL("DELETE FROM SQLITE_SEQUENCE WHERE NAME = '" + "disease_info" + "'");

        }
        if (!notclear) {
            ContentValues user_data = new ContentValues();
            user_data.put("disease_name", disease_name);
            user_data.put("district", district);
            user_data.put("no_of_reports", no_of_reports);
            user_data.put("first_reported", first_reported);
            user_data.put("last_reported", last_reported);
            user_data.put("image_link", image_link);
            user_data.put("description", description);

            db.insert("disease_info", null, user_data);
        }
    }
}
