package de.flapdoodle.statik.pipeline.generate

import de.flapdoodle.statik.files.FileSet

interface ProcessMediaFiles {
    fun process(
        baseUrl: String,
        fileSets: List<FileSet>
    ): ProcessedMediaFiles
}