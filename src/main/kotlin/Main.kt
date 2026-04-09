package org.iesra

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException

fun main(args: Array<String>) {

    if (args.isEmpty()) {
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

    // Instanciar las clases
    val loader = LogFileLoader()
    val parser = LogParser()
    val analyzer = LogAnalyzer()
    val reporter = ReportGenerator()

    // Cargar y parsear logs
    val lineas = try {
        loader.loadLines(rutaFichero)
    } catch (e: Exception) {
        println(e.message)
        return
    }

    val entries = lineas.mapNotNull { parser.parseLine(it) }

    // Analizar
    analyzer.analyze(entries, fechaInicio, fechaFin)

    // Generar informe
    val report = reporter.generateReport(analyzer)
    println(report)

    ficheroSalida?.let { reporter.saveReport(it, report) }
}