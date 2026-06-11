package com.edu.app.data.search

object FtsQuerySanitizer {
    private val tashkeel = Regex("[\\u064B-\\u0652\\u0670]")

    private fun normalize(token: String): String = token
        .replace(tashkeel, "")
        .replace('أ', 'ا').replace('إ', 'ا').replace('آ', 'ا')
        .replace('ى', 'ي')
        .replace('ة', 'ه')

    fun build(raw: String): String? {
        val cleaned = raw.trim()
        if (cleaned.length < 2) return null
        val terms = cleaned.split(Regex("\\s+"))
            .map { normalize(it) }
            .map { it.replace(Regex("[\"*:()\\-^]"), "") }
            .filter { it.isNotBlank() }
        if (terms.isEmpty()) return null
        return terms.joinToString(" AND ") { "$it*" }
    }
}
