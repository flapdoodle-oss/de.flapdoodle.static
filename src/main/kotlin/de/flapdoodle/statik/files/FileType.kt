package de.flapdoodle.statik.files

enum class FileType(val id: String) {
    Text("text"),
    Binary("binary");

    companion object {
        fun byId(id: String): FileType {
            return values().find { it.id == id }
                ?: throw IllegalAccessException("unknown type: $id")
        }
    }
}