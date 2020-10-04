package com.example.android.recordata.activities

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TextView
import androidx.core.app.ActivityCompat
import com.example.android.recordata.JsonAdmin
import com.example.android.recordata.R
import com.example.android.recordata.classModel.Carrete
import com.example.android.recordata.classModel.Fotografia
import com.example.android.recordata.customViews.CustomToast
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.gms.tasks.Task
import com.google.android.material.textfield.TextInputEditText

import java.util.ArrayList

class PhotoForm : Activity() {

    private var fusedLocationClient: FusedLocationProviderClient? = null
    private var interfazSpinner: SpinnerActivity? = null
    private var carretes: ArrayList<Carrete>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.photo_creation)
        this.carretes = JsonAdmin.getCarretes(applicationContext)
        this.fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        this.interfazSpinner = SpinnerActivity(this, applicationContext)
        Log.d("PhotoForm", "onCreate")
        //Lo selecciono automáticamente a  1/60
        val spinner_velocidad = findViewById<View>(R.id.spinner_input_velocidad) as Spinner
        spinner_velocidad.setSelection(7)
    }

    private fun addFoto(latitud: String, longitud: String) {
        //Aqui se cogen los valores de cada campo
        val inputDescripcion = findViewById<View>(R.id.input_descripcion) as TextInputEditText
        val descripcion = inputDescripcion.text!!.toString()
        val inputObjetivo = findViewById<View>(R.id.spinner_input_objetivo) as Spinner
        val objetivo = inputObjetivo.selectedItem as String
        val inputApertura = findViewById<View>(R.id.spinner_input_apertura) as Spinner
        val apertura = inputApertura.selectedItem as String
        val inputVelocidad = findViewById<View>(R.id.spinner_input_velocidad) as Spinner
        val velocidad = inputVelocidad.selectedItem as String
        Log.d("PhotoForm", "Se cogen todos los campos")
        inputDescripcion.setText("")
        inputDescripcion.clearFocus()


        //El iso se coge del carrete
        val nuevaFoto = Fotografia(descripcion, latitud, longitud, objetivo, apertura, velocidad)
        //Guardo la nueva foto en el ultimo carrete
        this.carretes!![this.carretes!!.size - 1].addFoto(nuevaFoto)
        Log.d("PhotoForm", "Se añade la foto al ultimo carrete del array")
        JsonAdmin.guardarCarretes(applicationContext, this.carretes)
        Log.d("PhotoForm", "Se guarda el array de carretes en el sharedPreferences")
        CustomToast.mostrarToast(applicationContext, "Imagen creada correctamente")

        //Aqui podría poner un poop up con un snackbar
    }


    fun pedirLocation(view: View) {
        val descripcion = findViewById<View>(R.id.input_descripcion) as TextView
        if (descripcion.text.length == 0) {
            CustomToast.mostrarToast(applicationContext, "Introduce una descripción")
        } else {

            if (ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                // TOD: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.

                /*if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                mMap.setMyLocationEnabled(true);
            }*/
                CustomToast.mostrarToast(applicationContext, "No tienes el permiso de localización")
                return
            } else {
                this.fusedLocationClient!!.lastLocation
                    .addOnSuccessListener{location : Location? ->
                            if (location != null) {
                                addFoto(location.latitude.toString(), location.longitude.toString())
                            } else {
                                CustomToast.mostrarToast(applicationContext, "Error en la ubicación")
                            }
                    }

                this.fusedLocationClient!!.lastLocation
                    .addOnFailureListener{e: Exception? ->
                        CustomToast.mostrarToast(applicationContext, "Error en la ubicación")
                }

                this.fusedLocationClient!!.lastLocation
                    .addOnCompleteListener{task: Task<Location> ->
                            Log.d("PhotoForm", "Si entra aqui es que al menos se ha llamado")

                    }
            }
        }
    }


    inner class SpinnerActivity(private val activity: Activity, private val context: Context) : Activity(),
        AdapterView.OnItemSelectedListener {

        init {
            val spinnerObjetivos = this.activity.findViewById<View>(R.id.spinner_input_objetivo) as Spinner
            spinnerObjetivos.onItemSelectedListener = this
        }

        override fun onItemSelected(parent: AdapterView<*>, view: View, pos: Int, id: Long) {

            Log.w(RollCreation.TAG, "Se ha seleccionado algo")
            // An item was selected. You can retrieve the selected item using
            // parent.getItemAtPosition(pos)
            var idRecurso = 0
            //Segun que objetivo se haya clickado, se carga en el spinner de aperturas unas aperturas u otras
            when (pos) {
                0 -> idRecurso = R.array.lens_50mm14
                1 -> idRecurso = R.array.lens_35mm28
                2 -> idRecurso = R.array.lens_75mm4
                3 -> idRecurso = R.array.lens_135mm2
            }
            /*
            //Aqui intentaba crear arrays de aperturas con los mismos nombres que los objetivos a los que señalaban
            String recurso = "R.array.";
            TextView text = (TextView) parent.getSelectedItem();
            int idRecurso;
            try {
                Class res = R.drawable.class;
                Field field = res.getField("drawableName");
                idRecurso = field.getInt(null);
            }
            catch (Exception e) {
                Log.e("MyTag", "Failure to get drawable id.", e);
            }*/

            if (idRecurso != 0) {
                // Create an ArrayAdapter using the string array and a default spinner layout
                val adapter = ArrayAdapter.createFromResource(
                    this.context,
                    idRecurso, android.R.layout.simple_spinner_item
                )
                // Specify the layout to use when the list of choices appears
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                // Apply the adapter to the spinner
                val spinnerApertura = this.activity.findViewById<View>(R.id.spinner_input_apertura) as Spinner
                spinnerApertura.adapter = adapter
            } else {
                Log.d(
                    RollCreation.TAG,
                    "No se ha podido cambiar el array de aperturas porque no se ha encontrado el item seleccionado"
                )
            }

        }

        override fun onNothingSelected(parent: AdapterView<*>) {
            // Another interface callback
        }

    }

}
