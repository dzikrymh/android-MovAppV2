package me.dzikry.movapp.ui.home.movie

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import me.dzikry.movapp.data.repositories.MovieRepository

class MovieViewModelFactory(private val repository: MovieRepository) : ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return MovieViewModel(repository) as T
    }
}