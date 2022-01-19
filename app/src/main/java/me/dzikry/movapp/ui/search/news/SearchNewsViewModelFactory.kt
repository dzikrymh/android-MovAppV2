package me.dzikry.movapp.ui.search.news

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import me.dzikry.movapp.data.networks.NewsAPIs

class SearchNewsViewModelFactory(private val api: NewsAPIs) : ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return SearchNewsViewModel(api) as T
    }
}