package jo.remind.data.repository

import com.google.firebase.firestore.FirebaseFirestore
import jo.remind.BuildConfig
import jo.remind.data.model.Movie
import jo.remind.data.model.MovieSearchCache
import jo.remind.data.network.RetrofitClient
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class MovieRepository @Inject constructor(
    private val firestore: FirebaseFirestore
) {
    suspend fun getOrFetchMovies(query: String): List<Movie> {
        val docRef = firestore.collection("movie_searches").document(query)
        val snapshot = docRef.get().await()

        return if (snapshot.exists()) {
            val cache = snapshot.toObject(MovieSearchCache::class.java)
            android.util.Log.d("MovieRepo", "Loaded from Firestore: ${cache?.results?.size} items")
            return cache?.results ?: emptyList()
        } else {
            val response = RetrofitClient.apiService.searchMovies(query, BuildConfig.API_KEY)
            val movies = response.results
            val cache = MovieSearchCache(query, movies)
            docRef.set(cache).await()
            movies
        }
    }
}
