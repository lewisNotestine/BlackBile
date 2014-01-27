package com.lnotes.grrr.controller;

import com.lnotes.grrr.data.cache.GrievanceLruCache;
import com.lnotes.grrr.data.cache.GrievanceTagLruCache;
import com.lnotes.grrr.data.cache.GrievanceTypeLruCache;
import com.lnotes.grrr.data.definition.GrrrDB;
import com.lnotes.grrr.data.model.Grievance;
import com.lnotes.grrr.data.model.GrievanceTag;
import com.lnotes.grrr.data.model.GrievanceType;

import java.util.HashMap;
import java.util.List;

/**
 * Class for performing bookkeeping operations on grievances, grievance types, and tags (e.g., indexing tags), etc.
 * Created by LN_1 on 12/11/13.
 */
public class GrievanceController {
    private static GrievanceController sInstance;

    private GrievanceTypeLruCache mGrievanceTypeCache;
    private GrievanceLruCache mGrievanceCache;
    private GrievanceTagLruCache mGrievanceTagCache;

    private HashMap<Integer, GrievanceType> mGrievanceTypes;
    private HashMap<Integer, Grievance> mGrievances;
    private HashMap<Integer, GrievanceTag> mGrievanceTags;

    protected GrievanceController() {
        mGrievanceTypeCache = new GrievanceTypeLruCache();
        mGrievanceCache = new GrievanceLruCache();
        mGrievanceTagCache = new GrievanceTagLruCache();

        mGrievanceTypes = new HashMap<>();
        mGrievances = new HashMap<>();
        mGrievanceTags = new HashMap<>();
    }

    public static GrievanceController getInstance() {
        if (sInstance == null) {
            sInstance = new GrievanceController();
        }
        return sInstance;
    }

    public static List<GrievanceType> getCurrentGrievanceTypes() {
        return GrrrDB.getInstance().selectAllGrievanceTypes();
    }

    public int getCountOfGrievancesOfType(int typeID) {
        return GrrrDB.getInstance().selectCountOfGrievancesByType(typeID);
    }
}
