package me.dzikry.movapp.ui.search.movie

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import me.dzikry.movapp.data.networks.MovieAPIs
import me.dzikry.movapp.data.repositories.MovieRepository

class SearchMovieViewModelFactory(private val api: MovieAPIs) : ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return SearchMovieViewModel(api) as T
    }
}