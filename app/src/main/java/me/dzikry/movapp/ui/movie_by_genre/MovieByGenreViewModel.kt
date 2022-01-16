package me.dzikry.movapp.ui.movie_by_genre

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import kotlinx.coroutines.flow.Flow
import me.dzikry.movapp.data.models.Movie
import me.dzikry.movapp.data.networks.MovieAPIs
import me.dzikry.movapp.data.paging.datasources.DiscoverMovieByGenrePagingSource

class MovieByGenreViewModel(private val api: MovieAPIs) : ViewModel() {

    fun getDiscoverMovieByGenre(genre_id: String): Flow<PagingData<Movie>> {
        return Pager(
            config = PagingConfig(
                pageSize = 20,
                maxSize = 200
            ),
            pagingSourceFactory = {
                DiscoverMovieByGenrePagingSource(api, genre_id)
            }
        ).flow.cachedIn(viewModelScope)
    }

}