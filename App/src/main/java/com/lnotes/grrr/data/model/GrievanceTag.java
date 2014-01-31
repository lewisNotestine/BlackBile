package com.lnotes.grrr.data.model;

import java.util.Date;

/**
 * <p>
 * Represents an issue tag as defined in the database model.
 * </p>
 * Created by LN_1 on 12/11/13.
 */
public class GrievanceTag extends ModelType {

    private long mID;
    private String mName;
    private Date mDateCreated;

    /**
     * The only place where we should use this constructor is when we are creating a new GrievanceTag row
     * in the database (and we already have an ID-less object) and we don't know the ID yet.
     * Provided for convenience.
     */
    public GrievanceTag(final String tagName, final Date newDate) {
        mName = tagName;
        mDateCreated = newDate;
    }

    /**
     * GrievanceTag contructor for when we know the ID ahead of time.
     * This will be used when we are reading objects out of the database.
     */
    public GrievanceTag(final int id, final String tagName, final Date newDate) {
        this(tagName, newDate);
        setID(id);
    }

    /**
     * This constructor sets no ID and creates the date as the time-stamp.  This is best used
     * for when the user creates new tags.
     */
    public GrievanceTag(String tagName) {
        this(tagName, new Date());
    }

    public long getID() {
        return mID;
    }

    public void setID(final long id) {
        mID = id;
    }

    public String getName() {
        return mName;
    }

    public void setName(final String newName) {
        mName = newName;
    }

    public Date getDateCreated() {
        return mDateCreated;
    }

    public void setDateCreated(final Date dateCreated) {
        mDateCreated = dateCreated;
    }
}
