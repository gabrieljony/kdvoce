package br.com.kdvoce.application.applicationkdvoce.Dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.Toast;

import br.com.kdvoce.application.applicationkdvoce.R;


public class MyDialogFragment extends DialogFragment {

    public static MyDialogFragment newInstance() {
        return new MyDialogFragment();
    }

    private void showResult(Boolean v) {
        Context context = getContext();
        if (v) {
            Toast.makeText(context, "Enviando solicitação", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Cancelado", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(R.string.dialog_fire_missiles)
                .setPositiveButton(R.string.fire, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        showResult(true);
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        showResult(false);
                    }
                });
        // Create the AlertDialog object and return it
        return builder.create();
    }
}