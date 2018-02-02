package com.example.sagar.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;



public class GetDataDiseaseReported {

    private Context mContext;

    public GetDataDiseaseReported(Context context) {
        this.mContext = context;
    }

    private SQLiteDatabase open() {
        SQLiteOpenHelper DataBaseSchema = new DataBaseSchema(mContext);
        return DataBaseSchema.getWritableDatabase();
    }

    public int getNoOfData() {

        int count = 0;
        SQLiteDatabase db = open();
        Cursor cursor = db.query("disease_info_data_visualize",
                new String[]{"_id", "disease_name", "age_pie_chart",
                        "disease_bargraph", "gender_bar_graph",},
                null,
                null, null, null, null
        );

        if (cursor.moveToFirst()) {
            do {
                count++;
                cursor.moveToNext();

            } while (!cursor.isAfterLast());
        }
        cursor.close();
        return count;
    }


    public String getData(int row_no, int column_no) {
    /*
       *row no specifies the colun from which we want to get daat
       * column no specifies the typre of data to receive
     */
        String send = null;
        SQLiteDatabase db = open();
        Cursor cursor = db.query("disease_info_data_visualize",
                new String[]{"_id", "disease_name", "age_pie_chart",
                        "disease_bargraph", "gender_bar_graph",},
                null,
                null, null, null, null
        );
        if (cursor.moveToFirst()) {
            do {

                int i = cursor.getInt(0);
                if (cursor.getInt(0) == row_no) {
                    send = cursor.getString(column_no);
                    cursor.close();
                    return send;

                }
                cursor.moveToNext();

            } while (!cursor.isAfterLast());
        }
        cursor.close();
        return send;
    }


}
