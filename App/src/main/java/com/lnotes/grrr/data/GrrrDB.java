package com.lnotes.grrr.data;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.lnotes.grrr.data.model.Grievance;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * <p>
 * Responsible for wrapping a SQLiteDatabase and handling querying and CRUD operations on it.
 * </p>
 * Created by LN_1 on 12/11/13.
 * //TODO: consider making a singleton.
 */
public class GrrrDB {

    private SQLiteDatabase mSQLiteDB;
    private static GrrrDB sInstance;

    public static void createInstance(SQLiteOpenHelper helper) {
        if (sInstance == null) {
            sInstance = new GrrrDB(helper);
        }
    }

    public static GrrrDB getInstance() {
        if (sInstance == null) {
            throw new NullPointerException("You should have called createInstance first!");
        }
        return sInstance;
    }

    private GrrrDB(SQLiteOpenHelper dbHelper) {
        mSQLiteDB = dbHelper.getWritableDatabase();
    }

    //TODO: implement CRUD operations.


    /**
     * <p>
     * gets all grievances, independent of tags.
     * TODO: handle the tags.
     * </p>
     */
    public List<Grievance> selectAllGrievances() {
        Cursor cursor = mSQLiteDB.rawQuery("select " +
                "gty.grievanceTypeID, " +
                "gtk.grievanceTokenID, " +
                "gty.grievanceTypeName, " +
                "gtk.createDateTime " +
                "from grievanceTokens AS gtk " +
                "inner join grievanceTypes AS gty on gtk.grievanceTypeID = gty.grievanceTypeID", null);
        List<Grievance> outList = new ArrayList<Grievance>();
        while (cursor.moveToNext()) {
            int typeID = cursor.getInt(cursor.getColumnIndex("grievanceTypeID"));
            int tokenID = cursor.getInt(cursor.getColumnIndex("grievanceTokenID"));
            String name = cursor.getString(cursor.getColumnIndex("grievanceTypeName"));

            //TODO: We'll eventually mediate all calls to create grievance object to the grievanceController.
            Grievance grievance = new Grievance(typeID, tokenID, name);
            outList.add(grievance);
        }

        return outList;
    }

}
