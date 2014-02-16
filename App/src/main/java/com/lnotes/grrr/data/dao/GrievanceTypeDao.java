package com.lnotes.grrr.data.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.lnotes.grrr.data.definition.BlackBileDatabaseHelper;
import com.lnotes.grrr.data.definition.GrievanceTagsTable;
import com.lnotes.grrr.data.definition.GrievanceTokensTable;
import com.lnotes.grrr.data.definition.GrievanceTokensTagsTable;
import com.lnotes.grrr.data.definition.GrievanceTypesTable;
import com.lnotes.grrr.data.definition.GrievanceTypesTagsTable;
import com.lnotes.grrr.data.model.GrievanceTag;
import com.lnotes.grrr.data.model.GrievanceType;

import java.util.ArrayList;
import java.util.List;

/**
 * Dao for {@link com.lnotes.grrr.data.model.GrievanceType} objects
 */
public class GrievanceTypeDao extends Dao<GrievanceType> {

    public GrievanceTypeDao(SQLiteDatabase database, SQLiteOpenHelper helper) {
        super(database, helper);
    }

    @Override
    public long insert(GrievanceType insertItem) {
        try {
            ContentValues typeValues = new ContentValues();
            typeValues.put(GrievanceTypesTable.COLUMN_GRIEVANCE_TYPE_NAME, insertItem.getName());
            typeValues.put(GrievanceTypesTable.COLUMN_CREATE_DATE_TIME, BlackBileDatabaseHelper.DATE_FORMAT.format(insertItem.getCreateDate()));
            final long typeRowId = getDatabase().insertWithOnConflict(GrievanceTypesTable.TABLE_GRIEVANCE_TYPES, null, typeValues, SQLiteDatabase.CONFLICT_IGNORE);

            for (GrievanceTag tag : insertItem.getGrievanceTags()) {
                ContentValues tagValues = new ContentValues();
                tagValues.put(GrievanceTagsTable.COLUMN_GRIEVANCE_TAG_NAME, tag.getName());
                tagValues.put(GrievanceTagsTable.COLUMN_CREATE_DATE_TIME, BlackBileDatabaseHelper.DATE_FORMAT.format(tag.getDateCreated()));
                final long tagRowId = getDatabase().insertWithOnConflict(GrievanceTagsTable.TABLE_GRIEVANCE_TAGS, null, tagValues, SQLiteDatabase.CONFLICT_IGNORE);

                //This check condition shouldn't obtain, but if we ever use an algorithm other than CONFLICT_IGNORE,
                // then we'll have to do a check here (can sometimes return -1 otherwise).
                if (tagRowId != -1) {
                    tag.setID(tagRowId);

                    ContentValues tagTypeValues = new ContentValues();
                    tagTypeValues.put(GrievanceTypesTagsTable.COLUMN_GRIEVANCE_TYPE_ID, typeRowId);
                    tagTypeValues.put(GrievanceTypesTagsTable.COLUMN_GRIEVANCE_TAG_ID, tagRowId);
                    getDatabase().insertWithOnConflict(GrievanceTypesTagsTable.TABLE_GRIEVANCE_TYPES_TAGS, null, tagTypeValues, SQLiteDatabase.CONFLICT_IGNORE);
                }
            }

            return typeRowId;
        } catch (SQLException ex) {
            //TODO: better exception handling, instead of using this to do flow control here.
            return Dao.INVALID_ROW;
        }
    }

    @Override
    public long delete(GrievanceType deleteItem) {
        try {
            //First get all Tokens that have this type.
            final String[] typeID = new String[]{String.format("%02d", deleteItem.getID())};
            final Cursor tokensCursor = getDatabase().rawQuery("SELECT * from " + GrievanceTokensTable.TABLE_GRIEVANCE_TOKENS +
                    " WHERE " + GrievanceTokensTable.COLUMN_GRIEVANCE_TYPE_ID + " = ?", typeID);

            //TODO: replace this block with a call to TokenDao or similar?  right now we are duplicating some code for deleting a token.
            //CONSIDER: using a cache to delete from in-memory Token objects if available. What if not available?
            while (tokensCursor.moveToNext()) {
                //Delete Token/Tag mappings.
                final String[] tokenID = new String[]{String.format("%02d", tokensCursor.getInt(tokensCursor.getColumnIndex(BlackBileDatabaseHelper.COLUMN_ID)))};
                getDatabase().delete(GrievanceTokensTagsTable.TABLE_GRIEVANCE_TOKENS_TAGS, GrievanceTokensTagsTable.COLUMN_GRIEVANCE_TOKEN_ID + " = ?", tokenID);

                //Delete Token
                getDatabase().delete(GrievanceTokensTable.TABLE_GRIEVANCE_TOKENS, BlackBileDatabaseHelper.COLUMN_ID + " = ?", tokenID);
            }

            //Delete Type/Tag mappings.
            getDatabase().delete(GrievanceTypesTagsTable.TABLE_GRIEVANCE_TYPES_TAGS, GrievanceTypesTagsTable.COLUMN_GRIEVANCE_TYPE_ID, typeID);

            //Delete Type.
            return getDatabase().delete(GrievanceTypesTable.TABLE_GRIEVANCE_TYPES, BlackBileDatabaseHelper.COLUMN_ID, typeID);
        } catch (SQLException ex) {
            //TODO: better exception handling.
            return Dao.INVALID_ROW;
        }
    }

    @Override
    public List<GrievanceType> selectAll() {
        List<GrievanceType> tempList = new ArrayList<>();
        Cursor cursor = getDatabase().rawQuery("select " +
                "gtyp." + BlackBileDatabaseHelper.COLUMN_ID + ", " +
                "gtyp." + GrievanceTypesTable.COLUMN_GRIEVANCE_TYPE_NAME + ", " +
                "gtyp." + GrievanceTypesTable.COLUMN_CREATE_DATE_TIME + ", " +
                "COUNT(gtk." + BlackBileDatabaseHelper.COLUMN_ID + ") AS countInstances " +
                "FROM " + GrievanceTypesTable.TABLE_GRIEVANCE_TYPES + " AS gtyp " +
                "LEFT OUTER JOIN " + GrievanceTokensTable.TABLE_GRIEVANCE_TOKENS + " AS gtk on gtyp." + BlackBileDatabaseHelper.COLUMN_ID + " = gtk." + GrievanceTokensTable.COLUMN_GRIEVANCE_TYPE_ID + " " +
                "GROUP BY gtyp." + BlackBileDatabaseHelper.COLUMN_ID + "; ", null);
        while (cursor.moveToNext()) {
            String name = cursor.getString(cursor.getColumnIndex(GrievanceTypesTable.COLUMN_GRIEVANCE_TYPE_NAME));
            String dateString = cursor.getString(cursor.getColumnIndex(GrievanceTypesTable.COLUMN_CREATE_DATE_TIME));
            int countInstances = cursor.getInt(cursor.getColumnIndex("countInstances"));
            GrievanceType grievanceType = new GrievanceType(name, dateString, countInstances);
            //TODO: cache the grievanceType here.
            tempList.add(grievanceType);
        }

        return tempList;
    }

    public int selectCountOfGrievancesByType(int typeID) {
        int count = 0;
        String[] args = new String[]{String.format("%02d", typeID)};
        Cursor cursor = getDatabase().rawQuery("SELECT COUNT(*) AS count from " +
                GrievanceTypesTable.TABLE_GRIEVANCE_TYPES + " gtyp " +
                "INNER JOIN " + GrievanceTokensTable.TABLE_GRIEVANCE_TOKENS + " g on gtyp." + BlackBileDatabaseHelper.COLUMN_ID + " = g." + GrievanceTokensTable.COLUMN_GRIEVANCE_TYPE_ID +
                "WHERE gtyp." + BlackBileDatabaseHelper.COLUMN_ID + " = ?", args);
        if (cursor.moveToFirst()) {
            count = cursor.getInt(cursor.getColumnIndex("count"));
        }

        return count;

    }

    public long insertGrievanceTypeTag(GrievanceTag tag, GrievanceType type) {
        final ContentValues contentValues = new ContentValues();
        contentValues.put(GrievanceTypesTagsTable.COLUMN_GRIEVANCE_TAG_ID, tag.getID());
        contentValues.put(GrievanceTypesTagsTable.COLUMN_GRIEVANCE_TYPE_ID, type.getID());
        return getDatabase().insertWithOnConflict(GrievanceTypesTagsTable.TABLE_GRIEVANCE_TYPES_TAGS, null, contentValues, SQLiteDatabase.CONFLICT_IGNORE);
    }

}
