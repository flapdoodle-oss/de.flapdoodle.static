package de.flapdoodle.statik.documents

import de.flapdoodle.statik.files.Reference

interface DocumentFromReference {
    fun readFrom(reference: Reference): Document?
}