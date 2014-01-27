package com.lnotes.grrr.data.cache;

import android.support.v4.util.LruCache;

import com.lnotes.grrr.data.model.Grievance;

/**
 * Created by LN_1 on 12/24/13.
 */
public class GrievanceLruCache extends LruCache<Integer, Grievance> {

    private static final int GRIEVANCE_CACHE_SIZE = 2 * 1024 * 1024;

    public GrievanceLruCache() {
        super(GRIEVANCE_CACHE_SIZE);
    }

    /**
     * <p>
     * Hits the database to get the right {@link com.lnotes.grrr.data.model.Grievance} object.
     * </p>
     */
    @Override
    protected Grievance create(Integer key) {
        //TODO: hit the DB to get the right Grievance.
        return null;
    }
}
