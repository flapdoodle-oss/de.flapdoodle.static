package de.flapdoodle.statik.files

import de.flapdoodle.statik.filetypes.Attributes
import de.flapdoodle.statik.io.FileTreeEvent
import de.flapdoodle.statik.io.IO
import de.flapdoodle.statik.io.OnFileTreeEvent
import de.flapdoodle.statik.io.PathPattern
import java.nio.file.Path

class CollectFileSet(
    val basePath: Path,
    val fileSetId: String,
    paths: Set<String>,
    val fileType: FileType
) : OnFileTreeEvent {
    private var pathPatterns = PathPattern(paths)

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
        return FileSet(fileSetId, basePath, fileType, nodes)
    }

    companion object {
        fun read(
            basePath: Path,
            fileSetId: String,
            paths: Set<String>,
            fileType: FileType
        ): FileSet {
            val collector = CollectFileSet(basePath, fileSetId, paths, fileType)
            IO.scan(basePath, collector)
            return collector.fileSet()
        }
    }
}