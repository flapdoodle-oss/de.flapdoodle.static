package de.flapdoodle.statik.pipeline.publish

import de.flapdoodle.statik.pipeline.generate.RendererPages

class Dump2ConsolePublisher : Publisher {
    override fun publish(rendererPages: RendererPages) {
        rendererPages.pages.forEach { page ->
            println("-------------------")
            println("path: ${page.path} (${page.mimeType})")
            println("- - - - - - - - - -")
            println(page.content)
        }
    }

    override fun baseUrl(): String {
        return ""
    }
}