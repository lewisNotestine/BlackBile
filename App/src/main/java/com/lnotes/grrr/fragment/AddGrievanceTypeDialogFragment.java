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
import com.lnotes.grrr.data.GrrrDB;

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
        setDataForAutoCompletion();

        mCancelButton = (Button) v.findViewById(R.id.dialog_addgrievancetype_cancelbutton);

        mCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        return v;
    }

    /**
     * <p>
     * Sets up a data adapter for {@link #mInsertTagsTextView}.
     * </p>
     */
    private void setDataForAutoCompletion() {

        Cursor tagsCursor = GrrrDB.getInstance().getTagsCursor();

        //TODO: consider factoring out cursor adapter into its own class or inner class?
        String[] columns = { "grievanceTagName" };
        int[] resourceIds = { android.R.layout.simple_list_item_1 };
        CursorAdapter tagsCursorAdapter = new TagsCursorAdapter(getActivity(),
                resourceIds[0],
                tagsCursor,
                columns,
                new int[] { android.R.id.text1 },
                0,
                tagsCursor.getColumnIndex(columns[0]));

        mInsertTagsTextView.setAdapter(tagsCursorAdapter);
        mInsertTagsTextView.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());
}


    /**
     * <p>
     * Extends {@link android.support.v4.widget.SimpleCursorAdapter} to take a column index for
     * the purpose of rendering a string.
     * //TODO: consider internalizing the DB ops in the caller into this class.
     * </p>
     */
    private class TagsCursorAdapter extends SimpleCursorAdapter {

        public TagsCursorAdapter(Context context, int resourceId, Cursor cursor, String[] columns, int[] to, int flags, int stringColumnIndex) {
            super(context, resourceId, cursor, columns, to, flags);
            setStringConversionColumn(stringColumnIndex);
        }
    }
}
