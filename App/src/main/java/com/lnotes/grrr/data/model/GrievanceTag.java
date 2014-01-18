package com.lnotes.grrr.data.model;

import java.util.Date;

/**
 * <p>
 * Represents an issue tag as defined in the database model.
 * </p>
 * Created by LN_1 on 12/11/13.
 */
public class GrievanceTag {

    private String mName;
    private Date mDateCreated;

    public GrievanceTag(String tagName, Date newDate) {
        mName = tagName;
        mDateCreated = newDate;
    }

    public GrievanceTag(String tagName) {
        this(tagName, new Date());
    }

    public String getName() {
        return mName;
    }

    public void setName(String newName) {
        mName = newName;
    }

    public Date getDateCreated() {
        return mDateCreated;
    }
}
