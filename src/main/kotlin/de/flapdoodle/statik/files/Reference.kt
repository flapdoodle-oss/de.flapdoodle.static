package de.flapdoodle.statik.files

import de.flapdoodle.statik.filetypes.Attributes
import de.flapdoodle.statik.io.LastModified
import java.nio.file.Path

sealed class Reference(
    open val path: Path,
    open val lastModified: LastModified,
    open val attributes: Attributes.Node
) {
    data class File(
        override val path: Path,
        val size: Long,
        override val lastModified: LastModified,
        override val attributes: Attributes.Node
    ) : Reference(path, lastModified, attributes)

    data class SymLink(
        override val path: Path,
        val destination: Path,
        override val lastModified: LastModified,
        override val attributes: Attributes.Node
    ) : Reference(path, lastModified, attributes)
}