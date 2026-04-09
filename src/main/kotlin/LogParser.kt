package org.iesra

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException

class LogParser {

    private val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
    private val regexFechaHora = Regex("""\[\d{4}-(0[1-9]|1[0-2])-(0[1-9]|[12]\d|3[01]) (?:[01]\d|2[0-3]):[0-5]\d:[0-5]\d\]""")

    fun parseLine(linea: String): LogEntry? {
        val indiceCierre = linea.indexOf(']')
        if (indiceCierre == -1) return null

        val fechaHora = linea.substring(0, indiceCierre + 1)
        val mensaje = linea.substring(indiceCierre + 1).trim()

        if (!regexFechaHora.matches(fechaHora)) return null

        val fechaTexto = fechaHora.removePrefix("[").removeSuffix("]")
        val fecha = try {
            LocalDateTime.parse(fechaTexto, formatter)
        } catch (e: DateTimeParseException) {
            return null
        }

        val nivel = when {
            mensaje.contains("INFO") -> Nivel.INFO
            mensaje.contains("WARNING") -> Nivel.WARNING
            mensaje.contains("ERROR") -> Nivel.ERROR
            else -> Nivel.UNKNOWN
        }

        return LogEntry(fecha, mensaje, nivel)
    }
}