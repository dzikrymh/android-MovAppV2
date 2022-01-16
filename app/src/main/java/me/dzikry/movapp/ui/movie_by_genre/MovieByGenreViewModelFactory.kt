package me.dzikry.movapp.ui.movie_by_genre

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import me.dzikry.movapp.data.networks.MovieAPIs

class MovieByGenreViewModelFactory(private val api: MovieAPIs) : ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return MovieByGenreViewModel(api) as T
    }
}