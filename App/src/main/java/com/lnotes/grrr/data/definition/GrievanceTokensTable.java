package com.lnotes.grrr.data.definition;

import android.database.sqlite.SQLiteDatabase;

/**
 * DatabaseTable for GrievanceToken object data.
 */
public class GrievanceTokensTable implements DatabaseTable{

    private static GrievanceTokensTable sInstance;
    public static final String TABLE_GRIEVANCE_TOKENS = "grievanceTokens";
    public static final String COLUMN_GRIEVANCE_TYPE_ID = "grievanceTypeID";
    public static final String COLUMN_CREATE_DATE_TIME = "createDateTime";
    public static final String COLUMN_MAGNITUDE = "magnitude";


    /**
     * <p>
     * This table stores instances of a grievance, as logged by the user.
     * </p>
     */
    private static final String CREATE_GRIEVANCE_TOKENS = "CREATE TABLE " + TABLE_GRIEVANCE_TOKENS + " (" + BlackBileDatabaseHelper.COLUMN_ID + " integer primary key autoincrement, " +
            COLUMN_GRIEVANCE_TYPE_ID + " integer not null," +
            COLUMN_CREATE_DATE_TIME + " text not null," +
            COLUMN_MAGNITUDE + " REAL not null, " +
            "FOREIGN KEY(" + COLUMN_GRIEVANCE_TYPE_ID + ") REFERENCES " + GrievanceTypesTable.TABLE_GRIEVANCE_TYPES +  "(" + BlackBileDatabaseHelper.COLUMN_ID + "));";

    public static GrievanceTokensTable getInstance() {
        if (sInstance == null) {
            sInstance = new GrievanceTokensTable();
        }
        return sInstance;
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL(CREATE_GRIEVANCE_TOKENS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase database) {
        //TODO: this implementation might be problematic for dependencies/FK relationships.
        database.execSQL("DROP TABLE IF EXISTS " + TABLE_GRIEVANCE_TOKENS);
        onCreate(database);
    }
}
