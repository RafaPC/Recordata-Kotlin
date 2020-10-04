package com.example.android.recordata.classModel

import java.io.Serializable
import java.util.ArrayList

class Carrete(val nombre: String, tipoNum: Int) : Serializable {

    private val fotografias = ArrayList<Fotografia>()
    private val tipo: String
    var descripcion: String? = null
        get() = if (field != null) {
            field
        } else {
            "Descripción."
        }
    var isAcabado: Boolean = false
        private set

    init {
        val tipo: String
        if (tipoNum == 0) {
            tipo = COLOR
        } else {
            tipo = BW
        }
        this.tipo = tipo
    }

    fun addFoto(foto: Fotografia) {
        this.fotografias.add(foto)
    }

    fun getFoto(i: Int): Fotografia? {
        try {
            return fotografias[i]
        } catch (ex: IndexOutOfBoundsException) {
            //Si salta esto es que el indice se pasaba por debajo de 0 o por encima del indice de la lista
            return null
        }

    }

    fun numFotos(): Int {
        return this.fotografias.size
    }

    fun sacarCarrete() {
        this.isAcabado = true
    }

    fun fechaInicio(): String {
        try {
            return this.fotografias[0].fecha
        } catch (ex: IndexOutOfBoundsException) {
            return "No hay fotografías"
        }

    }

    fun fechaFinal(): String {
        try {
            return this.fotografias[this.fotografias.size - 1].fecha
        } catch (ex: IndexOutOfBoundsException) {
            return "No hay fotografías"
        }

    }

    //@androidx.annotation.NonNull
    override fun toString(): String {
        var contenido = "Carrete: " + this.nombre + "\nTipo: " + this.tipo + "Fotos: "
        for (foto in this.fotografias) {
            contenido += "\n" + foto.toString()
        }
        return contenido
    }

    companion object {

        private val COLOR = "COLOR"
        private val BW = "B&W"
    }
}
