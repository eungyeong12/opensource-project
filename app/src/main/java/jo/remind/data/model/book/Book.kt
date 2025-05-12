package jo.remind.data.model.book

data class Book(
    val title: String = "",
    val authors: List<String> = emptyList(),
    val thumbnail: String? = null,
    val datetime: String = "",
    val publisher: String = "",
    val isbn: String = ""
)