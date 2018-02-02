package com.example.sagar.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Sagar on 11/18/2017.
 */

public class GetUserData {
    private Context mContext;
    private String send;

    public GetUserData(Context context) {
        this.mContext = context;
    }

    private SQLiteDatabase open() {
        SQLiteOpenHelper DataBase = new DataBaseSchema(mContext);
        return DataBase.getReadableDatabase();
    }

    //request specifies the data required by the caller
    public String getData(String request) {
        SQLiteDatabase db = open();
        Cursor cursor = db.query("login_info",
                new String[]{"_id", "Token", "user_type", "email", "user_name"},
                null,
                null, null, null, null
        );
        send = getDataAccToRequest(cursor, request);
        cursor.close();
        return send;
    }


    private String getDataAccToRequest(Cursor cursor, String request) {
        switch (request) {
            case "Token":
                String s = getEntry(cursor, 1);
                return getEntry(cursor, 1);

            case "user_type":
                return getEntry(cursor, 2);

            case "email":
                return getEntry(cursor, 3);
            case "user_name":
                return getEntry(cursor, 4);

            default:
                return null;

        }
    }

    private String getEntry(Cursor cursor, int column_no) {
        String queried_data = null;
        if (cursor.moveToFirst()) {
            do {
                queried_data = cursor.getString(column_no);
                cursor.moveToNext();

            } while (!cursor.isAfterLast());
        }
        return queried_data;
    }

}
