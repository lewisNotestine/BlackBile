package com.lnotes.grrr.data.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.lnotes.grrr.data.model.GrievanceToken;
import com.lnotes.grrr.data.model.GrievanceTag;
import com.lnotes.grrr.data.model.GrievanceType;

import java.util.List;

/**
 * <p>
 * Responsible for wrapping a SQLiteDatabase and handling querying and CRUD operations on it.
 * </p>
 * Created by LN_1 on 12/11/13.
 */
public class DaoController {

    private static DaoController sInstance;

    private final GrievanceTagDao mGrievanceTagDao;
    private final GrievanceTypeDao mGrievanceTypeDao;
    private final GrievanceTokenDao mGrievanceTokenDao;

    public static void createInstance(SQLiteOpenHelper helper) {
        if (sInstance == null) {
            sInstance = new DaoController(helper);
        }
    }

    public static DaoController getInstance() {
        if (sInstance == null) {
            throw new NullPointerException("You should have called createInstance first!");
        }
        return sInstance;
    }

    private DaoController(SQLiteOpenHelper dbHelper) {
        SQLiteDatabase sqliteDB = dbHelper.getWritableDatabase();
        mGrievanceTagDao = new GrievanceTagDao(sqliteDB, dbHelper);
        mGrievanceTypeDao = new GrievanceTypeDao(sqliteDB, dbHelper);
        mGrievanceTokenDao = new GrievanceTokenDao(sqliteDB, dbHelper);
    }

    public List<GrievanceToken> selectAllGrievances() {
        return mGrievanceTokenDao.selectAll();
    }

    /**
     * <p>
     * Return all the distinct GrievanceToken Types from the database.
     * </p>
     *
     * @return A list of all the GrievanceTypes in the DB.
     */
    public List<GrievanceType> selectAllGrievanceTypes() {
        return mGrievanceTypeDao.selectAll();
    }

    /**
     * <p>
     * Return the count of grievances of a particular type in the database
     * </p>
     */
    public int selectCountOfGrievancesByType(int typeID) {
        return mGrievanceTypeDao.selectCountOfGrievancesByType(typeID);

    }

    /**
     * <p>
     * Gets a {@link android.database.Cursor} representing {@link com.lnotes.grrr.data.model.GrievanceTag} objects
     * to be used by client widgets,
     * as in {@link com.lnotes.grrr.fragment.AddGrievanceTypeDialogFragment#setDataForAutoCompletion()}.
     * </p>
     *
     * @return the cursor that reprsents all {@link com.lnotes.grrr.data.model.GrievanceTag} objects in the db.
     */
    public Cursor getTagsCursor() {
        return mGrievanceTagDao.getTagsCursor();
    }

    public long insertGrievanceType(GrievanceType newGrievanceType) {
        return mGrievanceTypeDao.insert(newGrievanceType);
    }

    public long insertGrievanceTag(GrievanceTag newGrievanceTag) {
        return mGrievanceTagDao.insert(newGrievanceTag);
    }

    public long insertGrievanceToken(GrievanceToken grievanceToken) {
        return mGrievanceTokenDao.insert(grievanceToken);
    }

    /**Handled by TypeDao.
     * adds a tag to an existing type.
     * */
    public long insertGrievanceTypeTag(GrievanceTag tag, GrievanceType type) {
        return mGrievanceTypeDao.insertGrievanceTypeTag(tag, type);
    }

    /**Handled by TokenDao
     * adds a tag to an existing token.
     * */
    public long insertGrievanceTokenTag(GrievanceTag tag, GrievanceToken token) {
        return mGrievanceTokenDao.insertGrievanceTokenTag(tag, token);
    }

}
