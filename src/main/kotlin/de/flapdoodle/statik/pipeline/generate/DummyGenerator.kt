package de.flapdoodle.statik.pipeline.generate

import de.flapdoodle.statik.config.Pages
import de.flapdoodle.statik.documents.Document
import de.flapdoodle.statik.documents.DocumentSet
import de.flapdoodle.statik.pipeline.templates.AlwaysPebbleRenderEngineFactory
import de.flapdoodle.statik.pipeline.templates.RenderEngineFactory
import de.flapdoodle.statik.pipeline.templates.wrapper.Renderable
import java.nio.file.Path

class DummyGenerator(
    private val pathMapGenerator: PathMapGenerator = DefaultPathMapGenerator(),
    private val renderEngineFactory: RenderEngineFactory = AlwaysPebbleRenderEngineFactory()
) : Generator {
    override fun generate(
        baseUrl: String,
        basePath: Path,
        pages: Pages,
        documents: List<DocumentSet>,
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

        val renderEngine = renderEngineFactory.renderEngine(basePath.resolve(pages.templatePath))

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