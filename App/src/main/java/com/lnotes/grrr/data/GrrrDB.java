package com.lnotes.grrr.data;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.lnotes.grrr.data.model.Grievance;
import com.lnotes.grrr.data.model.GrievanceTag;
import com.lnotes.grrr.data.model.GrievanceType;

import android.database.SQLException;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * Responsible for wrapping a SQLiteDatabase and handling querying and CRUD operations on it.
 * </p>
 * Created by LN_1 on 12/11/13.
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

    /**
     * <p>
     * gets all grievances, independent of tags.
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
     *
     * @return A list of all the GrievanceTypes in the DB.
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
            String name = cursor.getString(cursor.getColumnIndex("grievanceTypeName"));
            String dateString = cursor.getString(cursor.getColumnIndex("createDateTime"));
            int countInstances = cursor.getInt(cursor.getColumnIndex("countInstances"));
            GrievanceType grievanceType = new GrievanceType(name, dateString, countInstances);
            //TODO: cache the grievanceType here.
            tempList.add(grievanceType);
        }

        return tempList;
    }


    /**
     * <p>
     * Return the count of grievances of a particular type in the database
     * </p>
     */
    public int selectCountOfGrievancesByType(int typeID) {
        int count = 0;
        String[] args = new String[]{String.format("%02d", typeID)};
        Cursor cursor = mSQLiteDB.rawQuery("select COUNT(*) as count from " +
                "grievanceTypes gtyp " +
                "INNER JOIN grievances g on gtyp.grievanceTypeID = g.grievanceTypeID " +
                "WHERE gtyp.grievanceTypeID = ?", args);
        if (cursor.moveToFirst()) {
            count = cursor.getInt(cursor.getColumnIndex("count"));
        }

        return count;
    }


    /**
     * <p>
     * Gets a {@link android.database.Cursor} representing {@link com.lnotes.grrr.data.model.GrievanceTag} objects
     * to be used by client widgets,
     * as in {@link com.lnotes.grrr.fragment.AddGrievanceTypeDialogFragment#setDataForAutoCompletion()}.
     * </p>
     *
     * @return the cursor that reprsents all {@link com.lnotes.grrr.data.model.GrievanceTag} objects in the db.
     */
    public Cursor getTagsCursor() {
        return mSQLiteDB.rawQuery("select " +
                "grievanceTagID as _id, " +
                "grievanceTagName " +
                " from grievanceTags", null);
    }

    public boolean insertGrievanceType(GrievanceType newGrievanceType) {
        try {
            ContentValues typeValues = new ContentValues();
            typeValues.put("grievanceTypeName", newGrievanceType.getName());
            typeValues.put("createDateTime", GrrrDatabaseHelper.DATE_FORMAT.format(newGrievanceType.getCreateDate()));
            final long typeRowId = mSQLiteDB.insertWithOnConflict("grievanceTypes", null, typeValues, SQLiteDatabase.CONFLICT_IGNORE);

            for (GrievanceTag tag : newGrievanceType.getGrievanceTags()) {
                ContentValues tagValues = new ContentValues();
                tagValues.put("grievanceTagName", tag.getName());
                tagValues.put("createDateTime", GrrrDatabaseHelper.DATE_FORMAT.format(tag.getDateCreated()));
                final long tagRowId = mSQLiteDB.insertWithOnConflict("grievanceTags", null, tagValues, SQLiteDatabase.CONFLICT_IGNORE);

                ContentValues tagTypeValues = new ContentValues();
                tagTypeValues.put("grievanceTypeID", typeRowId);
                tagTypeValues.put("grievanceTagID", tagRowId);
                mSQLiteDB.insertWithOnConflict("grievanceTypesTags", null, tagTypeValues, SQLiteDatabase.CONFLICT_IGNORE);
            }

            return true;
        } catch (SQLException ex) {
            //TODO: better exception handling, instead of using this to do flow control here.
            return false;
        }
    }

    public boolean insertGrievanceTag(GrievanceTag newGrievanceTag) {
        return false;
    }

    public boolean insertGrievanceToken(GrievanceType grievanceType) {
        try {
            //TODO: need to implement proper relationships etcs.
            mSQLiteDB.execSQL("INSERT INTO grievanceTokens (grievanceTypeId, createDateTime) " +
                    "select grievanceTypeId, " +
                    GrrrDatabaseHelper.DATE_FORMAT.format(grievanceType.getCreateDate()) + " " +
                    "FROM grievanceTypes " +
                    "WHERE grievanceTypeName = " + grievanceType.getName());
            return true;
        } catch (SQLException ex) {
            return false;
        }
    }

    public boolean insertGrievanceTypeTag(GrievanceTag tag, GrievanceType type) {
        return false;
    }
}
