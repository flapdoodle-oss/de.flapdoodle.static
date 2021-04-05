package de.flapdoodle.statik.pipeline.generate

import de.flapdoodle.statik.config.Pages
import de.flapdoodle.statik.documents.Document
import de.flapdoodle.statik.documents.DocumentSet
import de.flapdoodle.statik.pipeline.templates.RenderEngine
import de.flapdoodle.statik.pipeline.templates.wrapper.Renderable
import java.nio.file.Path
import javax.inject.Inject

class DummyGenerator @Inject constructor(
    private val pathMapGenerator: PathMapGenerator
) : Generator {
    override fun generate(
        baseUrl: String,
        basePath: Path,
        pages: Pages,
        documents: List<DocumentSet>,
        renderEngine: RenderEngine,
        renderableFactory: (path: String, documents: List<Document>) -> Renderable
    ): RendererPages {
        val pathMap = pathMapGenerator.pathMapOf(baseUrl, pages, documents)
        pathMap.forEach { path, entry ->
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
                    ?: throw IllegalArgumentException("document ${it.id} not found")
            }
            val renderable = renderableFactory(path, matchingDocuments)
            val pageDefinition = pages[entry.pageDefinition.id]
            val templateName = pageDefinition.template

            val renderedContent = renderEngine.render(templateName, renderable)
            RendererPages.Page(path, renderedContent)
//            path to renderedContent
        }

        renderedDocuments.forEach { (path, content) ->
            println("-------------------")
            println("path: $path")
            println("- - - - - - - - - -")
            println(content)
        }

        return RendererPages(renderedDocuments)
    }
}