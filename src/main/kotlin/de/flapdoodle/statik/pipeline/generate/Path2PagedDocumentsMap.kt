package de.flapdoodle.statik.pipeline.generate

import de.flapdoodle.statik.config.Id
import de.flapdoodle.statik.config.PageDefinition
import de.flapdoodle.statik.documents.Document
import de.flapdoodle.statik.types.groupByUnique

data class Path2PagedDocumentsMap(
    val pathList: List<Path2PagedDocuments> = emptyList()
): PathOfDocumentInPage {

    private val pathMap = pathList.groupByUnique(Path2PagedDocuments::path)
    private val maxPage = maxPageByPageDefinition(pathList)

    data class Path2PagedDocuments(
        val path: String,
        val pageDefinition: Id<PageDefinition>,
        val documents: List<Id<Document>>,
        val page: Int?
    )

    fun add(path: String, pageDefinitionId: Id<PageDefinition>, documents: List<Document>,page: Int? = null): Path2PagedDocumentsMap {
        return copy(pathList = pathList + Path2PagedDocuments(
            path=path,
            pageDefinition=pageDefinitionId,
            documents = documents.map { Id(it.id, Document::class) },
            page = page
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

    override fun pathOf(pageDefinition: Id<PageDefinition>, document: Id<Document>): PathOfDocument? {
        val entry = pathList.find {
            it.pageDefinition==pageDefinition && it.documents.contains(document)
        }
        if (entry!=null) {
            return PathOfDocument(
                path = entry.path,
                page = entry.page,
                maxPage = maxPage[entry.pageDefinition]
            )
        }
        return null
    }

    fun maxPageOf(pageDefinition: Id<PageDefinition>): Int? {
        return maxPage[pageDefinition]
    }

    fun pathOf(pageDefinition: Id<PageDefinition>, page: Int): String? {
        return pathList.find {
            it.pageDefinition==pageDefinition && it.page==page
        }?.path
    }

    companion object {
        private fun maxPageByPageDefinition(src: List<Path2PagedDocuments>): Map<Id<PageDefinition>, Int> {
            var ret = emptyMap<Id<PageDefinition>, Int>()
            src.forEach {
                if (it.page!=null) {
                    val last = ret[it.pageDefinition]
                    val max = if (last != null) {
                        Math.max(last,it.page)
                    } else {
                        it.page
                    }
                    ret = ret + (it.pageDefinition to max)
                }
            }
            return ret
        }
    }
}