package de.flapdoodle.statik.pipeline.generate

import de.flapdoodle.statik.config.PageDefinition
import de.flapdoodle.statik.config.Pages
import de.flapdoodle.statik.documents.DocumentSet
import de.flapdoodle.statik.path.Path

class DummyGenerator(
    private val pathMapGenerator: PathMapGenerator = DefaultPathMapGenerator()
) : Generator {
    override fun generate(pages: Pages, documents: List<DocumentSet>) {
        val pathMap = pathMapGenerator.pathMapOf(pages,documents)
        pathMap.forEach{ path, entry ->
            println("-------------------")
            println(path)
            entry.documents.forEach {
                println("-> $it")
            }
        }

//        pages.pageDefinitions.forEach { pageDefinition ->
//            val matchingDocuments = documentsFor(documents, pageDefinition)
//            matchingDocuments.forEach { docSet ->
//                docSet.documents.forEach { doc ->
//                    val parsedPath = Path.parse(pageDefinition.path)
//                    val attributesMap = (doc.allAttributes() + (Path.PAGE to 2)).flatten(".")
//                    println("map: $attributesMap")
//
//                    val renderedPath = renderPath.render(parsedPath,attributesMap,formatterLookup = {_,_ -> DefaultObjectFormatter()})
//                    println("could put \n${doc.reference}\n to\n$renderedPath")
//                }
//            }
//        }
    }

//    private fun documentsFor(documents: List<DocumentSet>, pageDefinition: PageDefinition): List<DocumentSet> {
//        if (pageDefinition.documents.isEmpty()) return documents
//
//        return documents.filter { pageDefinition.documents.contains(it.id) }
//    }
}