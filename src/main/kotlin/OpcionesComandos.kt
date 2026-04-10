package org.iesra

import java.time.LocalDateTime

data class OpcionesComandos(
    var input: String? = null,
    var output: String? = null,
    var stdout: Boolean = false,

    var from: LocalDateTime? = null,
    var to: LocalDateTime? = null,

    var levels: Set<Nivel>? = null
)