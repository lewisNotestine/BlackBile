package com.lnotes.grrr.data.definition;

import android.database.sqlite.SQLiteDatabase;

/**
 * Implements the Many-to-many relationship between Tokens and Tags.
 */
public class GrievanceTokensTagsTable implements DatabaseTable {

    private static GrievanceTokensTagsTable sInstance;
    public static final String TABLE_GRIEVANCE_TOKENS_TAGS = "grievanceTokensTags";
    public static final String COLUMN_GRIEVANCE_TOKEN_ID = "grievanceTokenID";
    public static final String COLUMN_GRIEVANCE_TAG_ID = "grievanceTagID";
    public static final String COLUMN_IS_SUBTRACTED = "isSubtracted";

    /**
     * <p>
     * A grievance token can have custom tokens assigned to it (as separate from, or extending, its types.  This only needs to be populated if different than its types.
     * Can be flagged as either adding or subtracting a tag from the token.
     * NOTE: if the the boolean isSubtracted field is set to 1, then the tag will represent a tag that already belongs to the grievanceType,
     * and is being taken away instead of added.
     * </p>
     */
    private static final String CREATE_GRIEVANCE_TOKENS_TAGS = "CREATE TABLE grievanceTokensTags(" +
            BlackBileDatabaseHelper.COLUMN_ID + " integer primary key autoincrement," +
            COLUMN_GRIEVANCE_TOKEN_ID + " integer not null," +
            COLUMN_GRIEVANCE_TAG_ID + " integer not null," +
            COLUMN_IS_SUBTRACTED + " integer not null," +
            "UNIQUE(" + COLUMN_GRIEVANCE_TOKEN_ID + ", " + COLUMN_GRIEVANCE_TAG_ID + ") ON CONFLICT ROLLBACK, " +
            "FOREIGN KEY(" + COLUMN_GRIEVANCE_TOKEN_ID+ ") REFERENCES " + GrievanceTokensTable.TABLE_GRIEVANCE_TOKENS + "(" + BlackBileDatabaseHelper.COLUMN_ID + ")," +
            "FOREIGN KEY(" + COLUMN_GRIEVANCE_TAG_ID + ") REFERENCES " + GrievanceTagsTable.TABLE_GRIEVANCE_TAGS + "(" + BlackBileDatabaseHelper.COLUMN_ID + "));";

    protected GrievanceTokensTagsTable() {

    }

    public static GrievanceTokensTagsTable getInstance() {
        if (sInstance == null) {
            sInstance = new GrievanceTokensTagsTable();
        }
        return sInstance;
    }


    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL(CREATE_GRIEVANCE_TOKENS_TAGS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase database) {
        database.execSQL("DROP TABLE IF EXISTS " + TABLE_GRIEVANCE_TOKENS_TAGS);
        onCreate(database);
    }


}
