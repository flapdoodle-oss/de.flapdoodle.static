package de.flapdoodle.statik.pipeline.files

import de.flapdoodle.statik.config.Sources
import de.flapdoodle.statik.files.FileSet

interface ReadFileSets {
    fun read(sources: Sources): List<FileSet>
}