package de.flapdoodle.statik.files

import de.flapdoodle.statik.config.Source
import de.flapdoodle.statik.io.FileTreeEvent
import de.flapdoodle.statik.io.OnFileTreeEvent
import de.flapdoodle.statik.io.PathPattern
import java.nio.file.Path

class CollectFileSet(
    val basePath: Path,
    val config: Source
) : OnFileTreeEvent {
    private var pathPatterns = PathPattern(config.paths)

    private var blobs = emptyList<Blob>()

    override fun onEvent(event: FileTreeEvent): OnFileTreeEvent.Action {
        val relativePath = basePath.relativize(event.path)

        when (event) {
            is FileTreeEvent.File -> {
                val match = pathPatterns.match(relativePath)
                if (match!=null) {
                    blobs = blobs + Blob.File(event.path, event.size, event.lastModified, match)
                }
            }
            is FileTreeEvent.SymLink -> {
                val match = pathPatterns.match(relativePath)
                if (match!=null) {
                    blobs = blobs + Blob.SymLink(event.path, event.destination, event.lastModified, match)
                }
            }
        }
        return OnFileTreeEvent.Action.Continue
    }

    fun fileSet(): FileSet {
        return FileSet(config.id, blobs)
    }
}