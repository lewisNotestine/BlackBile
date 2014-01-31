package com.lnotes.grrr.data.cache;

import android.support.v4.util.LruCache;

import com.lnotes.grrr.data.model.GrievanceTag;

/**
 * Created by LN_1 on 12/24/13.
 */
public class GrievanceTagLruCache extends LruCache<Integer, GrievanceTag>{

    private static final int GRIEVANCE_TAG_CACHE_SIZE = 2 * 1024 * 1024;

    public GrievanceTagLruCache() {
        super(GRIEVANCE_TAG_CACHE_SIZE);
    }

    /**
     * Hits the DB in the instance of a cache miss.
     */
    @Override
    public GrievanceTag create(Integer key) {
        //TODO: not return null.
        return null;
    }
}
