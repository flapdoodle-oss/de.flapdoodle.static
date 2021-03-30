package de.flapdoodle.statik.pipeline.compare

import de.flapdoodle.statik.filetypes.Attributes

class AttributesComparator(
    val comparators: List<Pair<String, Comparator<in Any>>>
) : Comparator<Attributes.Node> {
    override fun compare(attrA: Attributes.Node?, attrB: Attributes.Node?): Int {
        require(attrA != null) { "attrA is null" }
        require(attrB != null) { "attrB is null" }

        for (entry in comparators) {
            val property = entry.first
            val comparator = entry.second

            val path = property.split('.')
            val valA = attrA.find(path)?.singleOrNull()
            val valB = attrB.find(path)?.singleOrNull()

            val result = comparator.compare(valA, valB)
            if (result != 0) {
                return result
            }
        }

        return 0
    }
}