package com.lnotes.grrr.data.definition;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.lnotes.grrr.GrrrApplication;

import java.text.SimpleDateFormat;

/**
 * Created by LN_1 on 12/11/13.
 * <p>
 * Class for Data definition language for the Logger database.
 * </p>
 */
public class BlackBileDatabaseHelper extends SQLiteOpenHelper {

    //TODO: put this constant in DatabaseTable instead.
    public static final String COLUMN_ID = "_id";

    //TODO: need hours, minutes and seconds.
    public static String DATE_FORMAT_STRING = "yyyy-MM-dd";
    public static SimpleDateFormat DATE_FORMAT  = new SimpleDateFormat(DATE_FORMAT_STRING);

    private static BlackBileDatabaseHelper sInstance;

    public static BlackBileDatabaseHelper getInstance() {
        if (sInstance == null) {
            sInstance = new BlackBileDatabaseHelper();
        }
        return sInstance;
    }

    protected BlackBileDatabaseHelper() {
        super(GrrrApplication.getLoggrrrApplicationContext(), "testDBName", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqliteDatabase) {
        if (sqliteDatabase != null) {
            GrievanceTagsTable.getInstance().onCreate(sqliteDatabase);
            GrievanceTypesTable.getInstance().onCreate(sqliteDatabase);
            GrievanceTokensTable.getInstance().onCreate(sqliteDatabase);
            GrievanceTypesTagsTable.getInstance().onCreate(sqliteDatabase);
            GrievanceTokensTagsTable.getInstance().onCreate(sqliteDatabase);

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
        database.execSQL("insert into grievanceTags(grievanceTagName, createDateTime) VALUES('tagName2', '2013-12-11');");
        database.execSQL("insert into grievanceTags(grievanceTagName, createDateTime) VALUES('tagName3', '2013-12-11');");
        database.execSQL("insert into grievanceTypes(grievanceTypeName, createDateTime) VALUES('testGrievanceName', '2013-12-11');");
        database.execSQL("insert into grievanceTokens(grievanceTypeID, createDateTime) VALUES( 1, '2013-12-11');");
        database.execSQL("insert into grievanceTokens(grievanceTypeID, createDateTime) VALUES( 1, '2013-12-11');");
        database.execSQL("insert into grievanceTypes(grievanceTypeName, createDateTime) VALUES('testGrievanceName2', '2013-12-11');");
        database.execSQL("insert into grievanceTypes(grievanceTypeName, createDateTime) VALUES('testGrievanceName3', '2013-12-11');");
        database.execSQL("insert into grievanceTokens(grievanceTypeID, createDateTime) VALUES( 2, '2013-12-11');");
        database.execSQL("insert into grievanceTokens(grievanceTypeID, createDateTime) VALUES( 3, '2013-12-11');");
    }
}
