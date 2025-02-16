package com.kylecorry.trail_sense.shared.text

import com.kylecorry.sol.math.Range
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class TextUtilsTest {

    @Test
    fun getKeywords() {
        val text = """
            This is a test of the keyword tokenizer. It should return a list of keywords.
            Here's an example of contractions: don't, can't, won't, shouldn't, wouldn't.
            
            Knife, Knives
            
            Compasses, Compass
        """.trimIndent()

        val keywords = TextUtils.getKeywords(text, additionalStemWords = mapOf("knives" to "knife"))
        val expected = setOf(
            "test",
            "keyword",
            "token",
            "return",
            "list",
            "exampl", // This is how the stemmer works
            "contraction",
            "here",
            "knife",
            "compass"
        )

        assertEquals(expected, keywords)
    }

    @Test
    fun getPercentMatch() {
        val text = """
            This is a test of the keyword tokenizer. It should return a list of keywords.
            Here's an example of contractions: don't, can't, won't, shouldn't, wouldn't.
        """.trimIndent()

        assertEquals(1f, TextUtils.getQueryMatchPercent(text, text), 0f)
        assertEquals(1f, TextUtils.getQueryMatchPercent("This is a test", text), 0f)
        assertEquals(1f, TextUtils.getQueryMatchPercent("testing", text), 0f)
        assertEquals(
            0.667f,
            TextUtils.getQueryMatchPercent("Something about a keyword", text),
            0.001f
        )
    }

    @Test
    fun fuzzySearch() {
        val text = """
            This is a test of the keyword tokenizer. It should return a list of keywords.
            Here's an example of contractions: don't, can't, won't, shouldn't, wouldn't.
        """.trimIndent()

        assertFuzzySearchEqual(
            listOf(
                1f to Range(0, 40),
            ), TextUtils.fuzzySearch("This is a test", text)
        )
        assertFuzzySearchEqual(
            listOf(
                1f to Range(0, 40),
                1f to Range(41, 77),
            ), TextUtils.fuzzySearch("keyword", text)
        )
        assertFuzzySearchEqual(
            listOf(
                1f to Range(78, 154),
            ), TextUtils.fuzzySearch("contractions", text)
        )
    }

    private fun assertFuzzySearchEqual(
        expected: List<Pair<Float, Range<Int>>>,
        actual: List<Pair<Float, Range<Int>>>
    ) {
        assertEquals(expected.size, actual.size)
        for (i in expected.indices) {
            assertEquals(expected[i].first, actual[i].first, 0.001f)
            assertEquals(expected[i].second.start, actual[i].second.start)
            assertEquals(expected[i].second.end, actual[i].second.end)
        }
    }

    @Test
    fun getSections() {
        val text = """
            Start
            # Section 1
            This is section 1
            ## Section 1.1
            This is section 1.1
            ## Section 1.2
            This is section 1.2
            # Section 2
            This is section 2
        """.trimIndent()
        val sections = TextUtils.getSections(text)

        assertEquals(
            listOf(
                TextUtils.TextSection(null, null, "Start"),
                TextUtils.TextSection("Section 1", 1, "This is section 1"),
                TextUtils.TextSection("Section 1.1", 2, "This is section 1.1"),
                TextUtils.TextSection("Section 1.2", 2, "This is section 1.2"),
                TextUtils.TextSection("Section 2", 1, "This is section 2")
            ),
            sections
        )
    }

    @Test
    fun groupSectionsLevel1() {
        val sections = listOf(
            TextUtils.TextSection(null, null, "Start"),
            TextUtils.TextSection("Section 1", 1, "This is section 1"),
            TextUtils.TextSection("Section 1.1", 2, "This is section 1.1"),
            TextUtils.TextSection(null, null, "Value"),
            TextUtils.TextSection("Section 1.2", 2, "This is section 1.2"),
            TextUtils.TextSection("Section 2", 1, "This is section 2")
        )

        val grouped = TextUtils.groupSections(sections, 1)

        assertEquals(
            listOf(
                listOf(
                    TextUtils.TextSection(null, null, "Start"),
                ),
                listOf(
                    TextUtils.TextSection("Section 1", 1, "This is section 1"),
                    TextUtils.TextSection("Section 1.1", 2, "This is section 1.1"),
                    TextUtils.TextSection(null, null, "Value"),
                    TextUtils.TextSection("Section 1.2", 2, "This is section 1.2"),
                ),
                listOf(
                    TextUtils.TextSection("Section 2", 1, "This is section 2")
                )
            ),
            grouped
        )

    }

    @Test
    fun groupSectionsLevel2() {
        val sections = listOf(
            TextUtils.TextSection(null, null, "Start"),
            TextUtils.TextSection("Section 1", 1, "This is section 1"),
            TextUtils.TextSection("Section 1.1", 2, "This is section 1.1"),
            TextUtils.TextSection(null, null, "Value"),
            TextUtils.TextSection("Section 1.2", 2, "This is section 1.2"),
            TextUtils.TextSection("Section 2", 1, "This is section 2")
        )

        val grouped = TextUtils.groupSections(sections, 2)

        assertEquals(
            listOf(
                listOf(
                    TextUtils.TextSection(null, null, "Start"),
                ),
                listOf(
                    TextUtils.TextSection("Section 1", 1, "This is section 1")
                ),
                listOf(
                    TextUtils.TextSection("Section 1.1", 2, "This is section 1.1"),
                    TextUtils.TextSection(null, null, "Value")
                ),
                listOf(
                    TextUtils.TextSection("Section 1.2", 2, "This is section 1.2"),
                ),
                listOf(
                    TextUtils.TextSection("Section 2", 1, "This is section 2")
                )
            ),
            grouped
        )

    }

    @Test
    fun groupSectionsAnyLevel() {
        val sections = listOf(
            TextUtils.TextSection(null, null, "Start"),
            TextUtils.TextSection("Section 1", 1, "This is section 1"),
            TextUtils.TextSection("Section 1.1", 2, "This is section 1.1"),
            TextUtils.TextSection(null, null, "Value"),
            TextUtils.TextSection("Section 1.2", 2, "This is section 1.2"),
            TextUtils.TextSection("Section 2", 1, "This is section 2")
        )

        val grouped = TextUtils.groupSections(sections, null)

        assertEquals(
            listOf(
                listOf(
                    TextUtils.TextSection(null, null, "Start"),
                ),
                listOf(
                    TextUtils.TextSection("Section 1", 1, "This is section 1"),
                    TextUtils.TextSection("Section 1.1", 2, "This is section 1.1"),
                    TextUtils.TextSection(null, null, "Value"),
                    TextUtils.TextSection("Section 1.2", 2, "This is section 1.2"),
                ),
                listOf(
                    TextUtils.TextSection("Section 2", 1, "This is section 2")
                )
            ),
            grouped
        )

    }

    @Test
    fun toMarkdown() {
        assertEquals("Test", TextUtils.TextSection(null, null, "Test").toMarkdown())
        assertEquals("# Test\nText", TextUtils.TextSection("Test", 1, "Text").toMarkdown())
        assertEquals("## Test\nText", TextUtils.TextSection("Test", 2, "Text").toMarkdown())
        assertEquals(
            "### Test\nText\nOther",
            TextUtils.TextSection("Test", 3, "Text\nOther").toMarkdown()
        )
    }

}