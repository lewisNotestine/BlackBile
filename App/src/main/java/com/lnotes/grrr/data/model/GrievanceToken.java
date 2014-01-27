package com.lnotes.grrr.data.model;

import android.nfc.Tag;

import java.util.Set;

/**
 * <p>
 * Represents an instance of the GrievanceToken class. Stores a GrievanceType and
 * </p>
 * Created by LN_1 on 12/11/13.
 */
public class GrievanceToken {

    private int mTypeID;
    private int mTokenID;
    private String mName;
    private GrievanceType mGrievanceType;

    /** Tags to be added to the default given by {@link #mGrievanceType}*/
    private Set<Tag> mAddedTags;

    /** Tags to be subtracted from teh default given by {@link #mGrievanceType}*/
    private Set<Tag> mSubtractedTags;

    public GrievanceToken(int typeID, int tokenID, String name) {
        mTypeID = typeID;
        mTokenID = tokenID;
        mName = name;
        //TODO: mGrievanceType = [get it from some LRU cache or something.]
    }

    @Override
    public boolean equals(Object otherIssue) {
        return otherIssue instanceof GrievanceToken && ((GrievanceToken) otherIssue).mTypeID == mTypeID;
    }

    @Override
    public int hashCode() {
        int hash = 1;
        hash = hash * 17 + mTypeID;
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

}
