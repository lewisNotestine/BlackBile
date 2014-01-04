package com.lnotes.grrr.widget;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.lnotes.grrr.data.model.GrievanceType;

import java.util.List;

/**
 * <p></p>
 * Created by LN_1 on 12/23/13.
 */
public class GrievanceTypeListAdapter extends ArrayAdapter<GrievanceType> {

    //TODO: isn't this already taken care of somewhere?
    private int mLayoutResourceID;


    public GrievanceTypeListAdapter(Context context, int layoutResourceID, List<GrievanceType> grievanceTypeList) {
        super(context, layoutResourceID, grievanceTypeList);
        mLayoutResourceID = layoutResourceID;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;

        if (convertView == null ){
            LayoutInflater inflater = ((Activity) getContext()).getLayoutInflater();
            convertView = inflater.inflate(mLayoutResourceID, parent, false);

            viewHolder = new ViewHolder();

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        Log.d("BLARG", String.format("%02d", position));
        //assign values based on current position.
        GrievanceType item = getItem(position);

        viewHolder.grievanceTypeName.setText(item.getGrievanceTypeName());
        String count = String.format("%02d", item.getCountInstances());
        viewHolder.grievanceCountName.setText(count);
        return convertView;
    }


    private class ViewHolder {
        public TextView grievanceTypeName;
        public TextView grievanceCountName;

        public ViewHolder() {
            grievanceTypeName = new TextView(getContext());
            grievanceCountName = new TextView(getContext());
        }
    }
}
