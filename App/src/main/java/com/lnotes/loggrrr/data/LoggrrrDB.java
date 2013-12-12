package com.lnotes.loggrrr.data;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * <p>
 * Responsible for wrapping a SQLiteDatabase and handling CRUD operations on it.
 * </p>
 * Created by LN_1 on 12/11/13.
 */
public class LoggrrrDB {

    private SQLiteDatabase mSQLiteDB;

    public LoggrrrDB(SQLiteOpenHelper dbHelper) {
        mSQLiteDB = dbHelper.getWritableDatabase();
    }

    //TODO: implement CRUD operations.


}
