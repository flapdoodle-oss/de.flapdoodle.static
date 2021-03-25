package de.flapdoodle.statik.pipeline.documents

import de.flapdoodle.statik.documents.Document
import de.flapdoodle.statik.documents.DocumentSet
import de.flapdoodle.statik.files.FileSet

interface DocumentSetsFromFileSets {
    fun scan(fileSets: List<FileSet>): List<DocumentSet>
}