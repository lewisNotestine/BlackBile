package com.lnotes.grrr.widget;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.lnotes.grrr.data.model.GrievanceType;
import com.lnotes.grrr.R;

import java.util.List;

/**
 * <p>
 * ListAdapter for a {@link com.lnotes.grrr.data.model.GrievanceType} list.
 * </p>
 * Created by LN_1 on 12/23/13.
 */
public class GrievanceTypeListAdapter extends ArrayAdapter<GrievanceType> {

    private int mLayoutResourceID;
    private static String sCreateDateString;


    public GrievanceTypeListAdapter(Context context, int layoutResourceID, List<GrievanceType> grievanceTypeList) {
        super(context, layoutResourceID, grievanceTypeList);
        mLayoutResourceID = layoutResourceID;
        if (sCreateDateString == null) {
            sCreateDateString = getContext().getResources().getString(R.string.grievancetype_listitem_createdate);
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;

        if (convertView == null ){
            LayoutInflater inflater = ((Activity) getContext()).getLayoutInflater();
            convertView = inflater.inflate(mLayoutResourceID, parent, false);

            viewHolder = new ViewHolder();

            viewHolder.grievanceTypeName = (TextView) convertView.findViewById(R.id.listitem_grievance_type_grievance_typename);
            viewHolder.grievanceCountName = (TextView) convertView.findViewById(R.id.listitem_grievance_type_grievance_count);
            viewHolder.grievanceTypeCreateDate = (TextView) convertView.findViewById(R.id.listitem_grievance_type_createDate);

            //assign values based on current position.
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        GrievanceType item = getItem(position);
        viewHolder.grievanceTypeName.setText(item.getGrievanceTypeName());
        String count = String.format("%02d", item.getCountInstances());
        viewHolder.grievanceCountName.setText(count);
        String createDateString = new StringBuilder()
                .append(sCreateDateString)
                .append(" ")
                .append(item.getCreateDate())
                .toString();
        viewHolder.grievanceTypeCreateDate.setText(createDateString);

        return convertView;
    }


    private class ViewHolder {
        public TextView grievanceTypeName;
        public TextView grievanceCountName;
        public TextView grievanceTypeCreateDate;
    }
}
