package me.dzikry.movapp.data.networks

import me.dzikry.movapp.data.models.MovieDetail
import me.dzikry.movapp.data.models.response.*
import retrofit2.Response
import retrofit2.http.*

interface MovieAPIs {

    @GET("search/movie")
    suspend fun getSearchMovie(
        @Query("api_key") api_key: String,
        @Query("page") page: Int,
        @Query("query") query: String,
    ): Response<MovieResponse>

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

    @GET("account/{account_id}/favorite/movies")
    suspend fun getFavoriteMovies(
        @Path("account_id") account_id: Int,
        @Query("api_key") api_key: String,
        @Query("session_id") session_id: String,
        @Query("sort_by") sort_by: String = "created_at.desc",
        @Query("page") page: Int,
    ): Response<MovieResponse>

    @FormUrlEncoded
    @POST("account/{account_id}/favorite")
    suspend fun markAsFavoriteMovie(
        @Path("account_id") account_id: Int,
        @Query("api_key") api_key: String,
        @Query("session_id") session_id: String,
        @Field("media_type") media_type: String = "movie",
        @Field("media_id") movie_id: String,
        @Field("favorite") favorite: Boolean,
    ): Response<BaseMovieResponse>

    @GET("movie/{movie_id}/account_states")
    suspend fun getAccountStatesMovie(
        @Path("movie_id") movie_id: String,
        @Query("api_key") api_key: String,
        @Query("session_id") session_id: String,
    ): Response<AccountStateMovieResponse>

}