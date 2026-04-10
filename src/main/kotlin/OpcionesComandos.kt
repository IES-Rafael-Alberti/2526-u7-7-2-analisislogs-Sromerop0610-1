package org.iesra

data class CommandLineOptions(
    var input: String? = null,
    var output: String? = null,
    var stdout: Boolean = false
)