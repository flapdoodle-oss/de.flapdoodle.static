package de.flapdoodle.statik.pipeline.templates.wrapper

import de.flapdoodle.statik.documents.Document
import de.flapdoodle.statik.pipeline.generate.PathOfDocumentInPage

fun interface RenderableFactory {
    fun create(
        path: String,
        documents: List<Document>,
        pathOfDocumentInPage: PathOfDocumentInPage
    ): Renderable
}