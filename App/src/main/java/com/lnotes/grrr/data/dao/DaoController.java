package com.lnotes.grrr.data.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.lnotes.grrr.data.definition.BlackBileDatabaseHelper;
import com.lnotes.grrr.data.model.GrievanceToken;
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
public class DaoController {

    private SQLiteDatabase mSQLiteDB;
    private static DaoController sInstance;

    public static void createInstance(SQLiteOpenHelper helper) {
        if (sInstance == null) {
            sInstance = new DaoController(helper);
        }
    }

    public static DaoController getInstance() {
        if (sInstance == null) {
            throw new NullPointerException("You should have called createInstance first!");
        }
        return sInstance;
    }

    private DaoController(SQLiteOpenHelper dbHelper) {
        mSQLiteDB = dbHelper.getWritableDatabase();
    }

    /**
     * <p>
     * gets all grievances, independent of tags.
     * </p>
     */
    public List<GrievanceToken> selectAllGrievances() {
        Cursor cursor = mSQLiteDB.rawQuery("select " +
                "gty." + BlackBileDatabaseHelper.COLUMN_ID + " as grievanceTypeID, " +
                "gtk." + BlackBileDatabaseHelper.COLUMN_ID + " as grievanceTokenID, " +
                "gty.grievanceTypeName, " +
                "gtk.createDateTime " +
                "FROM grievanceTokens AS gtk " +
                "INNER JOIN grievanceTypes AS gty on gtk.grievanceTypeID = gty." + BlackBileDatabaseHelper.COLUMN_ID, null);
        List<GrievanceToken> outList = new ArrayList<GrievanceToken>();
        while (cursor.moveToNext()) {
            int typeID = cursor.getInt(cursor.getColumnIndex("grievanceTypeID"));
            int tokenID = cursor.getInt(cursor.getColumnIndex("grievanceTokenID"));
            String name = cursor.getString(cursor.getColumnIndex("grievanceTypeName"));

            //TODO: We'll eventually mediate all calls to create grievanceToken object to the grievanceController.
            GrievanceToken grievanceToken = new GrievanceToken(typeID, tokenID, name);
            outList.add(grievanceToken);
        }

        return outList;
    }

    /**
     * <p>
     * Return all the distinct GrievanceToken Types from the database.
     * </p>
     *
     * @return A list of all the GrievanceTypes in the DB.
     */
    public List<GrievanceType> selectAllGrievanceTypes() {
        List<GrievanceType> tempList = new ArrayList<>();
        Cursor cursor = mSQLiteDB.rawQuery("select " +
                "gtyp." + BlackBileDatabaseHelper.COLUMN_ID + ", " +
                "gtyp.grievanceTypeName, " +
                "gtyp.createDateTime, " +
                "count(gtk." + BlackBileDatabaseHelper.COLUMN_ID + ") AS countInstances " +
                "FROM grievanceTypes AS gtyp " +
                "LEFT OUTER JOIN grievanceTokens AS gtk on gtyp." + BlackBileDatabaseHelper.COLUMN_ID + " = gtk.grievanceTypeID " +
                "GROUP BY gtyp." + BlackBileDatabaseHelper.COLUMN_ID + "; ", null);
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
        Cursor cursor = mSQLiteDB.rawQuery("SELECT COUNT(*) AS count from " +
                "grievanceTypes gtyp " +
                "INNER JOIN grievances g on gtyp." + BlackBileDatabaseHelper.COLUMN_ID + " = g.grievanceTypeID " +
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
                BlackBileDatabaseHelper.COLUMN_ID +  ", " +
                "grievanceTagName " +
                " from grievanceTags", null);
    }

    public boolean insertGrievanceType(GrievanceType newGrievanceType) {
        try {
            ContentValues typeValues = new ContentValues();
            typeValues.put("grievanceTypeName", newGrievanceType.getName());
            typeValues.put("createDateTime", BlackBileDatabaseHelper.DATE_FORMAT.format(newGrievanceType.getCreateDate()));
            final long typeRowId = mSQLiteDB.insertWithOnConflict("grievanceTypes", null, typeValues, SQLiteDatabase.CONFLICT_IGNORE);

            for (GrievanceTag tag : newGrievanceType.getGrievanceTags()) {
                ContentValues tagValues = new ContentValues();
                tagValues.put("grievanceTagName", tag.getName());
                tagValues.put("createDateTime", BlackBileDatabaseHelper.DATE_FORMAT.format(tag.getDateCreated()));
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
                    "select " + BlackBileDatabaseHelper.COLUMN_ID + ", " +
                    BlackBileDatabaseHelper.DATE_FORMAT.format(grievanceType.getCreateDate()) + " " +
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
