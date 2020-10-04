package com.example.android.recordata.activities

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import com.example.android.recordata.JsonAdmin
import com.example.android.recordata.R
import com.example.android.recordata.classModel.Carrete
import com.example.android.recordata.customViews.CustomToast
import com.google.gson.Gson

import java.util.ArrayList

class RollCreation : AppCompatActivity() {
    private var arrayColor: Int = 0
    private var arrayBW: Int = 0
    private var botonColor: Button? = null
    private var botonBW: Button? = null
    private var lista: Spinner? = null
    private var tipoSeleccionado: Int = 0
    private val gson = Gson()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.roll_creation)

        //Cojo los datos de los carretes
        RollCreation.carretes = JsonAdmin.getCarretes(applicationContext)
        if (RollCreation.carretes.size == 0 || ultimoCarreteAcabado()) {
            //convertir de array a list
            this.arrayColor = R.array.rolls_color_array
            this.arrayBW = R.array.rolls_bw_array
            //Defino los botones
            this.botonColor = findViewById<View>(R.id.buttonColor) as Button
            this.botonBW = findViewById<View>(R.id.buttonBW) as Button

            //Construyo el dropdown de tipos de carretes (Spinner)
            this.lista = findViewById<View>(R.id.roll_spinner) as Spinner
        } else {
            //Si entra es que todavía quedan fotografias que hacer del ultimo carrete
            Log.w(TAG, RollCreation.carretes[0].toString())
            startActivity(Intent(this, RollManager::class.java))
        }
    }


    fun addCarrete(view: View) {
        //Cojo el item seleccionado
        val carreteSeleccionado = lista!!.selectedItem as String
        if (carreteSeleccionado == null) {
            CustomToast.mostrarToast(applicationContext, "Elige el carrete")
        } else {
            //Creo el carrete con el nombre y el tipo de carrete, así que ahora estará con el array de fotos vacio
            val carreteNuevo = Carrete(carreteSeleccionado, this.tipoSeleccionado)
            Log.d("Antes de añadir", RollCreation.carretes.toString())
            RollCreation.carretes.add(carreteNuevo)
            Log.d("Después de añadir", RollCreation.carretes.toString())

            Log.w(TAG, "Se ha añadido un carrete, ahora el array es asi -> " + RollCreation.carretes.toString())
            JsonAdmin.guardarCarretes(applicationContext, RollCreation.carretes)
            //Creo el intent y cambio de actividad
            val intent = Intent(this, RollManager::class.java)
            intent.putExtra("numCarrete", RollCreation.carretes.size - 1)
            startActivity(intent)
        }
    }

    fun botonColor(view: View) {
        this.botonColor!!.setBackgroundColor(Color.rgb(255, 0, 0))
        this.botonBW!!.setBackgroundColor(Color.argb(0, 0, 0, 0))
        this.tipoSeleccionado = 0
        cambiarRecurso(this.arrayColor)
    }

    fun botonBW(view: View) {
        this.botonBW!!.setBackgroundColor(Color.rgb(255, 0, 0))
        this.botonColor!!.setBackgroundColor(Color.argb(0, 0, 0, 0))
        this.tipoSeleccionado = 1
        cambiarRecurso(this.arrayBW)
    }

    private fun cambiarRecurso(idRecurso: Int) {
        // Create an ArrayAdapter using the string array and a default spinner layout
        val adapter = ArrayAdapter.createFromResource(
            this,
            idRecurso, android.R.layout.simple_spinner_item
        )
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        // Apply the adapter to the spinner
        this.lista!!.adapter = adapter
    }

    private fun ultimoCarreteAcabado(): Boolean {
        //Coge el ultimo carrete
        val ultimoCarrete = RollCreation.carretes[RollCreation.carretes.size - 1]
        return ultimoCarrete.isAcabado
    }

    public override fun onResume() {
        super.onResume()
        Log.d("RollList", JsonAdmin.getCarretes(applicationContext).toString())
        RollCreation.carretes = JsonAdmin.getCarretes(applicationContext)
    }

    companion object {

        protected val TAG = "Prueba"
        protected var carretes: ArrayList<Carrete> = arrayListOf()
    }

}
