package jo.remind.data.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    private const val MOVIE_BASE_URL = "https://api.themoviedb.org/3/"
    private const val BOOK_BASE_URL = "https://dapi.kakao.com/"

    val movieApiService: MovieApiService by lazy {
        Retrofit.Builder()
            .baseUrl(MOVIE_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(MovieApiService::class.java)
    }

    val bookApiService: BookApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BOOK_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(BookApiService::class.java)
    }
}