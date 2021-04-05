package de.flapdoodle.statik.files

import java.nio.file.Path

data class FileSet(
    val id: String,
    val basePath: Path,
    val type: FileType,
    val nodes: List<Reference>
)