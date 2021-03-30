package de.flapdoodle.statik.pipeline.generate

import de.flapdoodle.statik.config.Pages
import de.flapdoodle.statik.documents.Document
import de.flapdoodle.statik.documents.DocumentSet

class DummyGenerator(
    private val pathMapGenerator: PathMapGenerator = DefaultPathMapGenerator(),
    private val renderPage: RenderPage = DummyPageRenderer()
) : Generator {
    override fun generate(pages: Pages, documents: List<DocumentSet>) {
        val pathMap = pathMapGenerator.pathMapOf(pages,documents)
        pathMap.forEach{ path, entry ->
            println("-------------------")
            println("path: $path")
            println("pageId: ${entry.pageDefinition}")

            entry.documents.forEach {
                println("-> $it")
            }
        }

        val documentsById = documents.flatMap { it.documents.map { it.id to it } }.toMap()

        val renderedDocuments = pathMap.map { path, entry ->
            val matchingDocuments: List<Document> = entry.documents.map {
                documentsById[it.id]
                    ?: throw IllegalArgumentException("document ${it.id} not found") }
            path to renderPage.render(path, pages[entry.pageDefinition.id], matchingDocuments)
        }

        renderedDocuments.forEach { (path, content) ->
            println("-------------------")
            println("path: $path")
            println("- - - - - - - - - -")
            println(content)
        }

    }
}