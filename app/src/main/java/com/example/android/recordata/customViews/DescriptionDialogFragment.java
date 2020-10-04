package com.example.android.recordata.customViews;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import androidx.fragment.app.DialogFragment;
import com.example.android.recordata.R;

public class DescriptionDialogFragment extends DialogFragment {
    public static EditText descripcion;
    public interface DescriptionDialogListener{
        public void onDialogPositiveClick(DialogFragment dialog);
    }

    DescriptionDialogListener myListener;

    @Override
    public void onAttach(Activity activity){
        super.onAttach(activity);
        try{
            myListener = (DescriptionDialogListener) activity;
        }catch(ClassCastException ex){
            throw new ClassCastException(activity.toString() + " must implement DescriptionDialogListener");
        }
    }

    public Dialog onCreateDialog(Bundle savedInstanceState){
        AlertDialog.Builder builder= new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_change_desc, null);
        DescriptionDialogFragment.descripcion = view.findViewById(R.id.dialog_descripcion);
        builder.setView(view);

        //builder.setView(inflater.inflate(R.layout.dialog_change_desc, null));
        //Boton de cambiar
        builder.setPositiveButton("Cambiar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                myListener.onDialogPositiveClick(DescriptionDialogFragment.this);
            }
        })
                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        DescriptionDialogFragment.this.getDialog().cancel();
                    }
                });
        return builder.create();
    }
}
