package com.lnotes.grrr.data.model;

import android.nfc.Tag;

import java.util.Date;
import java.util.Set;

/**
 * <p>
 * Represents an instance of the GrievanceToken class. Stores a GrievanceType and
 * </p>
 * Created by LN_1 on 12/11/13.
 */
public class GrievanceToken extends ModelType {

    private final long mTypeID;
    private int mTokenID;
    private final Date mCreateDate;
    private String mName; //TODO: should this be "notes" or "description" instead?
    private GrievanceType mGrievanceType; //TODO: should this be here?
    private float mMagnitude;

    /** Tags to be added to the default given by {@link #mGrievanceType}*/
    private Set<Tag> mAddedTags;

    /** Tags to be subtracted from teh default given by {@link #mGrievanceType}*/
    private Set<Tag> mSubtractedTags;

    public GrievanceToken(final int typeID, final int tokenID, final String name, final float magnitude) {
        mTypeID = typeID;
        mTokenID = tokenID;
        mCreateDate = new Date(); //TODO: don't just new up a date.
        mName = name;
        mMagnitude = magnitude;
        //TODO: mGrievanceType = [get it from some LRU cache or something.]
    }


    /**
     * This is the constructor we use when the user creates a new Token.
     * The id is set when it is written to the db.
     */
    public GrievanceToken(final GrievanceType type, final float magnitude) {
        mTypeID = type.getID();
        mCreateDate = new Date();
        mGrievanceType = type;
        mMagnitude = magnitude;
    }

    @Override
    public boolean equals(Object otherIssue) {
        return otherIssue instanceof GrievanceToken && ((GrievanceToken) otherIssue).mTypeID == mTypeID;
    }

    @Override
    public int hashCode() {
        int hash = 1;
        hash = hash * 17 + (int)mTypeID;
        return hash;
    }

    @Override
    public String toString() {
        return new StringBuilder().append("Token ID:").append(mTokenID)
                .append("Type ID:").append(mTypeID)
                .append("Name: ").append(mName)
                .append('\n')
                .toString();
    }

    public Date getCreateDate() {
        return mCreateDate;
    }

    public long getTypeID() {
        return mTypeID;
    }

    public float getMagnitude() {
        return mMagnitude;
    }

    public void setMagnitude(final float magnitude) {
        mMagnitude = magnitude;
    }


}
