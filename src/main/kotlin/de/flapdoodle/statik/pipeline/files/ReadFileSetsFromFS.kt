package de.flapdoodle.statik.pipeline.files

import de.flapdoodle.statik.config.Sources
import de.flapdoodle.statik.files.CollectFileSet
import de.flapdoodle.statik.files.FileSet
import de.flapdoodle.statik.io.IO

class ReadFileSetsFromFS : ReadFileSets {
    override fun read(sources: Sources): List<FileSet> {
        return sources.sources.map { source ->
            CollectFileSet.read(sources.basePath, source.id, source.paths, source.type)
        }
    }
}