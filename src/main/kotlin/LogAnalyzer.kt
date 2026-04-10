package org.iesra

import java.time.LocalDateTime

class LogAnalyzer {

    var totalLineas = 0
        private set
    var lineasValidas = 0
        private set
    var lineasInvalidas = 0
        private set
    var mensajeInfo = 0
        private set
    var mensajeWarning = 0
        private set
    var mensajeError = 0
        private set
    var primeraFecha: LocalDateTime? = null
        private set
    var ultimaFecha: LocalDateTime? = null
        private set

    fun analyze(entries: List<LogEntry>, fechaInicio: LocalDateTime? = null, fechaFin: LocalDateTime? = null, niveles: Set<Nivel>?= null) {
        totalLineas = entries.size
        for (entry in entries) {
            val dentroRango = (fechaInicio == null || !entry.fecha.isBefore(fechaInicio)) &&
                    (fechaFin == null || !entry.fecha.isAfter(fechaFin))

            if (dentroRango) {
                lineasValidas++
                when (entry.nivel) {
                    Nivel.INFO -> mensajeInfo++
                    Nivel.WARNING -> mensajeWarning++
                    Nivel.ERROR -> mensajeError++
                    Nivel.UNKNOWN -> lineasInvalidas++
                }

                if (primeraFecha == null || entry.fecha.isBefore(primeraFecha)) {
                    primeraFecha = entry.fecha
                }
                if (ultimaFecha == null || entry.fecha.isAfter(ultimaFecha)) {
                    ultimaFecha = entry.fecha
                }
            } else {
                lineasInvalidas++
            }
        }
    }
}