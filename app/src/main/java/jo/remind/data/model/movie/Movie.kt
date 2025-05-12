package jo.remind.data.model.movie

data class Movie(
    val id: Int = 0,
    val title: String = "",
    val release_date: String = "",
    val poster_path: String? = null,
    val genre_ids: List<Int> = emptyList()
)