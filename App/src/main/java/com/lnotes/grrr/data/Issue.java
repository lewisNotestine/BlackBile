package com.lnotes.grrr.data;

import android.nfc.Tag;

import java.util.List;

/**
 * Created by LN_1 on 12/11/13.
 */
public class Issue {

    private int mID;
    private String mName;
    private int mTagId;
    private List<Tag> mTags;

    public Issue(int id, String name, int tagId) {
        mID = id;
        mName = name;
        mTagId = tagId;
    }

    @Override
    public boolean equals(Object otherIssue) {
        return otherIssue instanceof Issue && ((Issue) otherIssue).mID == mID;
    }

    @Override
    public int hashCode() {
        int hash = 1;
        hash = hash * 17 + mID;
        return hash;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        return builder.append("ID:").append(mID)
                .append("Name: ").append(mName)
                .append("Tag: ").append(mTagId)
                .append('\n')
                .toString();
    }

}
