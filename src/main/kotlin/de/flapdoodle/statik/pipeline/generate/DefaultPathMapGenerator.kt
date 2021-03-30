package de.flapdoodle.statik.pipeline.generate

import de.flapdoodle.statik.config.Id
import de.flapdoodle.statik.config.PageDefinition
import de.flapdoodle.statik.config.Pages
import de.flapdoodle.statik.documents.Document
import de.flapdoodle.statik.documents.DocumentSet
import de.flapdoodle.statik.filetypes.Attributes
import de.flapdoodle.statik.path.Path

class DefaultPathMapGenerator(
    val renderPath: RenderPath = RenderPathWithBaseUrl("baseUrl")
) : PathMapGenerator {
    override fun pathMapOf(pages: Pages, documents: List<DocumentSet>): Path2PagedDocumentsMap {
        var pathMap = Path2PagedDocumentsMap()

        pages.pageDefinitions.forEach { pageDefinition ->
            val parsedPath = Path.parse(pageDefinition.path)
            val matchingDocuments = documentsFor(documents, pageDefinition)
            val allDocuments = documents(matchingDocuments)

            if (pageDefinition.pageSize == 1) {
                // no paging
                allDocuments.forEach { (docSetId, doc) ->
                    val attributesMap = doc.allAttributes().flatten(".")
                    val renderedPath = renderPath.render(
                        parsedPath,
                        attributesMap,
                        formatterLookup = { _, _ -> DefaultObjectFormatter() })
                    pathMap = pathMap.add(
                        renderedPath,
                        Id.of(pageDefinition, PageDefinition::id),
                        listOf(doc)
                    )
                }
            } else {
                // paged
                val sortedDocument = sorted(allDocuments, pageDefinition)
                val pageChunks = sortedDocument.chunked(pageDefinition.pageSize)
                pageChunks.forEachIndexed { page, documents ->
                    val attributesMap = Attributes.of(mapOf(Path.PAGE to page+1)).flatten(".")
                    val renderedPath = renderPath.render(
                        parsedPath,
                        attributesMap,
                        formatterLookup = { _, _ -> DefaultObjectFormatter() })
                    pathMap = pathMap.add(
                        renderedPath,
                        Id.of(pageDefinition, PageDefinition::id),
                        documents.map { it.second }
                    )
                }
            }
        }

        return pathMap
    }

    private fun documentsFor(documents: List<DocumentSet>, pageDefinition: PageDefinition): List<DocumentSet> {
        if (pageDefinition.documents.isEmpty()) return documents

        return documents.filter { pageDefinition.documents.contains(it.id) }
    }

    private fun documents(documents: List<DocumentSet>): List<Pair<String, Document>> {
        return documents.flatMap { docSet ->
            docSet.documents.map { docSet.id to it }
        }
    }

    private fun sorted(
        documents: List<Pair<String, Document>>,
        pageDefinition: PageDefinition
    ): List<Pair<String, Document>> {
        return documents.sortedWith(comparator(pageDefinition.orderBy))
    }

    private fun comparator(orderBy: Set<String>): Comparator<Pair<String, Document>> {
        return Comparator { a, b ->
            val docA = a.second.allAttributes().flatten(".")
            val docB = b.second.allAttributes().flatten(".")
            compare(orderBy, docA, docB)
        }
    }

    private fun compare(
        orderBy: Set<String>,
        a: Map<String, Any>,
        b: Map<String, Any>
    ): Int {
        for (name in orderBy) {
            val valA = a[name]
            val valB = b[name]
            val comparsion: Int = compare(valA, valB)
            if (comparsion != 0) {
                return comparsion
            }
        }

        return 0
    }

    private fun compare(a: Any?, b: Any?): Int {
        return 0
    }
}