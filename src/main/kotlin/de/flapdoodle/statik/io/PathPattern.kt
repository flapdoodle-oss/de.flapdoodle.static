package de.flapdoodle.statik.io

import java.nio.file.Path
import java.util.regex.Matcher
import java.util.regex.Pattern


data class PathPattern(
    private val patterns: Set<String>
) {
    private var regex: List<Pair<Pattern, Set<String>>> = patterns.map {
        val pattern = Pattern.compile(it)
        val groupNames = groupNames(pattern)
        pattern to groupNames
    }

    fun match(path: Path): Map<String, String>? {
        val asString = path.toString()
        regex.forEach {
            val matcher = it.first.matcher(asString)
            if (matcher.matches()) {
                return it.second.associateWith { key -> matcher.group(key) }
            }
        }
        return null
    }

    companion object {
        val GROUP_NAMES_PATTERN = Pattern.compile("\\?\\<([a-zA-Z0-9]+)\\>")

        fun groupNames(pattern: Pattern): Set<String> {
            var ret = emptySet<String>()
            val groupNamesMatcher: Matcher = GROUP_NAMES_PATTERN.matcher(pattern.pattern())
            while (groupNamesMatcher.find()) {
                ret = ret + groupNamesMatcher.group(1)
            }
            return ret
        }
    }
}