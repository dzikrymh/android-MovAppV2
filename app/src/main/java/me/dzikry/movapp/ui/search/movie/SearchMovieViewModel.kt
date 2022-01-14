package me.dzikry.movapp.ui.search.movie

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import kotlinx.coroutines.flow.Flow
import me.dzikry.movapp.data.models.Movie
import me.dzikry.movapp.data.networks.MovieAPIs

class SearchMovieViewModel(private val api: MovieAPIs) : ViewModel() {

    fun getSearchMovie(keyword: String): Flow<PagingData<Movie>> {
        return Pager (config = PagingConfig(pageSize = 20, maxSize = 200),
            pagingSourceFactory = {SearchMoviePagingSource(api, keyword)}).flow.cachedIn(viewModelScope)
    }

}