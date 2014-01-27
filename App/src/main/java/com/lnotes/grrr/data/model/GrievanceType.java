package com.lnotes.grrr.data.model;

import com.lnotes.grrr.data.definition.BlackBileDatabaseHelper;

import java.text.ParseException;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * <p>
 * Class for encapsulating all data that represent a type of Grievance.  Also serves as bookkeeping for aggregate information about {@link Grievance}
 * objects of this grievanceType
 * </p>
 * Created by LN_1 on 12/17/13.
 */
public class GrievanceType {

    private String mTypeName;
    private Date mCreateDateTime;
    private Set<GrievanceTag> mGrievanceTags;

    /**
     * Tracks the number of {@link com.lnotes.grrr.data.model.Grievance} objects of this type constructed
     */
    private int mCountInstances;

    /**
     * This ctor is intended for use when reading from DB.
     * //TODO: consider a factory for creating GrievanceTypes instead of this...
     */
    public GrievanceType(String typeName, String createDateString, int countInstances) {
        try {
            mTypeName = typeName;
            mCreateDateTime = BlackBileDatabaseHelper.DATE_FORMAT.parse(createDateString);
            mGrievanceTags = new HashSet<>();
            mCountInstances = countInstances;
        } catch (ParseException ex) {
            throw new RuntimeException(ex);
        }
    }

    /**
     * This ctor is only intended for use when creating an altogether new GrievanceType
     * (rather than reading one from the database).
     */
    public GrievanceType(String typeName) {
       this(typeName, BlackBileDatabaseHelper.DATE_FORMAT.format(new Date()), 0);
    }

    @Override
    public boolean equals(Object otherGrievanceType) {
        return otherGrievanceType instanceof GrievanceType && ((GrievanceType) otherGrievanceType).mTypeName == mTypeName;
    }

    @Override
    public int hashCode() {
        int hash = 1;
        hash = hash * 31 + mTypeName.hashCode();
        return hash;
    }

    @Override
    public String toString() {
        return new StringBuilder()
                .append(" Name: ").append(mTypeName)
                .append(" CreateDate: ").append(mCreateDateTime)
                .append('\n').toString();
    }

    public String getName() {
        return mTypeName;
    }

    public Date getCreateDate() {
        return mCreateDateTime;
    }

    public Set<GrievanceTag> getGrievanceTags() {
        return mGrievanceTags;
    }

    /**
     * <p>
     * adds a tag to the set of tags.
     * @return true if the add operation succeeds, false otherwise.
     * </p>
     */
    public boolean addGrievanceTag(GrievanceTag newTag) {
        return mGrievanceTags.add(newTag);
    }

}
