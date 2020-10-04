package com.example.android.recordata.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.android.recordata.JsonAdmin
import com.example.android.recordata.R
import com.example.android.recordata.customViews.CustomToast

import java.util.ArrayList

class MainMenu : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_menu)
        //JsonAdmin.guardarCarretes(getApplicationContext(), new ArrayList<Carrete>());
    }

    fun clickOnFotos(view: View) {
        val carretes = JsonAdmin.getCarretes(applicationContext)
        if (carretes.size == 0) {
            CustomToast.mostrarToast(applicationContext, "Primero crea un carrete")
        } else {
            //CustomToast.mostrarToast(getApplicationContext(), "Esto es un toast");
            val intent = Intent(this, RollManager::class.java)
            intent.putExtra("numCarrete", JsonAdmin.getCarretes(applicationContext).size - 1)
            startActivity(intent)
        }
    }

    fun clickOnCarretes(view: View) {
        val intent = Intent(this, RollList::class.java)
        startActivity(intent)
    }

    fun clickOnMapa(view: View) {

    }

    fun clickOnAjustes(view: View) {

    }

    public override fun onResume() {
        super.onResume()
    }
}
