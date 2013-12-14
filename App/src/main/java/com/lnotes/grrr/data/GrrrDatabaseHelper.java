package com.lnotes.grrr.data;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.lnotes.grrr.GrrrApplication;

/**
 * Created by LN_1 on 12/11/13.
 * <p>
 * Class for Data definition language for the Logger database.
 * </p>
 */
public class GrrrDatabaseHelper extends SQLiteOpenHelper {

    //TODO: issues and tags: should they be many-to-many?

    private static final String CREATE_ISSUE_TAGS_SQL = "create table issueTags(issueTagID integer primary key autoincrement," +
            "issueTagName text not null," +
            "createDate text not null);" +
            "CREATE UNIQUE INDEX idxUniqueIssueTagName ON issueTags(issueTagName COLLATE nocase);";

    private static final String CREATE_ISSUES_SQL = "create table issues (issuesID integer primary key autoincrement, " +
            "issueName string not null," +
            "issueTagID integer, " +
            "createDate text not null," +
            "FOREIGN KEY(issueTagID) REFERENCES issueTags(issueTagID));" +
            "CREATE UNIQUE INDEX idxUniqueIssueName ON issues(issueName COLLATE nocase);";

    private static GrrrDatabaseHelper sInstance;

    public static GrrrDatabaseHelper getInstance() {
        if (sInstance == null) {
            sInstance = new GrrrDatabaseHelper();
        }
        return sInstance;
    }

    private GrrrDatabaseHelper() {
        super(GrrrApplication.getLoggrrrApplicationContext(), "testDBName", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqliteDatabase) {
        Log.i("BLARG: ", "creating data");
        if (sqliteDatabase != null) {
            sqliteDatabase.execSQL(CREATE_ISSUE_TAGS_SQL);
            sqliteDatabase.execSQL(CREATE_ISSUES_SQL);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i2) {
        //TODO: database upgrade.
    }
}
