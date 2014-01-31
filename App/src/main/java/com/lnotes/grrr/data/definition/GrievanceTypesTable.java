package com.lnotes.grrr.data.definition;

import android.database.sqlite.SQLiteDatabase;

/**
 * Made a
 */
public class GrievanceTypesTable implements DatabaseTable {

    private static GrievanceTypesTable sInstance;
    public static final String TABLE_GRIEVANCE_TYPES = "grievanceTypes";
    public static final String COLUMN_GRIEVANCE_TYPE_NAME = "grievanceTypeName";
    public static final String COLUMN_CREATE_DATE_TIME = "createDateTime";
    public static final String INDEX_UNIQUE_GRIEVANCE_TYPE_NAME = "idxUniqueGrievanceTypeName";

    /**
     * <p>
     * This table stores unique grievance types.
     * </p>
     */
    private static final String CREATE_GRIEVANCE_TYPES = "CREATE TABLE " + TABLE_GRIEVANCE_TYPES +
            " (" + BlackBileDatabaseHelper.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COLUMN_GRIEVANCE_TYPE_NAME + " TEXT NOT NULL," +
            COLUMN_CREATE_DATE_TIME + " TEXT NOT NULL);" +
            "CREATE UNIQUE INDEX " + INDEX_UNIQUE_GRIEVANCE_TYPE_NAME +
            " ON " + TABLE_GRIEVANCE_TYPES + "(" + COLUMN_GRIEVANCE_TYPE_NAME + " COLLATE nocase);";

    protected GrievanceTypesTable() {

    }

    public static GrievanceTypesTable getInstance() {
        if (sInstance == null) {
            sInstance = new GrievanceTypesTable();
        }
        return sInstance;
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL(CREATE_GRIEVANCE_TYPES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase database) {
        database.execSQL("DROP TABLE IF EXISTS " + TABLE_GRIEVANCE_TYPES);
        onCreate(database);
    }

}
