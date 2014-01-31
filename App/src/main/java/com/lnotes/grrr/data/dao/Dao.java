package com.lnotes.grrr.data.dao;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.List;

/**
 * Abstract base class for all class-specific Dao implementations.
 * @param <Clazz> is the model type.
 */
public abstract class Dao<Clazz> {

    public static final long INVALID_ROW = -1l;


    //TODO: should these be cached here?
    private SQLiteDatabase mDatabase;
    private SQLiteOpenHelper mHelper;



    public Dao(SQLiteDatabase database, SQLiteOpenHelper helper ) {
        mDatabase = database;
        mHelper = helper;
    }

    /**
     * Inserts a single record for the table representing the class specified,
     * (plus any of its dependencies if applicable)
     * and returns the PK id of the item inserted
     */
    public abstract long insert(Clazz insertItem);

    /**
     * Deletes a single record for the table representing the class specified,
     * (plus any of its dependencies if applicable)
     * and returns the PK id of the item deleted.
     */
    public abstract long delete(Clazz deleteItem);

    /**
     * Get all of the elements in the table.
     * */
    public abstract List<Clazz> selectAll();

    protected SQLiteDatabase getDatabase() {
        return mDatabase;
    }

    protected SQLiteOpenHelper getHelper() {
        return mHelper;
    }
}
