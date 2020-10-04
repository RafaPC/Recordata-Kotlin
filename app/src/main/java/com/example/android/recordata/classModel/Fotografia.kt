package com.example.android.recordata.classModel

import android.content.Context
import android.location.Address
import android.location.Geocoder

import java.io.IOException
import java.io.Serializable
import java.time.ZonedDateTime
import java.util.ArrayList
import java.util.Locale
import java.util.TimeZone

class Fotografia
//private String iso;

    (
    val descripcion: String,
    private val latitud: String,
    private val longitud: String,
    val objetivo: String,
    val apertura: String,
    val velocidad: String
) : Serializable {
    private val tiempo: ZonedDateTime

    val fecha: String
        get() = tiempo.dayOfMonth.toString() + "/" + tiempo.monthValue + "/" + tiempo.year

    init {
        //this.iso = iso;
        //Creo el tiempo de la zona donde se ejecuta el programa
        //Se saca la id de la zona con TimeZone y se crea el tiempo actual de esa zona con ZonedDateTime.now()
        this.tiempo = ZonedDateTime.now(TimeZone.getDefault().toZoneId())
    }

    fun getCiudad(context: Context): String? {
        val city: String?
        val geo = Geocoder(context, Locale.getDefault())
        var addresses: List<Address> = ArrayList()
        try {
            addresses =
                geo.getFromLocation(java.lang.Double.valueOf(this.latitud), java.lang.Double.valueOf(this.longitud), 1)
        } catch (ex: IOException) {
            return null
        }

        if (addresses.size > 0) {
            city = addresses[0].locality
        } else {
            city = null
        }
        return city
    }

    //@androidx.annotation.NonNull
    override fun toString(): String {
        return "Descripcion: " + this.descripcion + " - Localizacion: Latitud(" + this.latitud + "), Longitud(" + this.longitud + ") - Tiempo: " + this.fecha
    }
}
