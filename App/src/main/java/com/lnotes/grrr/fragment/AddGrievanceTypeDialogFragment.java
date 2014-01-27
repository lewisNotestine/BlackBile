package com.lnotes.grrr.fragment;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.widget.CursorAdapter;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.MultiAutoCompleteTextView;

import com.lnotes.grrr.R;
import com.lnotes.grrr.data.definition.GrrrDB;
import com.lnotes.grrr.data.model.GrievanceTag;
import com.lnotes.grrr.data.model.GrievanceType;

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

    private static final String[] TAG_DISPLAY_SOURCE_COLUMNS = { "grievanceTagName" };
    private static final int[] TAG_TO_RESOURCE_IDS = { android.R.layout.simple_list_item_1 };
    private static final int[] TAG_AUTOCOMPLETE_LAYOUT = { android.R.id.text1 };
    private static final int TAG_DISPLAY_COLUMN_INDEX = 1;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        final View v = inflater.inflate(R.layout.fragment_addgrievancetype_dialog, container, false);
        if (v != null) {
            mEnterNameEditText = (EditText) v.findViewById(R.id.dialog_addgrievancetype_addnametext);

            mInsertTagsTextView = (MultiAutoCompleteTextView) v.findViewById(R.id.dialog_addgrievancetype_tags_edittext);
            setDataForAutoCompletion();

            mCancelButton = (Button) v.findViewById(R.id.dialog_addgrievancetype_cancelbutton);
            mCancelButton.setOnClickListener(new CancelListener());

            mConfirmButton = (Button) v.findViewById(R.id.dialog_addgrievancetype_confirmbutton);
            mConfirmButton.setOnClickListener(new ConfirmListener());
        }
        return v;
    }

    /**
     * <p>
     * Sets up a data adapter for {@link #mInsertTagsTextView}.
     * </p>
     */
    private void setDataForAutoCompletion() {

        final CursorAdapter tagsCursorAdapter = new TagsCursorAdapter(getActivity(), 0); //TODO: Set flags/managers as necessary.

        mInsertTagsTextView.setAdapter(tagsCursorAdapter);
        mInsertTagsTextView.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());
    }


    private void updateListAdapter() {
        mInsertTagsTextView.invalidate();
    }

    /**
     * <p>
     * Extends {@link android.support.v4.widget.SimpleCursorAdapter} to take a column index for
     * the purpose of rendering a string.
     * </p>
     */
    private class TagsCursorAdapter extends SimpleCursorAdapter {

        public TagsCursorAdapter(Context context, int flags) {
            super(context, TAG_TO_RESOURCE_IDS[0],
                    GrrrDB.getInstance().getTagsCursor(),
                    TAG_DISPLAY_SOURCE_COLUMNS,
                    TAG_AUTOCOMPLETE_LAYOUT,
                    flags);
            setStringConversionColumn(TAG_DISPLAY_COLUMN_INDEX);
        }
    }

    /**
     * <p>
     * Listener for {@link #mConfirmButton}.
     * </p>
     */
    private class ConfirmListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            if (mEnterNameEditText.getText() != null && mEnterNameEditText.getText().toString().length() > 0) {
                final GrievanceType newGrievanceType = new GrievanceType(mEnterNameEditText.getText().toString());

                final Cursor cursor = ((Cursor) mInsertTagsTextView.getAdapter().getItem(0));
                if (cursor != null && cursor.getCount() > 0) {
                    while (cursor.moveToNext()) {
                        final String tagName = cursor.getString(cursor.getColumnIndex("grievanceTagName")); //TODO: named constants for column names.
                        GrievanceTag newGrievanceTag = new GrievanceTag(tagName);
                        newGrievanceType.addGrievanceTag(newGrievanceTag);
                    }
                }

                GrrrDB.getInstance().insertGrievanceType(newGrievanceType);
                updateListAdapter();
                dismiss();
            }
        }
    }

    /**
     * Listener for {@link #mCancelButton}.
     **/
    private class CancelListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            dismiss();
        }
    }
}
