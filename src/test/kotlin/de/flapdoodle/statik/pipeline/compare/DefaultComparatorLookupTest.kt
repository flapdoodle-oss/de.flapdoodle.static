package de.flapdoodle.statik.pipeline.compare

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class DefaultComparatorLookupTest {

    private val testee = DefaultComparatorLookup

    @Test
    fun naturalComparatorIfEverythingIsAString() {
        val comparator = testee.comparableComparator(setOf("Foo","bar"))

        assertThat(comparator).isNotNull
        assertThat(comparator!!.type).isEqualTo(String::class)
    }

    @Test
    fun naturalComparatorIfEverythingIsANumber() {
        val comparator = testee.comparableComparator(setOf(2L,3L))

        assertThat(comparator).isNotNull
        assertThat(comparator!!.type).isEqualTo(Long::class)
    }
}