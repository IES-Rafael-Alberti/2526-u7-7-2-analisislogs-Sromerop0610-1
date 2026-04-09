package org.iesra

import java.io.File
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

fun main(args: Array<String>) {
    val contenido = File("sample_app.log").readLines()
    var mensajeInfo = 0
    var mensajeWarning = 0
    var mensajeError = 0

    var totalLineas = 0
    var lineasValidas = 0
    var lineasInvalidas = 0

    var primeraFecha: LocalDateTime? = null
    var ultimaFecha: LocalDateTime? = null

    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")

    val fechaInicio = if (args.size >= 1) LocalDateTime.parse(args[0], formatter) else null
    val fechaFin = if (args.size >= 2) LocalDateTime.parse(args[1], formatter) else null

    val regexFechaHora = Regex("""\[\d{4}-(0[1-9]|1[0-2])-(0[1-9]|[12]\d|3[01]) (?:[01]\d|2[0-3]):[0-5]\d:[0-5]\d\]""")

    for (linea in contenido) {
        totalLineas++

        val indiceCierre = linea.indexOf(']')
        if (indiceCierre != -1) {
            val fechaHora = linea.substring(0, indiceCierre + 1)
            val mensaje = linea.substring(indiceCierre + 1).trim()

            if (regexFechaHora.matches(fechaHora)) {

                val fechaTexto = fechaHora.removePrefix("[").removeSuffix("]")
                val fecha = LocalDateTime.parse(fechaTexto, formatter)

                val dentroRango =
                    (fechaInicio == null || !fecha.isBefore(fechaInicio)) && (fechaFin == null || fechaFin.isAfter(
                        fechaFin
                    ))

                if (dentroRango) {
                    lineasValidas++

                    when {
                        mensaje.contains("INFO") -> mensajeInfo++
                        mensaje.contains("WARNING") -> mensajeWarning++
                        mensaje.contains("ERROR") -> mensajeError++
                    }

                    if (primeraFecha == null || fecha.isBefore(primeraFecha)) {
                        primeraFecha = fecha
                    }

                    if (ultimaFecha == null || fecha.isAfter(ultimaFecha)) {
                        ultimaFecha = fecha
                    }
                }
            }else {
                lineasInvalidas++
                println("Formato inválido de fecha/hora en línea: $linea")
            }
        } else {
            lineasInvalidas++
        }
    }

    println("\n--- INFORME ---")
    println("Total líneas procesadas: $totalLineas")
    println("Total líneas válidas: $lineasValidas")
    println("Total líneas inválidas: $lineasInvalidas")
    println("Número de mensajes de INFO: $mensajeInfo")
    println("Número de mensajes de WARNING: $mensajeWarning")
    println("Número de mensajes de ERROR: $mensajeError")
    println("Primera fecha encontrada: ${primeraFecha ?: "N/A"}")
    println("Última fecha encontrada: ${ultimaFecha ?: "N/A"}")
}