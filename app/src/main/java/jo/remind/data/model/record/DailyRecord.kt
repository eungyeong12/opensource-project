package jo.remind.data.model.record

data class DailyRecord(
    val date: String = "",
    val memo: String = "",
    val imageUrls: List<String> = emptyList(),
    val uploadedAt: String = ""
)
