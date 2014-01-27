package com.lnotes.grrr.fragment;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.lnotes.grrr.R;
import com.lnotes.grrr.data.dao.DaoController;
import com.lnotes.grrr.data.model.GrievanceType;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

/**
 * A simple {@link android.support.v4.app.DialogFragment} that waits for a user to undo logging a
 * GrievanceToken, and, after an interval, dismisses itself.
 */
public class LoggedGrievanceTokenDialogFragment extends DialogFragment {

    private static final long WAIT_FIVE_SECONDS = TimeUnit.SECONDS.toMillis(5);
    private Timer mTimer;
    private GrievanceType mGrievanceType;

    public LoggedGrievanceTokenDialogFragment(GrievanceType grievanceType) {
        super();
        mGrievanceType = grievanceType;
        mTimer = new Timer();
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
            final Button undoButton = (Button) view.findViewById(R.id.dialog_addgrievancetoken_undoadd);
            undoButton.setOnClickListener(new UndoListener());
        }
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        mTimer = new Timer();
        mTimer.schedule(new AutoDismissTimerTask(), WAIT_FIVE_SECONDS);
    }


    /**
     * Listener whose job is to cancel the db insert operation.
     */
    private class UndoListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            mTimer.cancel();
            mTimer.purge();
            dismiss();
        }
    }

    /**
     * Performs the DB insert and dismisses the fragment.
     */
    private class AutoDismissTimerTask extends TimerTask {
        @Override
        public void run() {
            DaoController.getInstance().insertGrievanceToken(mGrievanceType);
            dismiss();
        }
    }
}
