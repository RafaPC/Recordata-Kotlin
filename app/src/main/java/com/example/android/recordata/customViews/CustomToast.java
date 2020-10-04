package com.example.android.recordata.customViews;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import com.example.android.recordata.R;

public class CustomToast {

    public static void mostrarToast(Context context, String text) {
        android.widget.Toast toast = android.widget.Toast.makeText(context, text, android.widget.Toast.LENGTH_LONG);
        toast.setGravity(Gravity.BOTTOM, 0, 0);
        View toastView = toast.getView();
        //toastView.setBackgroundColor(Color.rgb(150,0,0));
        toastView.setBackgroundResource((int) R.layout.toast_message);
        toast.show();
    }
}
