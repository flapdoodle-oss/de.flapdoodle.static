package de.flapdoodle.statik.files

enum class ContentType {
    Markdown, Html, Text;

    companion object {
        fun ofExtension(ext: String): ContentType? {
            return when (ext) {
                "md" -> Markdown
                "html", "htm" -> Html
                "txt" -> Text
                else -> null
            }
        }
    }
}