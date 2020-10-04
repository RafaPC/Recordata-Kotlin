package com.example.android.recordata.customViews;

import android.content.Context;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.example.android.recordata.classModel.Fotografia;

public class PhotoInfo extends LinearLayout {

    public PhotoInfo(Context context, Fotografia foto, int numFoto) {
        super(context);
    }

    public static LinearLayout crearPhotoInfo(Context context, Fotografia foto, LinearLayout layout_root){
        LinearLayout layout_photoInfo = (LinearLayout) layout_root;
        Log.d("PhotoInfo", "Hijos del root " + layout_root.getChildCount());
        //Cojo el primer sub-layout
        LinearLayout layout_1 = (LinearLayout)layout_photoInfo.getChildAt(0);
        Log.d("PhotoInfo", "Hijos del primer subelemento " + layout_1.getChildCount());
        //Cojo el primer TextView
        TextView textView_descripcion = (TextView) layout_1.getChildAt(0);
        textView_descripcion.setText(foto.getDescripcion());
        //Cojo el segundo TextView
        TextView textView_objetivo = (TextView) layout_1.getChildAt(1);
        textView_objetivo.setText(foto.getObjetivo());

        //Cojo el primer sub-layout del segundo sub-layout
        //Primera fila con apertura y velocidad
        LinearLayout layout_2_1 = (LinearLayout) ((LinearLayout) layout_photoInfo.getChildAt(1)).getChildAt(0);
        TextView textView_apertura = (TextView)((LinearLayout) layout_2_1.getChildAt(0)).getChildAt(1);
        textView_apertura.setText(foto.getApertura());
        //Cojo el segundo sub-layout del segundo sub-layout
        TextView textView_velocidad = (TextView)((LinearLayout) layout_2_1.getChildAt(1)).getChildAt(1);
        textView_velocidad.setText(foto.getVelocidad());

        //Segunda fila con fecha y localizacion
        LinearLayout layout_2_2 = (LinearLayout) ((LinearLayout) layout_photoInfo.getChildAt(1)).getChildAt(1);
        TextView textView_fecha = (TextView)((LinearLayout) layout_2_2.getChildAt(0)).getChildAt(1);
        textView_fecha.setText(foto.getFecha());
        //Cojo el segundo sub-layout del segundo sub-layout
        TextView textView_localizacion = (TextView)((LinearLayout) layout_2_2.getChildAt(1)).getChildAt(1);
        String ciudad = foto.getCiudad(context);
        if(ciudad == null){
            ciudad = "Error";
        }
        textView_localizacion.setText(ciudad);

        return layout_photoInfo;
    }

    public static void crearPhotoInfo2(Fotografia foto, LinearLayout layout_photoInfo){

    }

}
