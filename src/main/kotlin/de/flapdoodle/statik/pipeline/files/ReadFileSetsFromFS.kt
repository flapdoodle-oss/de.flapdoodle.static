package de.flapdoodle.statik.pipeline.files

import de.flapdoodle.statik.config.Sources
import de.flapdoodle.statik.files.CollectFileSet
import de.flapdoodle.statik.files.FileSet
import de.flapdoodle.statik.io.IO

class ReadFileSetsFromFS : ReadFileSets {
    override fun read(sources: Sources): List<FileSet> {
        return sources.sources.map { source ->
            val collector = CollectFileSet(sources.basePath, source)
            source.paths.forEach { path ->
                IO.scan(sources.basePath, collector)
            }
            collector.fileSet()
        }
    }
}