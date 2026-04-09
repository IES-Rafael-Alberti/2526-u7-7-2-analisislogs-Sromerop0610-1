package org.iesra

import java.time.LocalDateTime

data class LogEntry(
    val fecha: LocalDateTime,
    val mensaje: String,
    val nivel: Nivel
)