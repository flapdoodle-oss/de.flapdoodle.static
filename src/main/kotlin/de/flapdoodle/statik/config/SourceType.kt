package de.flapdoodle.statik.config

enum class SourceType(val id: String) {
    Content("content"),
    Images("images");

    companion object {
        fun byId(id: String): SourceType {
            return values().find { it.id == id }
                ?: throw IllegalAccessException("unknown type: $id")
        }
    }
}