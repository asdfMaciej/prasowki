package io.maciej01.prasowki.activity;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.os.Bundle;

/**
 * Created by Maciej on 2017-05-15.
 */

public class SpinnerDialog extends DialogFragment {

    public SpinnerDialog() {}

    @Override
    public Dialog onCreateDialog(final Bundle savedInstanceState) {

        ProgressDialog _dialog = new ProgressDialog(getActivity());
        this.setStyle(STYLE_NO_TITLE, getTheme());
        _dialog.setMessage("Ładowanie ostatnich prasówek...");
        _dialog.setCancelable(false);
        return _dialog;
    }
}
