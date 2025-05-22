package jo.remind.data.model.record

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class BookRecord(
    val date: String = "",
    val title: String = "",
    val writer: String = "",
    val publishDate: String = "",
    val imageUrl: String? = "",
    val memo: String = "",
    val rating: Float = 5.0f,
    val uploadedAt: String = ""
) : Parcelable