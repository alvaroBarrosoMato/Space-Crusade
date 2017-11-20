package alvaro.daniel.space_crusade;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

/**
 * Created by Dany on 19/11/2017.
 */

public class ExitAppFragmentDialog extends DialogFragment {
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        //get the layout inflater
        LayoutInflater inflater= getActivity().getLayoutInflater();

        //inflate and set de layout for the dialog
        //Pass null as the parent view because is going in the dialog layout
        View dialog = inflater.inflate(R.layout.fragment_exit_app_dialog, null);
        //add actiob buttons
            //.setTitle(R.string.exitDialogTitle)
            //.setMessage(R.string.exitDialogDescription)
            /*.setPositiveButton(R.string.dialogAffirmative, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    getActivity().finish();
                }
            })
            .setNegativeButton(R.string.dialogNegative, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    // User cancelled the dialog
                }
            });*/

        Button dialogOk = (Button)dialog.findViewById(R.id.dialogButtonOk);
        Button dialogNo = (Button)dialog.findViewById(R.id.dialogButtonNo);
        dialogOk.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                getActivity().finish();
                System.exit(0);
            }
        });

        dialogNo.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
               dismiss();
            }
        });

        builder.setView(dialog);
        // Create the AlertDialog object and return it
        return builder.create();
    }
}
