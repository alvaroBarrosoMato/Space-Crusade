package alvaro.daniel.space_crusade;

import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
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
        BaseActivity.setFont(dialog);

        Button dialogOk = (Button)dialog.findViewById(R.id.dialogButtonOk);
        Button dialogNo = (Button)dialog.findViewById(R.id.dialogButtonNo);
        dialogOk.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Animation pressed_anim = AnimationUtils.loadAnimation(getContext(), R.anim.button_pressed_anim);
                v.startAnimation(pressed_anim);
                getActivity().finish();
                System.exit(0);
            }
        });

        dialogNo.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                Animation pressed_anim = AnimationUtils.loadAnimation(getContext(), R.anim.button_pressed_anim);
                v.startAnimation(pressed_anim);
                dismiss();
            }
        });

        builder.setView(dialog);
        // Create the AlertDialog object and return it
        return builder.create();
    }

    @Override
    public void onStart(){
        super.onStart();
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    }

}
