package com.lnotes.grrr.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.lnotes.grrr.R;
import com.lnotes.grrr.controller.GrievanceController;
import com.lnotes.grrr.data.model.Grievance;
import com.lnotes.grrr.data.model.GrievanceType;
import com.lnotes.grrr.widget.GrievanceTypeListAdapter;

import java.util.List;

/**
 * <p>
 * Fragment for viewing a collection of {@link com.lnotes.grrr.data.model.GrievanceType} objects,
 * intended for the purpose of displaying them in a list and offering them up to the user so that
 * the user may log a {@link com.lnotes.grrr.data.model.Grievance}.
 * </p>
 * Created by LN_1 on 12/23/13.
 */
public class GrievanceTypeListFragment extends Fragment {

    private ListView mGrievanceTypesListView;
    private GrievanceTypeListAdapter mGrievanceTypesListAdapter;
    private List<GrievanceType> mGrievanceTypes;


    @Override
    public void onCreate(Bundle savedState) {
        super.onCreate(savedState);
        mGrievanceTypes = GrievanceController.getCurrentGrievanceTypes();
        mGrievanceTypesListAdapter = new GrievanceTypeListAdapter(getActivity(), R.layout.listitem_grievance_type, mGrievanceTypes);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstance) {
        return inflater.inflate(R.layout.fragment_grievance_list, container, false);
    }

    @Override
    public void onResume() {
        super.onResume();
        mGrievanceTypesListView = (ListView) getView().findViewById(R.id.grievance_list_listview);
        mGrievanceTypesListView.setAdapter(mGrievanceTypesListAdapter);
    }
}
