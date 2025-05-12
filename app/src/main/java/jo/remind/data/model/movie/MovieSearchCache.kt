package jo.remind.data.model.movie

data class MovieSearchCache(
    val query: String = "",
    val results: List<Movie> = emptyList(),
    val timestamp: Long = System.currentTimeMillis()
)
