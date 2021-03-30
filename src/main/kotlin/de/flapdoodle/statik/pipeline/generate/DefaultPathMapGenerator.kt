package de.flapdoodle.statik.pipeline.generate

import de.flapdoodle.statik.config.Id
import de.flapdoodle.statik.config.PageDefinition
import de.flapdoodle.statik.config.Pages
import de.flapdoodle.statik.documents.Document
import de.flapdoodle.statik.documents.DocumentSet
import de.flapdoodle.statik.filetypes.Attributes
import de.flapdoodle.statik.path.Path
import de.flapdoodle.statik.pipeline.compare.*
import de.flapdoodle.statik.types.head
import de.flapdoodle.statik.types.tail

class DefaultPathMapGenerator(
    val renderPath: RenderPath = RenderPathWithBaseUrl("baseUrl"),
    val comparatorLookup: ComparatorLookup = DefaultComparatorLookup
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
                    val attributesMap = doc.allAttributes()
                    val renderedPath = renderPath.render(
                        parsedPath,
                        propertyLookup = { name -> attributesMap.find(name.split('.'))?.singleOrNull()},
                        formatterLookup = { _, _ -> DefaultObjectFormatter() })
                    pathMap = pathMap.add(
                        renderedPath,
                        Id.of(pageDefinition, PageDefinition::id),
                        listOf(doc)
                    )
                }
            } else {
                // paged
                val sortedDocument = sorted(allDocuments, pageDefinition, comparatorLookup)
                val pageChunks = sortedDocument.chunked(pageDefinition.pageSize)
                pageChunks.forEachIndexed { page, documents ->
                    val attributesMap = Attributes.of(mapOf(Path.PAGE to page+1))
                    val renderedPath = renderPath.render(
                        parsedPath,
                        propertyLookup = { name -> attributesMap.find(name.split('.'))?.singleOrNull()},
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
        pageDefinition: PageDefinition,
        comparatorLookup: ComparatorLookup
    ): List<Pair<String, Document>> {
        val documentAttributes = documents.map { it.second.allAttributes() }

        val comparatorByProperty: List<Pair<String, java.util.Comparator<in Any>?>> = pageDefinition.orderBy.map {
            val path = it.split('.')
            val values = documentAttributes.map { attr -> attr.find(path)?.singleOrNull() }.toSet()
            val comparator = comparatorLookup.comparatorFor(values)
            it to comparator
        }

        val missingComparators = comparatorByProperty.filter { it.second==null }.map { it.first }

        require(missingComparators.isEmpty()) {
            "could not sort by $missingComparators"
        }

        val comparator = AttributesComparator(comparatorByProperty.map { it.first to it.second!! })


        return documents.sortedWith(Comparators.compareWith(comparator) { it.second.allAttributes() })
    }
}