package de.flapdoodle.statik.files

import de.flapdoodle.statik.config.SourceType

data class FileSet(
    val id: String,
    val type: SourceType,
    val nodes: List<Reference>
)