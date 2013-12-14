package com.lnotes.grrr.data;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * <p>
 * Responsible for wrapping a SQLiteDatabase and handling querying and CRUD operations on it.
 * </p>
 * Created by LN_1 on 12/11/13.
 * //TODO: consider making a singleton.
 */
public class GrrrDB {

    private SQLiteDatabase mSQLiteDB;
    private static GrrrDB sInstance;

    public static void createInstance(SQLiteOpenHelper helper) {
        if (sInstance == null) {
            sInstance = new GrrrDB(helper);
        }
    }

    public static GrrrDB getInstance() {
        if (sInstance == null) {
            throw new NullPointerException("You should have called createInstance first!");
        }
        return sInstance;
    }

    private GrrrDB(SQLiteOpenHelper dbHelper) {
        mSQLiteDB = dbHelper.getWritableDatabase();
    }

    //TODO: implement CRUD operations.

    /**
     * <p>
     * This is just a temp class for testing database ops.  It inserts a test dataset into the db.
     * </p>
     */
    public void createTestData() {
        mSQLiteDB.execSQL("insert into issueTags(issueTagName, createDate) VALUES('tagName1', '2013-12-11');");
        mSQLiteDB.execSQL("insert into issues(issueName, issueTagID, createDate) VALUES('issueName1', 1, " + Calendar.getInstance().get(Calendar.SECOND) + ")");
        mSQLiteDB.execSQL("insert into issues(issueName, issueTagID, createDate) VALUES('issueName2', null, " + Calendar.getInstance().get(Calendar.SECOND) + ")");
    }

    /**
     * <p>
     * gets all issues, independent of tags.
     * TODO: handle the tags.
     * </p>
     */
    public List<Issue> selectAllIssues() {
        String[] columns = {"issuesID", "issueName", "issueTagID", "createDate" };
        Cursor cursor = mSQLiteDB.query(false, "issues", columns, null, null, null, null, null, null);
        List<Issue> outList = new ArrayList<Issue>();
        while (cursor.moveToNext()) {
            int ID = cursor.getInt(cursor.getColumnIndex("issuesID"));
            String name = cursor.getString(cursor.getColumnIndex("issueName"));
            int tagID = cursor.getInt(cursor.getColumnIndex("issueTagID"));

            //We'll eventually mediate all calls to create Issue object to the IssueController.
            Issue issue = new Issue(ID, name, tagID);
            outList.add(issue);
        }

        return outList;
    }

}
