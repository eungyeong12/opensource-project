package jo.remind.data.model.record

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class MovieRecord(
    val date: String = "",
    val title: String = "",
    val releaseDate: String = "",
    val imageUrl: String = "",
    val genre: String = "",
    val director: String = "",
    val memo: String = "",
    val rating: Float = 5.0f,
    val uploadedAt: String = ""
) : Parcelable
