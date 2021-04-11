package de.flapdoodle.statik.pipeline.generate

import de.flapdoodle.statik.config.Id
import de.flapdoodle.statik.config.PageDefinition
import de.flapdoodle.statik.documents.Document
import de.flapdoodle.statik.types.groupByUnique

data class Path2PagedDocumentsMap(
    val pathList: List<Path2PagedDocuments> = emptyList()
): PathOfDocumentInPage {

    private val pathMap = pathList.groupByUnique(Path2PagedDocuments::path)

    data class Path2PagedDocuments(
        val path: String,
        val pageDefinition: Id<PageDefinition>,
        val documents: List<Id<Document>>
    )

    fun add(path: String, pageDefinitionId: Id<PageDefinition>, documents: List<Document>): Path2PagedDocumentsMap {
        return copy(pathList = pathList + Path2PagedDocuments(
            path=path,
            pageDefinition=pageDefinitionId,
            documents = documents.map { Id(it.id, Document::class) }
        ))
    }

    operator fun get(path: String): Path2PagedDocuments? {
        return pathMap[path]
    }

    fun forEach(action: (String, Path2PagedDocuments) -> Unit) {
        pathMap.forEach(action)
    }

    fun <D> map(transformation: (String, Path2PagedDocuments) -> D): List<D> {
        return pathMap.map { transformation(it.key, it.value) }
    }

    override fun pathOf(pageDefinition: Id<PageDefinition>, document: Id<Document>): String? {
        return pathList.find {
            it.pageDefinition==pageDefinition && it.documents.contains(document)
        }?.path
    }
}