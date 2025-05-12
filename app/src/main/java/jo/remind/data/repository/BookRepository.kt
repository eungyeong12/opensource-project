package jo.remind.data.repository

import com.google.firebase.firestore.FirebaseFirestore
import jo.remind.data.model.book.Book
import jo.remind.data.model.book.BookSearchCache
import jo.remind.data.network.RetrofitClient
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class BookRepository @Inject constructor(
    private val firestore: FirebaseFirestore
) {
    suspend fun getOrFetchBooks(query: String, apiKey: String): List<Book> {
        val docRef = firestore.collection("book_searches").document(query)
        val snapshot = docRef.get().await()

        return if (snapshot.exists()) {
            val cache = snapshot.toObject(BookSearchCache::class.java)
            android.util.Log.d("BookRepo", "Loaded from Firestore: ${cache?.results?.size} items")
            cache?.results ?: emptyList()
        } else {
            val response = RetrofitClient.bookApiService.searchBooks(
                apiKey = apiKey,
                query = query
            )
            val books = response.documents
            val cache = BookSearchCache(query = query, results = books)
            docRef.set(cache).await()
            books
        }
    }
}
