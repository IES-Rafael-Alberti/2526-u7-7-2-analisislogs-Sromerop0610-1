package org.iesra

class ParserOpcionesComandos {

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

                else -> {
                    throw IllegalArgumentException("Opción desconocida: ${args[i]}")
                }
            }
            i++
        }

        return options
    }
}