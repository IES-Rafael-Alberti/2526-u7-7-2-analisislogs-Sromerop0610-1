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
            INFO: ${analyzer.mensajeInfo}
            WARNING: ${analyzer.mensajeWarning}
            ERROR: ${analyzer.mensajeError}
            Primera fecha: ${analyzer.primeraFecha ?: "N/A"}
            Última fecha: ${analyzer.ultimaFecha ?: "N/A"}
        """.trimIndent()
    }

    fun generateStats(analyzer: LogAnalyzer): String {
        return """
            --- ESTADÍSTICAS ---
            Total líneas procesadas: ${analyzer.totalLineas}
            Total líneas válidas: ${analyzer.lineasValidas}
            Total líneas inválidas: ${analyzer.lineasInvalidas}
            INFO: ${analyzer.mensajeInfo}
            WARNING: ${analyzer.mensajeWarning}
            ERROR: ${analyzer.mensajeError}
        """.trimIndent()
    }

    fun saveReport(path: String, content: String) {
        try {
            File(path).writeText(content)
        } catch (e: IOException) {
            println("Error al escribir el fichero de salida")
        }
    }
}