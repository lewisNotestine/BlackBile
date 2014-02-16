package com.lnotes.grrr.data.definition;

import android.database.sqlite.SQLiteDatabase;

/**
 * Definition/schema for grievance tags.
 */
public class GrievanceTagsTable implements DatabaseTable {

    private static GrievanceTagsTable sInstance;
    public static final String TABLE_GRIEVANCE_TAGS = "grievanceTags";
    public static final String COLUMN_GRIEVANCE_TAG_NAME = "grievanceTagName";
    public static final String COLUMN_CREATE_DATE_TIME = "createDateTime";
    public static final String INDEX_UNIQUE_GRIEVANCE_TAG_NAME = "idxUniqueGrievanceTagName";
    private static final String CREATE_GRIEVANCE_TAGS = "CREATE TABLE " + TABLE_GRIEVANCE_TAGS +
            "(" +BlackBileDatabaseHelper.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            COLUMN_GRIEVANCE_TAG_NAME + " TEXT UNIQUE NOT NULL," +
            COLUMN_CREATE_DATE_TIME + " text not null);" +
            "CREATE UNIQUE INDEX " + INDEX_UNIQUE_GRIEVANCE_TAG_NAME +
            " ON " + TABLE_GRIEVANCE_TAGS + "(" + COLUMN_GRIEVANCE_TAG_NAME + " COLLATE nocase);";

    protected GrievanceTagsTable() {

    }

    public static GrievanceTagsTable getInstance() {
        if (sInstance == null) {
            sInstance = new GrievanceTagsTable();
        }
        return sInstance;
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL(CREATE_GRIEVANCE_TAGS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase database) {
        database.execSQL("DROP TABLE IF EXISTS " + TABLE_GRIEVANCE_TAGS);
        onCreate(database);
    }


}
