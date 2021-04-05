package de.flapdoodle.statik.pipeline.publish

import de.flapdoodle.statik.files.FileSet
import de.flapdoodle.statik.pipeline.generate.ProcessedMediaFiles
import de.flapdoodle.statik.pipeline.generate.RendererPages

class Dump2ConsolePublisher : Publisher {
    override fun publish(rendererPages: RendererPages, mediaFiles: ProcessedMediaFiles) {
        rendererPages.pages.forEach { page ->
            println("-------------------")
            println("path: ${page.path} (${page.mimeType})")
            println("- - - - - - - - - -")
            println(page.content)
        }
        println("-------------------")
        mediaFiles.files.forEach { file ->
            println("path: ${file.path}:${file.content.size} (${file.mimeType})")
        }
    }

    override fun baseUrl(): String {
        return ""
    }
}