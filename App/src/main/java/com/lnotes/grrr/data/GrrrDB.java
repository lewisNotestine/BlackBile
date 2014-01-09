package com.lnotes.grrr.data;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.lnotes.grrr.data.model.Grievance;
import com.lnotes.grrr.data.model.GrievanceType;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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

    /**
     * <p>
     * Return all the distinct Grievance Types from the database.
     * </p>
     * @return A list of all the GrievanceTypes in the DB.
     * //TODO: optimize, cache, blah blah blah.
     */
    public List<GrievanceType> selectAllGrievanceTypes() {
        List<GrievanceType> tempList = new ArrayList<>();
        Cursor cursor = mSQLiteDB.rawQuery("select " +
                "gtyp.grievanceTypeID, " +
                "gtyp.grievanceTypeName, " +
                "gtyp.createDateTime, " +
                "count(gtk.grievanceTokenID) AS countInstances " +
                "FROM grievanceTypes AS gtyp " +
                "LEFT OUTER JOIN grievanceTokens AS gtk on gtyp.grievanceTypeID = gtk.grievanceTypeID " +
                "GROUP BY gtyp.grievanceTypeID; ", null);
        while (cursor.moveToNext()) {
            int typeID = cursor.getInt(cursor.getColumnIndex("grievanceTypeID"));
            String name = cursor.getString(cursor.getColumnIndex("grievanceTypeName"));
            String dateString = cursor.getString(cursor.getColumnIndex("createDateTime"));
            int countInstances = cursor.getInt(cursor.getColumnIndex("countInstances"));
            GrievanceType grievanceType = new GrievanceType(typeID, name, dateString, countInstances);
            //TODO: cache the grievanceType here.
            tempList.add(grievanceType);
        }

        return tempList;
    }


    /**
     * <p>
     * Return the count of grievances of a particular type in the database
     * //TODO: consider making this part of Grievance's responsibility and not making too many DB calls as part of a listAdapter as is currently being done.
     * </p>
     */
    public int selectCountOfGrievancesByType(int typeID) {
        int count = 0;
        String[] args = new String[] {String.format("%02d", typeID)};
        Cursor cursor = mSQLiteDB.rawQuery("select COUNT(*) as count from " +
                "grievanceTypes gtyp " +
                "INNER JOIN grievances g on gtyp.grievanceTypeID = g.grievanceTypeID " +
                "WHERE gtyp.grievanceTypeID = ?", args);
        if (cursor.moveToFirst()) {
            count = cursor.getInt(cursor.getColumnIndex("count"));
        }

        return count;
    }
}
