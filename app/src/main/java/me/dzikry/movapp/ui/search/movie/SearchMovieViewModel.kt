package me.dzikry.movapp.ui.search.movie

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
class SearchMovieViewModel @Inject constructor(
    private val repository: MovieRepository,
) : BaseViewModel() {
    private lateinit var _movieFlow: Flow<PagingData<Movie>>
    val movieFlow: Flow<PagingData<Movie>> get() = _movieFlow

    fun getSearchMovie(keyword: String) = launchPagingAsync({
        repository.getSearchMovie(keyword = keyword).cachedIn(viewModelScope)
    }, {
        _movieFlow = it
    })

}