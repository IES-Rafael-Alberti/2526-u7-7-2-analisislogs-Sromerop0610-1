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
            println("Error: la fecha inicial es mayor que la final")
            return
        }
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

    val entries = lineas.mapNotNull { parser.parseLine(it) }

    analyzer.analyze(entries, options.from, options.to, options.levels)

    val report = reporter.generateReport(analyzer)

    if (options.stdout) {
        println(report)
    }

    options.output?.let {
        reporter.saveReport(it, report)
    }
}