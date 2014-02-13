package com.lnotes.grrr.fragment;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.lnotes.grrr.R;
import com.lnotes.grrr.data.dao.DaoController;
import com.lnotes.grrr.data.model.GrievanceToken;
import com.lnotes.grrr.data.model.GrievanceType;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

/**
 * A simple {@link android.support.v4.app.DialogFragment} that waits for a user to undo logging a
 * GrievanceToken, and, after an interval, dismisses itself.
 */
public class LoggedGrievanceTokenDialogFragment extends DialogFragment {

    private GrievanceType mGrievanceType;
    private EditText mMagnitudeEditText;

    public LoggedGrievanceTokenDialogFragment(GrievanceType grievanceType) {
        super();
        mGrievanceType = grievanceType;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //TODO: implement custom layout, style etc. this is here as a reminder.
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_addgrievancetoken_dialog, container, false);
        if (view != null) {
            final Button doneButton = (Button) view.findViewById(R.id.dialog_addgrievancetoken_done);
            mMagnitudeEditText = (EditText) view.findViewById(R.id.dialog_addgrievancetoken_magnitudetext);
            doneButton.setOnClickListener(new DoneListener());
        }
        return view;
    }


    /**
     * Validates the data, performs the DB insert and dismisses the dialog.
     */
    private class DoneListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {

            //Check if we have valid data, and if so insert it.
            if (mMagnitudeEditText.getText() != null && mMagnitudeEditText.getText().length() <= 0) {
                raiseSetMagnitudeToast();
            } else {
                final float magnitude = Float.valueOf(mMagnitudeEditText.getText().toString());
                final GrievanceToken newToken = new GrievanceToken(mGrievanceType, magnitude);
                DaoController.getInstance().insertGrievanceToken(newToken);
                dismiss();
            }
        }
    }


    /**Raise a toast to remind the user they need to set the magnitude.*/
    private void raiseSetMagnitudeToast() {
        Toast.makeText(getActivity(), R.string.set_magnitude_toast, Toast.LENGTH_SHORT).show();
    }
}
