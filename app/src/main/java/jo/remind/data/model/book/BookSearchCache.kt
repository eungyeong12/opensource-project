package jo.remind.data.model.book

data class BookSearchCache(
    val query: String = "",
    val results: List<Book> = emptyList(),
    val timestamp: Long = System.currentTimeMillis()
)