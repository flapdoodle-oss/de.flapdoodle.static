package de.flapdoodle.statik.pipeline.publish

import de.flapdoodle.statik.files.FileSet
import de.flapdoodle.statik.pipeline.generate.ProcessedMediaFiles
import de.flapdoodle.statik.pipeline.generate.RendererPages

interface Publisher {
    fun publish(rendererPages: RendererPages, mediaFiles: ProcessedMediaFiles)
    fun baseUrl(): String
}