package me.dzikry.movapp.data.repositories

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import me.dzikry.movapp.data.models.Movie
import me.dzikry.movapp.data.models.MovieDetail
import me.dzikry.movapp.data.models.Review
import me.dzikry.movapp.data.models.response.*

interface MovieRepository {
    suspend fun getPopular(page: Int) : MovieResponse
    suspend fun getTopRated(page: Int) : MovieResponse
    suspend fun getUpcoming(page: Int) : MovieResponse
    suspend fun getGenre() : GenreResponse
    suspend fun getMovie(movie_id: String) : MovieDetail
    suspend fun getTrailer(movie_id: String) : TrailerResponse
    suspend fun getReviews(movie_id: String) : Flow<PagingData<Review>>
    suspend fun getDiscoverMovieByGenre(genre_id: String): Flow<PagingData<Movie>>
    suspend fun getSearchMovie(keyword: String): Flow<PagingData<Movie>>
    suspend fun getFavoriteMovies(account_id: Int, session_id: String) : Flow<PagingData<Movie>>
    suspend fun markAsFavoriteMovie(account_id: Int, session_id: String, movie_id: String, favorite: Boolean) : BaseMovieResponse
    suspend fun getAccountStatesMovie(session_id: String, movie_id: String) : AccountStateMovieResponse
}