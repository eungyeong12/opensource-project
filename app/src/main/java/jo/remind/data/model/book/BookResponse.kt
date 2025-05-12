package jo.remind.data.model.book

data class BookResponse(
    val documents: List<Book>,
    val meta: Meta
)