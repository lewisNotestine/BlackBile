package com.lnotes.grrr.data.model;

import com.lnotes.grrr.controller.GrievanceController;
import com.lnotes.grrr.data.GrrrDatabaseHelper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * <p>
 * Class for encapsulating all data that represent a type of Grievance.  Also serves as bookkeeping for aggregate information about {@link Grievance}
 * objects of this grievanceType
 * </p>
 * Created by LN_1 on 12/17/13.
 */
public class GrievanceType {


    private int mID;
    private String mTypeName;
    private Date mCreateDateTime;

    /**
     * Tracks the number of {@link com.lnotes.grrr.data.model.Grievance} objects of this type constructed
     */
    private int mCountInstances;
    private Date mMostRecentLogged;

    public GrievanceType(int id, String typeName, String createDateString) {
        try {
            mID = id;
            mTypeName = typeName;
            mCreateDateTime = new SimpleDateFormat(GrrrDatabaseHelper.DATE_FORMAT, Locale.ENGLISH).parse(createDateString);

            //TODO: should this really be happening? Should probably be managed using an aggregate SQL function>
            mCountInstances = GrievanceController.getInstance().getCountOfGrievancesOfType(mID);
        } catch (ParseException ex) {
            mCreateDateTime = new Date();
        }
    }

    @Override
    public boolean equals(Object otherGrievanceType) {
        return otherGrievanceType instanceof GrievanceType && ((GrievanceType) otherGrievanceType).mID == mID;
    }

    @Override
    public int hashCode() {
        int hash = 1;
        hash = hash * 31 + mID;
        return hash;
    }

    @Override
    public String toString() {
        return new StringBuilder().append(" ID: ").append(mID)
                .append(" Name: ").append(mTypeName)
                .append(" CreateDate: ").append(mCreateDateTime)
                .append('\n').toString();
    }

    public int getID() {
        return mID;
    }

    public String getGrievanceTypeName() {
        return mTypeName;
    }

    public int  getCountInstances() {
        return mCountInstances;
    }
}
