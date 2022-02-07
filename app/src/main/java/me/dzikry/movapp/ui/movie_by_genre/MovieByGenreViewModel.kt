package me.dzikry.movapp.ui.movie_by_genre

import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import me.dzikry.movapp.data.models.Movie
import me.dzikry.movapp.data.repositories.MovieRepository
import me.dzikry.movapp.utils.BaseViewModel
import javax.inject.Inject

@HiltViewModel
class MovieByGenreViewModel @Inject constructor(
    private val repository: MovieRepository,
) : BaseViewModel() {
    private lateinit var _movieFlow: Flow<PagingData<Movie>>
    val movieFlow: Flow<PagingData<Movie>> get() = _movieFlow

    fun getDiscoverMovieByGenre(genre_id: String) = launchPagingAsync({
        repository.getDiscoverMovieByGenre(genre_id).cachedIn(viewModelScope)
    }, {
        _movieFlow = it
    })
}