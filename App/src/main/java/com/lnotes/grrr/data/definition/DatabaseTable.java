package com.lnotes.grrr.data.definition;

import android.database.sqlite.SQLiteDatabase;

/**
 * Interface defining the contract adhered to by all database tables.
 * //TODO: Should I make this into an abstract class instead, and store an instance therein?
 */
public interface DatabaseTable {

    /**
     * executes the table creation statements from the given database.
     * @param database the db to execute the statements.
     */
    public void onCreate(SQLiteDatabase database);

    /**
     * executes the drop and recreation of the table from the given database.
     * @param database the db to execute the drop/recreation.
     */
    public void onUpgrade(SQLiteDatabase database);
}
