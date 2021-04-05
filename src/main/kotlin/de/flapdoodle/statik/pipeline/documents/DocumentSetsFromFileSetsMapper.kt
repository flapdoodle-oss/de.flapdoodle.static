package de.flapdoodle.statik.pipeline.documents

import de.flapdoodle.statik.files.FileType
import de.flapdoodle.statik.documents.DocumentFromReference
import de.flapdoodle.statik.documents.DocumentSet
import de.flapdoodle.statik.files.FileSet
import javax.inject.Inject

class DocumentSetsFromFileSetsMapper @Inject constructor(
    private val documentFromReference: DocumentFromReference
) : DocumentSetsFromFileSets {
    override fun scan(fileSets: List<FileSet>): List<DocumentSet> {
        return fileSets
            .filter { it.type == FileType.Text }
            .map { set ->
                DocumentSet(
                    id = set.id,
                    documents = set.nodes.mapNotNull(documentFromReference::readFrom)
                )
            }
    }
}