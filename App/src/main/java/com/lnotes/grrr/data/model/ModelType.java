package com.lnotes.grrr.data.model;

/**
 * Abstraction for any class that has a data model.
 * Basically includes methods for getting/setting from a database.
 */
public abstract class ModelType {

    private long mID;

    /**Getter for {@link #mID}*/
    public long getID() {
        return mID;
    }

    /**Setter for {@link #mID}*/
    protected void setID(final long id) {
        mID = id;
    }



}
