package com.example.sagar.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Sagar on 11/19/2017.
 */

public class DataStoreDiseaseReported {

    private Context mContext;

    public DataStoreDiseaseReported(Context context) {
        this.mContext = context;
    }

    private SQLiteDatabase open() {
        SQLiteOpenHelper DataBaseSchema = new DataBaseSchema(mContext);
        return DataBaseSchema.getWritableDatabase();
    }

    public void storeDiseaseInfo(String disease_name, String age_pie_chart,
                                 String disease_bargraph, String gender_bar_graph,
                                 boolean delete_table,boolean notclear) {
        SQLiteDatabase db = open();
        if (delete_table) {
            db.delete("disease_info_data_visualize", null, null);
            db.execSQL("DELETE FROM SQLITE_SEQUENCE WHERE NAME = '" + "disease_info_data_visualize" + "'");

        }
        if (!notclear) {
            ContentValues user_data = new ContentValues();
            user_data.put("disease_name", disease_name);
            user_data.put("age_pie_chart", age_pie_chart);
            user_data.put("disease_bargraph", disease_bargraph);
            user_data.put("gender_bar_graph", gender_bar_graph);


            db.insert("disease_info_data_visualize", null, user_data);
        }
    }

}
