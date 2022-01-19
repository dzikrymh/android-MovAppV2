package me.dzikry.movapp.ui.home.news

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import me.dzikry.movapp.data.repositories.NewsRepository

class NewsViewModelFactory(
    private val repository: NewsRepository
) : ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return NewsViewModel(repository) as T
    }
}