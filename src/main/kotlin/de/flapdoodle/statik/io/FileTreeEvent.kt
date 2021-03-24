package de.flapdoodle.statik.io

import java.nio.file.Path

sealed class FileTreeEvent(open val path: Path) {
    data class Enter(override val path: Path) : FileTreeEvent(path)
    data class Leave(override val path: Path) : FileTreeEvent(path)

    data class SymLink(
            override val path: Path,
            val destination: Path,
            val lastModified: LastModified
    ) : FileTreeEvent(path)

    data class File(
            override val path: Path,
            val size: Long,
            val lastModified: LastModified
    ) : FileTreeEvent(path)
}