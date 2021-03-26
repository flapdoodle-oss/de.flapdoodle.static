package de.flapdoodle.statik.path

import de.flapdoodle.statik.regex.Patterns
import de.flapdoodle.statik.types.Either
import de.flapdoodle.statik.types.asSet
import java.util.regex.Pattern

// /foo/bar/:property-nix/:other/nix
data class Path(
    val parts: List<Part>,
//    val pathPrefix: String? -- not used??
) {

    init {
        val propertyNames = propertyNames()
        val propertyNamesAsSet = propertyNames.toSet()
        require(propertyNames.size == propertyNamesAsSet.size) {"name used more than once: $propertyNames"}
        val indexOfPageName = propertyNames.indexOf(PAGE)
        require(indexOfPageName==-1 || indexOfPageName==propertyNames.size-1) {"page property is not last in path: $propertyNames"}
    }

    fun isPaging() = propertyNames().contains(PAGE)

    fun propertyNames() = parts.filterIsInstance<Part.Property>()
        .map { it.property }

    fun propertyNamesWithoutPage() = propertyNames().filter { it != PAGE }

    fun isEmpty() = parts.isEmpty()

    fun isPagedEmpty() = isEmpty() || propertyNamesWithoutPage().isEmpty()

    companion object {
        private val PATH_PROP_PATTERN = Pattern.compile("(:((?<name>([a-zA-Z0-9]+))(#(?<formatter>[a-zA-Z0-9]+))?))")
        const val PAGE = "page"
        const val FIRST_PAGE = 1

        fun parse(src: String): Path {
            var parts = emptyList<Part>()

            Patterns.parse(PATH_PROP_PATTERN, src) { either ->
                when (either) {
                    is Either.Left -> {
                        parts = parts + Part.Static(either.left)
                    }
                    is Either.Right -> {
                        parts = parts + Part.Property(
                            property = either.right.group("name") ?: throw IllegalArgumentException("name not found"),
                            formatter = either.right.group("formatter")
                        )
                    }
                }
            }
            return Path(parts = parts)
        }

    }

    sealed class Part {
        data class Static(
            val fixed: String,
        ) : Part()

        data class Property(
            val property: String,
            val formatter: String?
        ) : Part()
    }
}