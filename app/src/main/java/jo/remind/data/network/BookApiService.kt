package jo.remind.data.network

import jo.remind.data.model.book.BookResponse
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface BookApiService {

    @GET("v3/search/book")
    suspend fun searchBooks(
        @Header("Authorization") apiKey: String,
        @Query("query") query: String,
        @Query("size") size: Int = 20
    ): BookResponse
}