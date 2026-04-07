package org.iesra

import java.io.File

fun main() {
    val contenido = File("sample_app.log").readLines()
    var mensajeInfo = 0
    var mensajeWarning = 0
    var mensajeError = 0

    val regexFechaHora = Regex("""\[\d{4}-(0[1-9]|1[0-2])-(0[1-9]|[12]\d|3[01]) (?:[01]\d|2[0-3]):[0-5]\d:[0-5]\d\]""")

    for (linea in contenido) {
        val indiceCierre = linea.indexOf(']')
        if (indiceCierre != -1) {
            val fechaHora = linea.substring(0, indiceCierre + 1)
            val mensaje = linea.substring(indiceCierre + 1).trim()

            if (regexFechaHora.matches(fechaHora)) {
                when {
                    mensaje.contains("INFO") -> mensajeInfo++
                    mensaje.contains("WARNING") -> mensajeWarning++
                    mensaje.contains("ERROR") -> mensajeError++
                }
            } else {
                println("Formato inválido de fecha/hora en línea: $linea")
            }
        }
    }

    println("Mensaje Info: $mensajeInfo, Error: $mensajeError, Warning: $mensajeWarning")
}