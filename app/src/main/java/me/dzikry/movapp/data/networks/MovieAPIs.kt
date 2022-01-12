package me.dzikry.movapp.data.networks

import me.dzikry.movapp.data.models.MovieDetail
import me.dzikry.movapp.data.models.response.GenreResponse
import me.dzikry.movapp.data.models.response.MovieResponse
import me.dzikry.movapp.data.models.response.ReviewResponse
import me.dzikry.movapp.data.models.response.TrailerResponse
import me.dzikry.movapp.utils.Const
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import java.util.concurrent.TimeUnit

interface MovieAPIs {

    @GET("movie/popular")
    suspend fun getPopularMovie(
        @Query("api_key") api_key: String,
        @Query("page") page: Int,
    ): Response<MovieResponse>

    @GET("movie/top_rated")
    suspend fun getTopRatedMovie(
        @Query("api_key") api_key: String,
        @Query("page") page: Int,
    ): Response<MovieResponse>

    @GET("movie/upcoming")
    suspend fun getUpcomingMovie(
        @Query("api_key") api_key: String,
        @Query("page") page: Int,
    ): Response<MovieResponse>

    @GET("genre/movie/list")
    suspend fun getGenreMovie(
        @Query("api_key") api_key: String,
    ): Response<GenreResponse>

    @GET("discover/movie")
    suspend fun getMovieByGenre(
        @Query("api_key") api_key: String,
        @Query("page") page: Int,
        @Query("with_genres") genre_id: String,
    ): Response<MovieResponse>

    @GET("movie/{movie_id}")
    suspend fun getMovieDetail(
        @Path("movie_id") movie_id: String,
        @Query("api_key") api_key: String,
    ): Response<MovieDetail>

    @GET("movie/{movie_id}/videos")
    suspend fun getTrailerLink(
        @Path("movie_id") movie_id: String,
        @Query("api_key") api_key: String,
    ): Response<TrailerResponse>

    @GET("movie/{movie_id}/reviews")
    suspend fun getReviewMovie(
        @Path("movie_id") movie_id: String,
        @Query("api_key") api_key: String,
        @Query("page") page: Int,
    ): Response<ReviewResponse>

    companion object {
        operator fun invoke() : MovieAPIs {
            val logging = HttpLoggingInterceptor()
            logging.level = HttpLoggingInterceptor.Level.BODY
            val okHttpClient = OkHttpClient.Builder()
                .addInterceptor(logging)
                .readTimeout(90, TimeUnit.SECONDS)
                .connectTimeout(90, TimeUnit.SECONDS)
                .build()

            return Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl(Const.BASE_URL_MOVIE)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(MovieAPIs::class.java)
        }
    }
}