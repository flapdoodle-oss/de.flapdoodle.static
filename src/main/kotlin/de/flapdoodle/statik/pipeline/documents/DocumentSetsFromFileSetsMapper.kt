package de.flapdoodle.statik.pipeline.documents

import de.flapdoodle.statik.config.SourceType
import de.flapdoodle.statik.documents.DocumentFromReference
import de.flapdoodle.statik.documents.DocumentSet
import de.flapdoodle.statik.files.FileSet

class DocumentSetsFromFileSetsMapper(
    private val documentFromReference: DocumentFromReference
) : DocumentSetsFromFileSets {
    override fun scan(fileSets: List<FileSet>): List<DocumentSet> {
        return fileSets
            .filter { it.type == SourceType.Content }
            .map { set ->
                DocumentSet(
                    id = set.id,
                    documents = set.nodes.mapNotNull(documentFromReference::readFrom)
                )
            }
    }
}