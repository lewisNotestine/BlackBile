package com.lnotes.grrr.data.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.lnotes.grrr.data.definition.BlackBileDatabaseHelper;
import com.lnotes.grrr.data.definition.GrievanceTokensTable;
import com.lnotes.grrr.data.definition.GrievanceTokensTagsTable;
import com.lnotes.grrr.data.definition.GrievanceTypesTable;
import com.lnotes.grrr.data.model.GrievanceTag;
import com.lnotes.grrr.data.model.GrievanceToken;

import java.util.ArrayList;
import java.util.List;

/**
 * Dao for {@link com.lnotes.grrr.data.model.GrievanceToken} objects
 */
public class GrievanceTokenDao extends Dao<GrievanceToken> {

    public GrievanceTokenDao(SQLiteDatabase database, SQLiteOpenHelper helper) {
        super(database, helper);
    }

    @Override
    public long insert(GrievanceToken insertItem) {
        try {
            final ContentValues contentValues = new ContentValues();
            contentValues.put(GrievanceTokensTable.COLUMN_GRIEVANCE_TYPE_ID, insertItem.getTypeID());
            contentValues.put(GrievanceTokensTable.COLUMN_CREATE_DATE_TIME, BlackBileDatabaseHelper.DATE_FORMAT.format(insertItem.getCreateDate()));
            return getDatabase().insertWithOnConflict(GrievanceTokensTable.TABLE_GRIEVANCE_TOKENS, null, contentValues, SQLiteDatabase.CONFLICT_IGNORE);
        } catch (SQLException ex) {
            //TODO: Just throwing up stack for now, need to do something better.
            throw new RuntimeException(ex);
        }
    }

    @Override
    public long delete(GrievanceToken deleteItem) {
        String[] where = new String[]{String.format("%02d", deleteItem.getID())};
        //Delete Token/Tag mappings.
        getDatabase().delete(GrievanceTokensTagsTable.TABLE_GRIEVANCE_TOKENS_TAGS, GrievanceTokensTagsTable.COLUMN_GRIEVANCE_TOKEN_ID + " = ?", where);

        //Delete Token.
        return getDatabase().delete(GrievanceTokensTable.TABLE_GRIEVANCE_TOKENS, BlackBileDatabaseHelper.COLUMN_ID + " = ?" , where);
    }

    /**
     * <p>
     * gets all grievances, independent of tags.
     * </p>
     */
    @Override
    public List<GrievanceToken> selectAll() {
        Cursor cursor = getDatabase().rawQuery("select " +
                "gty." + BlackBileDatabaseHelper.COLUMN_ID + " AS grievanceTypeID, " +
                "gtk." + BlackBileDatabaseHelper.COLUMN_ID + " AS grievanceTokenID, " +
                "gty." + GrievanceTypesTable.COLUMN_GRIEVANCE_TYPE_NAME + ", " +
                "gtk." + GrievanceTokensTable.COLUMN_CREATE_DATE_TIME + " " +
                "FROM " + GrievanceTokensTable.TABLE_GRIEVANCE_TOKENS + " AS gtk " +
                "INNER JOIN " + GrievanceTypesTable.TABLE_GRIEVANCE_TYPES + " AS gty on gtk." + GrievanceTokensTable.COLUMN_GRIEVANCE_TYPE_ID + " = gty." + BlackBileDatabaseHelper.COLUMN_ID, null);
        List<GrievanceToken> outList = new ArrayList<GrievanceToken>();
        while (cursor.moveToNext()) {
            final int typeID = cursor.getInt(cursor.getColumnIndex("grievanceTypeID"));
            final int tokenID = cursor.getInt(cursor.getColumnIndex("grievanceTokenID"));
            final float magnitude = cursor.getFloat(cursor.getColumnIndex(GrievanceTokensTable.COLUMN_MAGNITUDE));
            String name = cursor.getString(cursor.getColumnIndex(GrievanceTypesTable.COLUMN_GRIEVANCE_TYPE_NAME));

            //TODO: We'll eventually mediate all calls to create grievanceToken object to the grievanceController.
            GrievanceToken grievanceToken = new GrievanceToken(typeID, tokenID, name, magnitude);
            outList.add(grievanceToken);
        }

        return outList;
    }

    public long insertGrievanceTokenTag(GrievanceTag tag, GrievanceToken token) {
        final ContentValues contentValues = new ContentValues();
        contentValues.put(GrievanceTokensTagsTable.COLUMN_GRIEVANCE_TAG_ID, tag.getID());
        contentValues.put(GrievanceTokensTagsTable.COLUMN_GRIEVANCE_TOKEN_ID, token.getID());
        return getDatabase().insertWithOnConflict(GrievanceTokensTagsTable.TABLE_GRIEVANCE_TOKENS_TAGS, null, contentValues, SQLiteDatabase.CONFLICT_IGNORE);
    }
}
