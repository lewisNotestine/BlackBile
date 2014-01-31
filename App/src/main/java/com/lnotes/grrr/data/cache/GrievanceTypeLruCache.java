package com.lnotes.grrr.data.cache;

import android.support.v4.util.LruCache;

import com.lnotes.grrr.data.model.GrievanceType;

/**
 * Created by LN_1 on 12/24/13.
 */
public class GrievanceTypeLruCache extends LruCache<Integer, GrievanceType> {

    private static final int GRIEVANCE_TYPE_CACHE_SIZE = 2 * (1024 * 1024);

    public GrievanceTypeLruCache() {
        super(GRIEVANCE_TYPE_CACHE_SIZE);
    }
}
