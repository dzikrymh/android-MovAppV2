package me.dzikry.movapp.data.repositories

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import me.dzikry.movapp.data.models.Movie
import me.dzikry.movapp.data.models.MovieDetail
import me.dzikry.movapp.data.models.Review
import me.dzikry.movapp.data.models.response.*
import me.dzikry.movapp.data.networks.MovieAPIs
import me.dzikry.movapp.data.paging.datasources.DiscoverMovieByGenrePagingSource
import me.dzikry.movapp.data.paging.datasources.FavoriteMoviePagingSource
import me.dzikry.movapp.data.paging.datasources.ReviewMoviePagingSource
import me.dzikry.movapp.data.paging.datasources.SearchMoviePagingSource
import me.dzikry.movapp.utils.Const
import okio.IOException
import javax.inject.Inject

class MovieRepositoryImpl @Inject constructor(
    private val api: MovieAPIs,
) : MovieRepository {

    override suspend fun getPopular(page: Int) : MovieResponse {
        val response = api.getPopularMovie(
            api_key = Const.API_KEY_MOVIE,
            page = page
        )
        if (response.isSuccessful) {
            return response.body()!!
        } else {
            throw IOException("Gagal mengambil data")
        }
    }

    override suspend fun getTopRated(page: Int) : MovieResponse {
        val response = api.getTopRatedMovie(
            api_key = Const.API_KEY_MOVIE,
            page = page
        )
        if (response.isSuccessful) {
            return response.body()!!
        } else {
            throw IOException("Gagal mengambil data")
        }
    }

    override suspend fun getUpcoming(page: Int) : MovieResponse {
        val response = api.getUpcomingMovie(
            api_key = Const.API_KEY_MOVIE,
            page = page
        )
        if (response.isSuccessful) {
            return response.body()!!
        } else {
            throw IOException("Gagal mengambil data")
        }
    }

    override suspend fun getGenre() : GenreResponse {
        val response = api.getGenreMovie(
            api_key = Const.API_KEY_MOVIE,
        )
        if (response.isSuccessful) {
            return response.body()!!
        } else {
            throw IOException("Gagal mengambil data")
        }
    }

    override suspend fun getMovie(movie_id: String) : MovieDetail {
        val response = api.getMovieDetail(
            api_key = Const.API_KEY_MOVIE,
            movie_id = movie_id
        )
        if (response.isSuccessful) {
            return response.body()!!
        } else {
            throw IOException("Gagal mengambil data")
        }
    }

    override suspend fun getTrailer(movie_id: String) : TrailerResponse {
        val response = api.getTrailerLink(
            api_key = Const.API_KEY_MOVIE,
            movie_id = movie_id
        )
        if (response.isSuccessful) {
            return response.body()!!
        } else {
            throw IOException("Gagal mengambil data")
        }
    }

    override suspend fun getReviews(movie_id: String): Flow<PagingData<Review>> = Pager(
        config = PagingConfig(
            pageSize = 20,
            maxSize = 200
        ),
        pagingSourceFactory = {
            ReviewMoviePagingSource(api, movie_id)
        }
    ).flow

    override suspend fun getDiscoverMovieByGenre(genre_id: String): Flow<PagingData<Movie>> = Pager(
        config = PagingConfig(
            pageSize = 20,
            maxSize = 200
        ),
        pagingSourceFactory = {
            DiscoverMovieByGenrePagingSource(api, genre_id)
        }
    ).flow

    override suspend fun getSearchMovie(keyword: String): Flow<PagingData<Movie>> = Pager(
        config = PagingConfig(
            pageSize = 20,
            maxSize = 200
        ),
        pagingSourceFactory = {
            SearchMoviePagingSource(api, keyword)
        }
    ).flow

    override suspend fun getFavoriteMovies(
        account_id: Int,
        session_id: String
    ): Flow<PagingData<Movie>> = Pager(
        config = PagingConfig(
            pageSize = 20,
            maxSize = 200
        ),
        pagingSourceFactory = {
            FavoriteMoviePagingSource(api, account_id, session_id)
        }
    ).flow

    override suspend fun markAsFavoriteMovie(
        account_id: Int,
        session_id: String,
        movie_id: String,
        favorite: Boolean
    ): BaseMovieResponse {
        val response = api.markAsFavoriteMovie(
            account_id = account_id,
            api_key = Const.API_KEY_MOVIE,
            session_id = session_id,
            movie_id = movie_id,
            favorite = favorite
        )

        if (response.isSuccessful) {
            return response.body()!!
        } else {
            val gson = Gson()
            val errorResponse: BaseMovieResponse =
                gson.fromJson(response.errorBody()!!.charStream(), BaseMovieResponse::class.java)
            throw IOException(errorResponse.status_message)
        }
    }

    override suspend fun getAccountStatesMovie(
        session_id: String,
        movie_id: String
    ): AccountStateMovieResponse {
        val response = api.getAccountStatesMovie(
            movie_id = movie_id,
            api_key = Const.API_KEY_MOVIE,
            session_id = session_id
        )

        if (response.isSuccessful) {
            return response.body()!!
        } else {
            val gson = Gson()
            val errorResponse: BaseMovieResponse =
                gson.fromJson(response.errorBody()!!.charStream(), BaseMovieResponse::class.java)
            throw IOException(errorResponse.status_message)
        }
    }
}