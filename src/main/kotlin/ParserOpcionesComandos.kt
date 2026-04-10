import org.iesra.OpcionesComandos
import org.iesra.Nivel

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException

class ParserOpcionesComandos {

    private val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")

    fun parse(args: Array<String>): OpcionesComandos {
        val options = OpcionesComandos()

        var i = 0
        while (i < args.size) {
            when (args[i]) {

                "-i", "--input" -> {
                    i++
                    options.input = args[i]
                }

                "-o", "--output" -> {
                    i++
                    options.output = args[i]
                }

                "-p", "--stdout" -> {
                    options.stdout = true
                }

                "-f", "--from" -> {
                    i++
                    options.from = LocalDateTime.parse(args[i], formatter)
                }

                "-t", "--to" -> {
                    i++
                    options.to = LocalDateTime.parse(args[i], formatter)
                }

                "-l", "--level" -> {
                    i++
                    val niveles = args[i].split(",").map {
                        Nivel.valueOf(it.trim().uppercase())
                    }.toSet()
                    options.levels = niveles
                }

                "-s", "--stats" -> {
                    options.stats = true
                }

                "-r", "--report" -> {
                    options.report = true
                }

                "--ignore-invalid" -> {
                    options.ignoreInvalid = true
                }

                else -> {
                    throw IllegalArgumentException("Opción desconocida: ${args[i]}")
                }
            }
            i++
        }

        return options
    }
}