package org.iesra

import java.io.File
import java.io.IOException
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException

fun main(args: Array<String>) {

    if (args.isEmpty()){
        println("Uso: <fichero_log> [fecha_inicio] [fecha_fin] [fichero_salida]")
        return
    }

    val rutaFichero = args[0]
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")

    val fechaInicio: LocalDateTime? = try {
        if (args.size >= 2) LocalDateTime.parse(args[1], formatter) else null
    } catch (e: DateTimeParseException) {
        println("Formato de fecha_inicio incorrecto")
        return
    }
    val fechaFin: LocalDateTime? = try {
        if (args.size >= 3) LocalDateTime.parse(args[2], formatter) else null
    } catch (e: DateTimeParseException) {
        println("Formato de fecha_fin incorrecto")
        return
    }

    val ficheroSalida = if (args.size >= 4) args[3] else null

    val contenido: List<String>
    try{
        contenido = File(rutaFichero).readLines()
    } catch (e: IOException) {
        println("Error: fichero no encontrado o no accesible")
        return
    }

    var mensajeInfo = 0
    var mensajeWarning = 0
    var mensajeError = 0

    var totalLineas = 0
    var lineasValidas = 0
    var lineasInvalidas = 0

    var primeraFecha: LocalDateTime? = null
    var ultimaFecha: LocalDateTime? = null

    val regexFechaHora = Regex("""\[\d{4}-(0[1-9]|1[0-2])-(0[1-9]|[12]\d|3[01]) (?:[01]\d|2[0-3]):[0-5]\d:[0-5]\d\]""")

    for (linea in contenido) {
        totalLineas++

        val indiceCierre = linea.indexOf(']')
        if (indiceCierre == -1) {
            lineasInvalidas++
            println("Línea mal formada: $linea")
        } else {

            val fechaHora = linea.substring(0, indiceCierre + 1)
            val mensaje = linea.substring(indiceCierre + 1).trim()

            if (!regexFechaHora.matches(fechaHora)) {
                lineasInvalidas++
                println("Formato inválido de fecha/hora: $linea")
            } else {
                val fechaTexto = fechaHora.removePrefix("[").removeSuffix("]")

                val fecha = try {
                    LocalDateTime.parse(fechaTexto, formatter)
                } catch (e: DateTimeParseException) {
                    lineasInvalidas++
                    println("Error parseando fecha: $linea")
                    null
                }
                if (fecha != null) {

                    val dentroRango =
                        (fechaInicio == null || !fecha.isBefore(fechaInicio)) &&
                                (fechaFin == null || !fecha.isAfter(fechaFin))

                    if (dentroRango) {
                        lineasValidas++

                        when {
                            mensaje.contains("INFO") -> mensajeInfo++
                            mensaje.contains("WARNING") -> mensajeWarning++
                            mensaje.contains("ERROR") -> mensajeError++
                            else -> {
                                println("Nivel no reconocido: $linea")
                                lineasInvalidas++
                            }
                        }

                        if (primeraFecha == null || fecha.isBefore(primeraFecha)) {
                            primeraFecha = fecha
                        }

                        if (ultimaFecha == null || fecha.isAfter(ultimaFecha)) {
                            ultimaFecha = fecha
                        }
                    }
                }
            }
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

    if (ficheroSalida != null) {
        try {
            File(ficheroSalida).writeText("""
                --- INFORME ---
                Total líneas procesadas: $totalLineas
                Total líneas válidas: $lineasValidas
                Total líneas inválidas: $lineasInvalidas
                Número de mensajes de INFO: $mensajeInfo
                Número de mensajes de WARNING: $mensajeWarning
                Número de mensajes de ERROR: $mensajeError
                Primera fecha encontrada: ${primeraFecha ?: "N/A"}
                Última fecha encontrada: ${ultimaFecha ?: "N/A"}
            """.trimIndent())
        } catch (e: IOException) {
            println("Error al escribir el fichero de salida")
        }
    }
}