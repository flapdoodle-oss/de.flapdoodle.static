package de.flapdoodle.statik.files

import de.flapdoodle.statik.config.Source
import de.flapdoodle.statik.filetypes.Attributes
import de.flapdoodle.statik.io.FileTreeEvent
import de.flapdoodle.statik.io.OnFileTreeEvent
import de.flapdoodle.statik.io.PathPattern
import java.nio.file.Path

class CollectFileSet(
    val basePath: Path,
    val source: Source
) : OnFileTreeEvent {
    private var pathPatterns = PathPattern(source.paths)

    private var nodes = emptyList<Reference>()

    override fun onEvent(event: FileTreeEvent): OnFileTreeEvent.Action {
        val relativePath = basePath.relativize(event.path)

        when (event) {
            is FileTreeEvent.File -> {
                val match = pathPatterns.match(relativePath)
                if (match!=null) {
                    nodes = nodes + Reference.File(event.path, event.size, event.lastModified, Attributes.of(match))
                }
            }
            is FileTreeEvent.SymLink -> {
                val match = pathPatterns.match(relativePath)
                if (match!=null) {
                    nodes = nodes + Reference.SymLink(event.path, event.destination, event.lastModified, Attributes.of(match))
                }
            }
            else -> {
                // skip
            }
        }
        return OnFileTreeEvent.Action.Continue
    }

    fun fileSet(): FileSet {
        return FileSet(source.id, source.type, nodes)
    }
}