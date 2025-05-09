package jo.remind.data.model

data class Movie(
    val id: Int,
    val title: String,
    val release_date: String,
    val poster_path: String?,
    val genre_ids: List<Int> = emptyList()
)