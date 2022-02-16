package me.dzikry.movapp.ui.home.favorite

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
class FavoriteViewModel @Inject constructor(
    private val repository: MovieRepository
) : BaseViewModel() {
    private lateinit var _movieFavoriteFlow: Flow<PagingData<Movie>>
    val movieFavoriteFlow: Flow<PagingData<Movie>> get() = _movieFavoriteFlow

    fun getDiscoverFavoriteMovie(account_id: Int, session_id: String) = launchPagingAsync({
        repository.getFavoriteMovies(
            account_id = account_id,
            session_id = session_id,
        ).cachedIn(viewModelScope)
    }, {
        _movieFavoriteFlow = it
    })
}