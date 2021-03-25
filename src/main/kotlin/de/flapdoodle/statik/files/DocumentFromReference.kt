package de.flapdoodle.statik.files

interface DocumentFromReference {
    fun readFrom(reference: Reference): Document?
}