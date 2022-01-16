package me.dzikry.movapp.ui.movie_detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import me.dzikry.movapp.data.repositories.MovieRepository

class MovieDetailViewModelFactory(private val repository: MovieRepository) : ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return MovieDetailViewModel(repository) as T
    }
}