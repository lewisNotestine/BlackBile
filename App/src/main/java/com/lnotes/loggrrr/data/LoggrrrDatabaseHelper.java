package com.lnotes.loggrrr.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by LN_1 on 12/11/13.
 * <p>
 * Class for Data definition language for the Logger database.
 * </p>
 */
public class LoggrrrDatabaseHelper extends SQLiteOpenHelper {

    private static final String CREATE_ISSUE_TAGS_SQL = "create table issueTags(issueTagID integer primary key," +
            "issueTagName text not null," +
            "createDate text not null);" +
            "CREATE UNIQUE INDEX idxUniqueIssueTagName ON issueTags(issueTagName COLLATE nocase);";


    private static final String CREATE_ISSUES_SQL = "create table issues (issuesID integer primary key, " +
            "issueName string not null," +
            "FOREIGN KEY(issueTagId) REFERENCES issueTags(issueTagID)," +
            "createDate text not null);" +
            "CREATE UNIQUE INDEX idxUniqueIssueName ON issues(issueName COLLATE nocase);";

    public LoggrrrDatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public void createTestData() {
        getWritableDatabase().execSQL("insert into issueTags VALUES('tagName1', '2013-12-11');");
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        Log.i("BLARG: ", "creating data");
        getWritableDatabase().execSQL(CREATE_ISSUE_TAGS_SQL);
        getWritableDatabase().execSQL(CREATE_ISSUES_SQL);
        createTestData();
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i2) {
        //TODO: database upgrade.
    }
}
