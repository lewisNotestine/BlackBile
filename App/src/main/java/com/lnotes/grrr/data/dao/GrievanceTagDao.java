package com.lnotes.grrr.data.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.lnotes.grrr.data.definition.BlackBileDatabaseHelper;
import com.lnotes.grrr.data.definition.GrievanceTagsTable;
import com.lnotes.grrr.data.model.GrievanceTag;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * DAO object for {@link com.lnotes.grrr.data.model.GrievanceTag} objects.
 */
public class GrievanceTagDao extends Dao<GrievanceTag> {

    public GrievanceTagDao(SQLiteDatabase database, SQLiteOpenHelper helper) {
        super(database, helper);
    }

    @Override
    public long insert(final GrievanceTag insertItem) {
        final ContentValues contentValues = new ContentValues();
        contentValues.put(GrievanceTagsTable.COLUMN_GRIEVANCE_TAG_NAME, insertItem.getName());
        contentValues.put(GrievanceTagsTable.COLUMN_CREATE_DATE_TIME, BlackBileDatabaseHelper.DATE_FORMAT.format(insertItem.getDateCreated()));
        final long id = getDatabase().insertWithOnConflict(GrievanceTagsTable.TABLE_GRIEVANCE_TAGS, null, contentValues, SQLiteDatabase.CONFLICT_IGNORE);
        insertItem.setID(id);
        return id;
    }

    @Override
    public long delete(GrievanceTag deleteItem) {
        String[] deleteID = new String[]{String.format("%02d", deleteItem.getID())};
        return getDatabase().delete(GrievanceTagsTable.TABLE_GRIEVANCE_TAGS, BlackBileDatabaseHelper.COLUMN_ID + " = ?", deleteID);
    }

    @Override
    public List<GrievanceTag> selectAll() {
        try {
            final List<GrievanceTag> grievanceTagList = new ArrayList<GrievanceTag>();
            final Cursor grievanceTagCursor = getDatabase().rawQuery("SELECT * from " + GrievanceTagsTable.TABLE_GRIEVANCE_TAGS + ";", null);

            while (grievanceTagCursor.moveToNext()) {
                final int id = grievanceTagCursor.getInt(grievanceTagCursor.getColumnIndex(BlackBileDatabaseHelper.COLUMN_ID));
                final String tagName = grievanceTagCursor.getString(grievanceTagCursor.getColumnIndex(GrievanceTagsTable.COLUMN_GRIEVANCE_TAG_NAME));
                final String dateString = grievanceTagCursor.getString(grievanceTagCursor.getColumnIndex(GrievanceTagsTable.COLUMN_CREATE_DATE_TIME));
                final Date createDate = BlackBileDatabaseHelper.DATE_FORMAT.parse(dateString);

                GrievanceTag tag = new GrievanceTag(id, tagName, createDate);
                grievanceTagList.add(tag);
            }

            return grievanceTagList;
        } catch (ParseException ex) {
            throw new RuntimeException(ex);
        }
    }

    public Cursor getTagsCursor() {
        return getDatabase().rawQuery("select " +
                BlackBileDatabaseHelper.COLUMN_ID + ", " +
                GrievanceTagsTable.COLUMN_GRIEVANCE_TAG_NAME +
                " from " + GrievanceTagsTable.TABLE_GRIEVANCE_TAGS, null);
    }
}
