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
                    if (i >= args.size) {
                        throw IllegalArgumentException("Falta el fichero de entrada")
                    }
                    options.input = args[i]
                }

                "-o", "--output" -> {
                    i++
                    if (i >= args.size) {
                        throw IllegalArgumentException("Falta el fichero de salida")
                    }
                    options.output = args[i]
                }

                "-p", "--stdout" -> {
                    options.stdout = true
                }

                "-f", "--from" -> {
                    i++
                    if (i >= args.size) {
                        throw IllegalArgumentException("Falta la fecha inicial")
                    }

                    options.from = try {
                        LocalDateTime.parse(args[i], formatter)
                    } catch (e: DateTimeParseException) {
                        throw IllegalArgumentException("Formato de fecha inválido en --from")
                    }
                }

                "-t", "--to" -> {
                    i++
                    if (i >= args.size) {
                        throw IllegalArgumentException("Falta la fecha final")
                    }

                    options.to = try {
                        LocalDateTime.parse(args[i], formatter)
                    } catch (e: DateTimeParseException) {
                        throw IllegalArgumentException("Formato de fecha inválido en --to")
                    }
                }

                "-l", "--level" -> {
                    i++
                    if (i >= args.size) {
                        throw IllegalArgumentException("Falta el valor para --level")
                    }

                    val nivelesTexto = args[i].split(",")

                    val niveles = mutableSetOf<Nivel>()

                    for (nivelTexto in nivelesTexto) {
                        val nivel = try {
                            Nivel.valueOf(nivelTexto.trim().uppercase())
                        } catch (e: IllegalArgumentException) {
                            throw IllegalArgumentException("Nivel no válido: $nivelTexto")
                        }
                        niveles.add(nivel)
                    }

                    options.levels = niveles
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