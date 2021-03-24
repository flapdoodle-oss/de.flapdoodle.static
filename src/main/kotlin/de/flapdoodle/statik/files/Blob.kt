package de.flapdoodle.statik.files

import de.flapdoodle.statik.io.LastModified
import java.nio.file.Path

sealed class Blob(
    open val path: Path,
    open val lastModified: LastModified
) {
    data class File(
        override val path: Path,
        val size: Long,
        override val lastModified: LastModified
    ) : Blob(path, lastModified)

    data class SymLink(
        override val path: Path,
        val destination: Path,
        override val lastModified: LastModified
    ) : Blob(path, lastModified)
}