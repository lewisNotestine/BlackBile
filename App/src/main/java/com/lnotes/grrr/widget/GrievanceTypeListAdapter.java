package com.lnotes.grrr.widget;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.lnotes.grrr.data.model.GrievanceType;
import com.lnotes.grrr.R;
import com.lnotes.grrr.fragment.LoggedGrievanceTokenDialogFragment;

import java.util.List;

/**
 * <p>
 * ListAdapter for a {@link com.lnotes.grrr.data.model.GrievanceType} list.
 * </p>
 * Created by LN_1 on 12/23/13.
 */
public class GrievanceTypeListAdapter extends ArrayAdapter<GrievanceType> {

    private int mLayoutResourceID;
    private LoggedGrievanceTokenDialogFragment mDialog;

    private static String sCreateDateString;
    private static final String DIALOG_TAG = "grievanceLoggedDialog";

    public GrievanceTypeListAdapter(Context context, int layoutResourceID, List<GrievanceType> grievanceTypeList) {
        super(context, layoutResourceID, grievanceTypeList);
        mLayoutResourceID = layoutResourceID;
        if (sCreateDateString == null) {
            sCreateDateString = getContext().getResources().getString(R.string.grievancetype_listitem_createdate);
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;

        if (convertView == null ){
            final LayoutInflater inflater = ((Activity) getContext()).getLayoutInflater();
            convertView = inflater.inflate(mLayoutResourceID, parent, false);

            viewHolder = new ViewHolder();
            if (convertView != null) {
                viewHolder.grievanceTypeName = (TextView) convertView.findViewById(R.id.listitem_grievance_type_grievance_typename);
                viewHolder.grievanceCountName = (TextView) convertView.findViewById(R.id.listitem_grievance_type_grievance_count);
                viewHolder.grievanceTypeCreateDate = (TextView) convertView.findViewById(R.id.listitem_grievance_type_createDate);
                viewHolder.logItButton = (Button) convertView.findViewById(R.id.listitem_grievance_type_log_grievance_button);
                //assign values based on current position.
                convertView.setTag(viewHolder);
            }
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        final GrievanceType item = getItem(position);
        viewHolder.grievanceTypeName.setText(item.getName());
        final String count = "99"; //TODO: this needs to have been created already.
        viewHolder.grievanceCountName.setText(count);
        final String createDateString = new StringBuilder()
                .append(sCreateDateString)
                .append(" ")
                .append(item.getCreateDate())
                .toString();
        viewHolder.grievanceTypeCreateDate.setText(createDateString);
        viewHolder.logItButton.setOnClickListener(new LogItButtonClickListener(item));

        return convertView;
    }


    private class ViewHolder {
        public TextView grievanceTypeName;
        public TextView grievanceCountName;
        public TextView grievanceTypeCreateDate;
        public Button logItButton;
    }


    /**
     * This button logs a grievance.
     */
    private class LogItButtonClickListener implements View.OnClickListener {

        private final GrievanceType mGrievanceType;

        public LogItButtonClickListener(GrievanceType grievanceType) {
            mGrievanceType = grievanceType;
        }

        @Override
        public void onClick(View view) {
            if (mDialog == null) {
                mDialog = new LoggedGrievanceTokenDialogFragment(mGrievanceType);
            }
            mDialog.show(((FragmentActivity) getContext()).getSupportFragmentManager(), DIALOG_TAG);
        }
    }
}
