package de.flapdoodle.statik.pipeline.generate

import de.flapdoodle.statik.config.Id
import de.flapdoodle.statik.config.PageDefinition
import de.flapdoodle.statik.config.Pages
import de.flapdoodle.statik.config.Site
import de.flapdoodle.statik.documents.Document
import de.flapdoodle.statik.documents.DocumentSet
import de.flapdoodle.statik.pipeline.templates.Paging
import de.flapdoodle.statik.pipeline.templates.RenderData
import de.flapdoodle.statik.pipeline.templates.RenderEngine
import java.nio.file.Path
import javax.inject.Inject

class DummyGenerator @Inject constructor(
    private val pathMapGenerator: PathMapGenerator
) : Generator {
    override fun generate(
        baseUrl: String,
        basePath: Path,
        pages: Pages,
        site: Site,
        documents: List<DocumentSet>,
        renderEngine: RenderEngine
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
            val pageDefinition = pages[entry.pageDefinition.id]
            val templateName = pageDefinition.template

            val currentPage = entry.page

            val paging = currentPage?.let {
                val maxPage = pathMap.maxPageOf(entry.pageDefinition)!!

                Paging(
                    current = Paging.Page(currentPage, path),
                    prev = pageFor(pathMap, entry.pageDefinition, if (currentPage>0) currentPage-1 else null),
                    next = pageFor(pathMap, entry.pageDefinition, if (currentPage<maxPage) currentPage+1 else null),
                    first = pageFor(pathMap, entry.pageDefinition, 0)!!,
                    last = pageFor(pathMap, entry.pageDefinition, maxPage)!!
                )
            }

            val renderedContent = renderEngine.render(
                templateName, RenderData(
                    url = path,
                    baseUrl = baseUrl,
                    site = site,
                    documents = matchingDocuments,
                    pathOfDocumentInPage = pathMap,
                    paging = paging
                )
            )
            RendererPages.Page(path, renderedContent)
//            path to renderedContent
        }

//        renderedDocuments.forEach { (path, content) ->
//            println("-------------------")
//            println("path: $path")
//            println("- - - - - - - - - -")
//            println(content)
//        }

        return RendererPages(renderedDocuments)
    }

    private fun pageFor(pathMap: Path2PagedDocumentsMap, pageDefinition: Id<PageDefinition>, page: Int?): Paging.Page? {
        if (page!=null) {
            return Paging.Page(
                index = page,
                path = pathMap.pathOf(pageDefinition, page)!!
            )
        }
        return null
    }
}