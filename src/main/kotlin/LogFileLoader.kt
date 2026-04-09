package org.iesra

import java.io.File
import java.io.IOException

class LogFileLoader {
    fun loadLines(ruta: String): List<String> {
        return try {
            File(ruta).readLines()
        } catch (e: IOException) {
            throw IOException("Error: fichero no encontrado o no accesible")
        }
    }
}