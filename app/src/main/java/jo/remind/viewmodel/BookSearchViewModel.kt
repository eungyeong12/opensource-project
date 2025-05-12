package jo.remind.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import jo.remind.BuildConfig
import jo.remind.data.model.book.Book
import jo.remind.data.repository.BookRepository
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BookSearchViewModel @Inject constructor(
    private val bookRepository: BookRepository
) : ViewModel() {
    var isLoading by mutableStateOf(false)
        private set
    var books by mutableStateOf<List<Book>>(emptyList())
        private set

    fun searchBooks(query: String) {
        viewModelScope.launch {
            isLoading = true
            try {
                books = bookRepository.getOrFetchBooks(
                    query = query,
                    apiKey = "KakaoAK ${BuildConfig.BOOK_API_KEY}"
                )
            } catch (e: Exception) {
                e.printStackTrace()
                books = emptyList()
            } finally {
                isLoading = false
            }
        }
    }
}

