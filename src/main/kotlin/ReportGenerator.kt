package org.iesra

import java.io.File
import java.io.IOException

class ReportGenerator {

    fun generateReport(analyzer: LogAnalyzer): String {
        return """
            --- INFORME ---
            Total líneas procesadas: ${analyzer.totalLineas}
            Total líneas válidas: ${analyzer.lineasValidas}
            Total líneas inválidas: ${analyzer.lineasInvalidas}
            Número de mensajes de INFO: ${analyzer.mensajeInfo}
            Número de mensajes de WARNING: ${analyzer.mensajeWarning}
            Número de mensajes de ERROR: ${analyzer.mensajeError}
            Primera fecha encontrada: ${analyzer.primeraFecha ?: "N/A"}
            Última fecha encontrada: ${analyzer.ultimaFecha ?: "N/A"}
        """.trimIndent()
    }

    fun saveReport(rutaSalida: String, report: String) {
        try {
            File(rutaSalida).writeText(report)
        } catch (e: IOException) {
            println("Error al escribir el fichero de salida")
        }
    }
}