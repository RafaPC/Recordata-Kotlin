package com.example.android.recordata.activities;


import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentActivity;
import com.example.android.recordata.JsonAdmin;
import com.example.android.recordata.R;
import com.example.android.recordata.classModel.Carrete;
import com.example.android.recordata.classModel.Fotografia;
import com.example.android.recordata.customViews.CustomToast;
import com.example.android.recordata.customViews.DescriptionDialogFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class RollManager extends FragmentActivity implements DescriptionDialogFragment.DescriptionDialogListener {

    private int indiceCarreteActual;
    private static ArrayList<Carrete> carretes = null;
    private Carrete carreteActual;
    private LinearLayout listaFotos;
    private static TextView description_view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.roll_manager);
        this.listaFotos = findViewById(R.id.list_photos);
        RollManager.description_view = findViewById(R.id.descripcion);
        this.indiceCarreteActual = getIntent().getIntExtra("numCarrete",0);
        //Si el carrete de esta clase es distinto al guardado en json
        ArrayList<Carrete> carretesJson = JsonAdmin.getCarretes(getApplicationContext());
        if(RollManager.carretes == null){
            RollManager.carretes = carretesJson;
        }else if(!RollManager.carretes.equals(carretesJson)){
            RollManager.carretes = carretesJson;
        }
        this.carreteActual = RollManager.carretes.get(this.indiceCarreteActual);

        //Escribo la descripcion
        ((TextView)findViewById(R.id.descripcion)).setText(this.carreteActual.getDescripcion());
        //Escribo como el título el nombre del carrete
        TextView titulo = (TextView) findViewById(R.id.carreteActual);
        titulo.setText(this.carreteActual.getNombre());

        if(this.carreteActual.isAcabado()){
            disableAddPhoto();
        }
    }

    private void disableAddPhoto(){
        FloatingActionButton button_addFoto = findViewById(R.id.boton_addFoto);
        button_addFoto.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#FF9B66")));
        button_addFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CustomToast.mostrarToast(getApplicationContext(),"El carrete está sacado");
            }
        });
    }
    private void inicio(){
        RollManager.carretes = JsonAdmin.getCarretes(getApplicationContext());
        this.indiceCarreteActual = getIntent().getIntExtra("numCarrete",this.indiceCarreteActual);

        this.carreteActual = RollManager.carretes.get(this.indiceCarreteActual);
        Fotografia primeraFoto = this.carreteActual.getFoto(0);
        //Escribo la fecha
        TextView fecha_primera_foto = findViewById(R.id.fecha_ini_foto);
        //Si la fecha de la primera foto no estaba escrita
        if(fecha_primera_foto.getText().length() == 0){
            if (primeraFoto != null) {
                fecha_primera_foto.setText(primeraFoto.getFecha());
            } else {
                //Supongo que si no coge la primera foto es porque no hay, así que es tontería mirar la segunda
                fecha_primera_foto.setText("No hay fotos");
            }
        }
        TextView fecha_ultima_foto = findViewById(R.id.fecha_ultima_foto);
        Fotografia ultimaFoto = this.carreteActual.getFoto(this.carreteActual.numFotos() - 1);
        if(ultimaFoto != null && ultimaFoto != primeraFoto){
            fecha_ultima_foto.setText(ultimaFoto.getFecha());
        }else{
            fecha_ultima_foto.setText("No hay fotos");
        }
        for(int i = listaFotos.getChildCount(); i < this.carreteActual.numFotos(); i++){
                //Meto el linear layout de "R.layour.photo_info" en la lista de fotos y luego le introduzco los datos
                getLayoutInflater().inflate(R.layout.photo_info, this.listaFotos);
                rellenarPhotoInfo(this.carreteActual.getFoto(i));
            }

    }

    private void rellenarPhotoInfo(Fotografia foto){
        LinearLayout photoInfo = (LinearLayout) this.listaFotos.getChildAt(this.listaFotos.getChildCount() - 1);
        TextView descripcion = (TextView) findViewById(R.id.foto_descripcion);
        descripcion.setText(foto.getDescripcion());
        descripcion.setId(0);
        TextView objetivo = (TextView) findViewById(R.id.foto_objetivo);
        objetivo.setText(foto.getObjetivo());
        objetivo.setId(0);
        TextView apertura = (TextView) findViewById(R.id.apertura);
        apertura.setText(foto.getApertura());
        apertura.setId(0);
        TextView velocidad = (TextView) findViewById(R.id.velocidad);
        velocidad.setText(foto.getVelocidad());
        velocidad.setId(0);
        TextView fecha = (TextView) findViewById(R.id.fecha);
        fecha.setText(foto.getFecha());
        fecha.setId(0);
        TextView ubicacion = (TextView) findViewById(R.id.ubicacion);
        ubicacion.setText(foto.getCiudad(getApplicationContext()));
        ubicacion.setId(0);

        ImageView iconAper = (ImageView) findViewById(R.id.icon_apertura);
        iconAper.setImageResource(R.drawable.ic_camera_black_30dp);
        iconAper.setId(0);
        ImageView iconVel = (ImageView) findViewById(R.id.icon_velocidad);
        iconVel.setImageResource(R.drawable.ic_timer_black_30dp);
        iconVel.setId(0);
        ImageView iconFecha = (ImageView) findViewById(R.id.icon_fecha);
        iconFecha.setImageResource(R.drawable.ic_today_black_30dp);
        iconFecha.setId(0);
        ImageView iconUbi = (ImageView) findViewById(R.id.icon_ubicacion);
        iconUbi.setImageResource(R.drawable.ic_map_black_30dp);
        iconUbi.setId(0);
    }

    public void addFoto(View view){
        Intent intent = new Intent(this, PhotoForm.class);
        startActivity(intent);
    }

    public void sacarCarrete(View view){
        this.carretes.get(this.carretes.size() - 1).sacarCarrete();
        JsonAdmin.guardarCarretes(getApplicationContext(), this.carretes);
        disableAddPhoto();
    }

    public void editarDescripcion(View view){
        //Mostrar el dialog con la cosa esa
        DialogFragment dialog = new DescriptionDialogFragment();
        dialog.show(getSupportFragmentManager(), "DescriptionDialogTag");
    }

    @Override
    public void onDialogPositiveClick(DialogFragment dialog) {
        String descripcion = DescriptionDialogFragment.descripcion.getText().toString();
        RollManager.carretes.get(RollManager.carretes.size() - 1).setDescripcion(descripcion);
        JsonAdmin.guardarCarretes(getApplicationContext(), RollManager.carretes);
        RollManager.description_view.setText(descripcion);
    }

    @Override
    public void onResume(){
        super.onResume();
        this.indiceCarreteActual = getIntent().getIntExtra("numCarrete",0);
        inicio();
    }
    /*class ScrollView extends FrameLayout {
        public ScrollView(@androidx.annotation.NonNull Context context) {
            super(context);
        }

        @Override
        protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
            heightMeasureSpec = MeasureSpec.makeMeasureSpec(300, MeasureSpec.AT_MOST);
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }
    }*/


}
