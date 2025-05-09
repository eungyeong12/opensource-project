package jo.remind.viewmodel

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import jo.remind.BuildConfig
import jo.remind.data.model.Movie
import jo.remind.data.network.RetrofitClient
import kotlinx.coroutines.launch
import javax.inject.Inject

private val genreMap = mapOf(
    28 to "액션",
    12 to "어드벤처",
    16 to "애니메이션",
    35 to "코미디",
    80 to "범죄",
    99 to "다큐",
    18 to "드라마",
    10751 to "가족",
    14 to "판타지",
    36 to "역사",
    27 to "공포",
    10402 to "음악",
    9648 to "미스터리",
    10749 to "로맨스",
    878 to "SF",
    10770 to "TV 영화",
    53 to "스릴러",
    10752 to "전쟁",
    37 to "서부"
)

@HiltViewModel
class MovieSearchViewModel @Inject constructor() : ViewModel() {

    var movieList by mutableStateOf<List<Movie>>(emptyList())
        private set

    var isLoading by mutableStateOf(false)
        private set

    fun searchMovies(query: String) {
        viewModelScope.launch {
            try {
                isLoading = true
                val response = RetrofitClient.apiService.searchMovies(query, BuildConfig.API_KEY)
                movieList = response.results
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                isLoading = false
            }
        }
    }

    fun mapGenreIdsToNames(genreIds: List<Int>): String {
        return genreIds.map { genreMap[it] ?: "기타" }.distinct().joinToString(", ")
    }
}
