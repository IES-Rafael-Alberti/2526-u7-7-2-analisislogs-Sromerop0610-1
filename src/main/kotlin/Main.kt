package org.iesra

import ParserOpcionesComandos

fun main(args: Array<String>) {

    val parserCLI = ParserOpcionesComandos()

    val options = try {
        parserCLI.parse(args)
    } catch (e: Exception) {
        println(e.message)
        return
    }

    if (options.input == null) {
        println("Error: debes indicar --input")
        return
    }

    if (!options.stdout && options.output == null) {
        println("Error: debes indicar --stdout o --output")
        return
    }

    if (options.from != null && options.to != null) {
        if (options.from!!.isAfter(options.to)) {
            println("Error: rango de fechas inválido")
            return
        }
    }

    if (options.stats && options.report) {
        println("Error: no puedes usar --stats y --report a la vez")
        return
    }

    if (!options.stats && !options.report) {
        options.report = true
    }

    val loader = LogFileLoader()
    val parser = LogParser()
    val analyzer = LogAnalyzer()
    val reporter = ReportGenerator()

    val lineas = try {
        loader.loadLines(options.input!!)
    } catch (e: Exception) {
        println(e.message)
        return
    }

    val entries = mutableListOf<LogEntry>()
    var invalidas = 0

    for (linea in lineas) {
        val entry = parser.parseLine(linea)

        if (entry != null) {
            entries.add(entry)
        } else {
            invalidas++

            if (!options.ignoreInvalid) {
                println("Línea inválida detectada: $linea")
                return
            }
        }
    }

    analyzer.analyze(
        entries,
        options.from,
        options.to,
        options.levels,
        invalidas
    )

    val output = if (options.stats) {
        reporter.generateStats(analyzer)
    } else {
        reporter.generateReport(analyzer)
    }

    if (options.stdout) {
        println(output)
    }

    options.output?.let {
        reporter.saveReport(it, output)
    }
}