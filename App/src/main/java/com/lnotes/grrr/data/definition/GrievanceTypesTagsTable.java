package com.lnotes.grrr.data.definition;

import android.database.sqlite.SQLiteDatabase;

/**
 * Table to implement many-to-many relationship between GrievanceType and GrievanceToken.
 */
public class GrievanceTypesTagsTable implements DatabaseTable {

    private static GrievanceTypesTagsTable sInstance;
    public static final String TABLE_GRIEVANCE_TYPES_TAGS = "grievanceTypesTags";
    public static final String COLUMN_GRIEVANCE_TYPE_ID = "grievanceTypeID";
    public static final String COLUMN_GRIEVANCE_TAG_ID = "grievanceTagID";


    /**
     * <p>
     * A grievance type can, by default, have a set of tags associated with it.  This relationship is many-to-many.
     * </p>
     */
    private static final String CREATE_GRIEVANCE_TYPES_TAGS = "CREATE TABLE " + TABLE_GRIEVANCE_TYPES_TAGS + "(" +
            BlackBileDatabaseHelper.COLUMN_ID + " integer primary key autoincrement," +
            COLUMN_GRIEVANCE_TYPE_ID + " integer not null," +
            COLUMN_GRIEVANCE_TAG_ID + " integer not null," +
            "UNIQUE(" + COLUMN_GRIEVANCE_TYPE_ID + ", " + COLUMN_GRIEVANCE_TAG_ID + ") ON CONFLICT ROLLBACK," +
            "FOREIGN KEY(grievanceTypeID) REFERENCES grievanceTypes(grievanceTypeID)," +
            "FOREIGN KEY(grievanceTagID) REFERENCES grievanceTags(grievanceTagID));";

    public static GrievanceTypesTagsTable getInstance() {
        if (sInstance == null) {
            sInstance = new GrievanceTypesTagsTable();
        }
        return sInstance;
    }


    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL(CREATE_GRIEVANCE_TYPES_TAGS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase database) {
        database.execSQL("DROP TABLE IF EXISTS " + TABLE_GRIEVANCE_TYPES_TAGS);
        onCreate(database);
    }
}
