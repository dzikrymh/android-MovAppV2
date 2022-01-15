package me.dzikry.movapp.data.repositories

import me.dzikry.movapp.data.models.MovieDetail
import me.dzikry.movapp.data.models.response.GenreResponse
import me.dzikry.movapp.data.models.response.MovieResponse
import me.dzikry.movapp.data.models.response.ReviewResponse
import me.dzikry.movapp.data.models.response.TrailerResponse
import me.dzikry.movapp.data.networks.MovieAPIs
import me.dzikry.movapp.utils.Const
import okio.IOException

class MovieRepository(private val api: MovieAPIs) {

    suspend fun getPopular(page: Int) : MovieResponse {
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

    suspend fun getTopRated(page: Int) : MovieResponse {
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

    suspend fun getUpcoming(page: Int) : MovieResponse {
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

    suspend fun getGenre() : GenreResponse {
        val response = api.getGenreMovie(
            api_key = Const.API_KEY_MOVIE,
        )
        if (response.isSuccessful) {
            return response.body()!!
        } else {
            throw IOException("Gagal mengambil data")
        }
    }

    suspend fun getMovieByGenre(genre: String, page: Int) : MovieResponse {
        val response = api.getMovieByGenre(
            api_key = Const.API_KEY_MOVIE,
            page = page,
            genre_id = genre
        )
        if (response.isSuccessful) {
            return response.body()!!
        } else {
            throw IOException("Gagal mengambil data")
        }
    }

    suspend fun getMovie(movie_id: String) : MovieDetail {
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

    suspend fun getTrailer(movie_id: String) : TrailerResponse {
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

    suspend fun getReviews(movie_id: String, page: Int) : ReviewResponse {
        val response = api.getReviewMovie(
            api_key = Const.API_KEY_MOVIE,
            movie_id = movie_id,
            page = page
        )
        if (response.isSuccessful) {
            return response.body()!!
        } else {
            throw IOException("Gagal mengambil data")
        }
    }

}