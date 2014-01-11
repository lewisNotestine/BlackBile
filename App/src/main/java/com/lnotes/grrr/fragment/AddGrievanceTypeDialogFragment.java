package com.lnotes.grrr.fragment;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.MultiAutoCompleteTextView;

import com.lnotes.grrr.R;

/**
 * <p>
 * {@link android.support.v4.app.DialogFragment} with widgets for adding a
 * {@link com.lnotes.grrr.data.model.GrievanceType} to the database.
 * </p>
 * Created by LN_1 on 1/9/14.
 */
public class AddGrievanceTypeDialogFragment extends DialogFragment {

    private EditText mEnterNameEditText;
    private Button mConfirmButton;
    private Button mCancelButton;
    private MultiAutoCompleteTextView mInsertTagsTextView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_addgrievancetype_dialog, container, false);
        mInsertTagsTextView = (MultiAutoCompleteTextView) v.findViewById(R.id.dialog_addgrievancetype_tags_edittext);
        mCancelButton = (Button) v.findViewById(R.id.dialog_addgrievancetype_cancelbutton);

        mCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        return v;
    }

}
