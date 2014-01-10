package com.lnotes.grrr.data;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.lnotes.grrr.GrrrApplication;

import java.util.Calendar;

/**
 * Created by LN_1 on 12/11/13.
 * <p>
 * Class for Data definition language for the Logger database.
 * </p>
 */
public class GrrrDatabaseHelper extends SQLiteOpenHelper {

    public static String DATE_FORMAT = "yyyy-MM-dd";


    /**
     * <p>
     * These are tags that can be assigned to a grievance.
     * </p>
     */
    private static final String CREATE_GRIEVANCE_TAGS = "CREATE TABLE grievanceTags(grievanceTagID integer primary key autoincrement," +
            "grievanceTagName text not null," +
            "createDateTime text not null);" +
            "CREATE UNIQUE INDEX idxUniqueGrievanceTagName ON grievanceTags(grievanceTagName COLLATE nocase);";


    /**
     * <p>
     * This table stores unique grievance types.
     * </p>
     */
    private static final String CREATE_GRIEVANCE_TYPES = "CREATE TABLE grievanceTypes (grievanceTypeID integer primary key autoincrement, " +
            "grievanceTypeName string not null," +
            "createDateTime text not null);" +
            "CREATE UNIQUE INDEX idxUniqueGrievanceName ON grievanceTypes(grievanceTypeName COLLATE nocase);";


    /**
     * <p>
     * This table stores instances of a grievance, as logged by the user.
     * </p>
     */
    private static final String CREATE_GRIEVANCE_TOKENS = "CREATE TABLE grievanceTokens (grievanceTokenID integer primary key autoincrement, " +
            "grievanceTypeID integer not null," +
            "createDateTime text not null," +
            "FOREIGN KEY(grievanceTypeID) REFERENCES grievanceTypes(grievanceTypeID));";


    /**
     * <p>
     * A grievance type can, by default, have a set of tags associated with it.  This relationship is many-to-many.
     * </p>
     */
    private static final String CREATE_GRIEVANCE_TYPES_TAGS = "CREATE TABLE grievanceTypesTags(" +
            "grievanceTypeTagID integer primary key autoincrement," +
            "grievanceTypeID integer not null," +
            "grievanceTagID integer not null," +
            "UNIQUE(grievanceTypeID, grievanceTagID) ON CONFLICT ROLLBACK," +
            "FOREIGN KEY(grievanceTypeID) REFERENCES grievanceTypes(grievanceTypeID)," +
            "FOREIGN KEY(grievanceTagID) REFERENCES grievanceTags(grievanceTagID));";


    /**
     * <p>
     * A grievance token can have custom tokens assigned to it (as separate from, or extending, its types.  This only needs to be populated if different than its types.
     * Can be flagged as either adding or subtracting a tag from the token.
     * NOTE: if the the boolean isSubtracted field is set to 1, then the tag will represent a tag that already belongs to the grievanceType,
     * and is being taken away instead of added.
     * </p>
     */
    private static final String CREATE_GRIEVANCE_TOKENS_TAGS = "CREATE TABLE grievanceTokensTags(" +
            "grievanceTokenTagID integer primary key autoincrement," +
            "grievanceTokenID integer not null," +
            "grievanceTagID integer not null," +
            "isSubtracted integer not null," +
            "UNIQUE(grievanceTokenID, grievanceTagID) ON CONFLICT ROLLBACK, " +
            "FOREIGN KEY(grievanceTokenID) REFERENCES grievanceTokens(grievanceTokenID)," +
            "FOREIGN KEY(grievanceTagID) REFERENCES grievanceTags(grievanceTagID));";


    private static GrrrDatabaseHelper sInstance;

    public static GrrrDatabaseHelper getInstance() {
        if (sInstance == null) {
            sInstance = new GrrrDatabaseHelper();
        }
        return sInstance;
    }

    protected GrrrDatabaseHelper() {
        super(GrrrApplication.getLoggrrrApplicationContext(), "testDBName", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqliteDatabase) {
        if (sqliteDatabase != null) {
            sqliteDatabase.execSQL(CREATE_GRIEVANCE_TAGS);
            sqliteDatabase.execSQL(CREATE_GRIEVANCE_TYPES);
            sqliteDatabase.execSQL(CREATE_GRIEVANCE_TOKENS);
            sqliteDatabase.execSQL(CREATE_GRIEVANCE_TYPES_TAGS);
            sqliteDatabase.execSQL(CREATE_GRIEVANCE_TOKENS_TAGS);

            //TODO: this is only temporary.
            createTestData(sqliteDatabase);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i2) {
        //TODO: database upgrade.
    }

    /**
     * <p>
     * This is just a temp method for testing database ops.  It inserts a test dataset into the db.
     * </p>
     */
    public void createTestData(SQLiteDatabase database) {
        database.execSQL("insert into grievanceTags(grievanceTagName, createDateTime) VALUES('tagName1', '2013-12-11');");
        database.execSQL("insert into grievanceTypes(grievanceTypeName, createDateTime) VALUES('testGrievanceName', '2013-12-11');");
        database.execSQL("insert into grievanceTokens(grievanceTypeID, createDateTime) VALUES( 1, '2013-12-11');");
        database.execSQL("insert into grievanceTokens(grievanceTypeID, createDateTime) VALUES( 1, '2013-12-11');");
        database.execSQL("insert into grievanceTypes(grievanceTypeName, createDateTime) VALUES('testGrievanceName2', '2013-12-11');");
        database.execSQL("insert into grievanceTypes(grievanceTypeName, createDateTime) VALUES('testGrievanceName3', '2013-12-11');");
        database.execSQL("insert into grievanceTokens(grievanceTypeID, createDateTime) VALUES( 2, '2013-12-11');");
        database.execSQL("insert into grievanceTokens(grievanceTypeID, createDateTime) VALUES( 3, '2013-12-11');");

        //TODO: try to insert tags.  Just working on bare bones stuff for now.
    }
}
