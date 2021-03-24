package de.flapdoodle.statik.files

import de.flapdoodle.statik.config.Source
import de.flapdoodle.statik.io.FileTreeEvent
import de.flapdoodle.statik.io.OnFileTreeEvent

class CollectFileSet(val config: Source) : OnFileTreeEvent {
    private var blobs = emptyList<Blob>()

    override fun onEvent(event: FileTreeEvent): OnFileTreeEvent.Action {
        when (event) {
            is FileTreeEvent.File -> {
                blobs = blobs + Blob.File(event.path, event.size, event.lastModified)
            }
            is FileTreeEvent.SymLink -> {
                blobs = blobs + Blob.SymLink(event.path, event.destination, event.lastModified)
            }
        }
        return OnFileTreeEvent.Action.Continue
    }

    fun fileSet(): FileSet {
        return FileSet(config.id, blobs)
    }
}