package me.dzikry.movapp.ui.search.news

import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import me.dzikry.movapp.data.models.Article
import me.dzikry.movapp.data.repositories.NewsRepository
import me.dzikry.movapp.utils.BaseViewModel
import javax.inject.Inject

@HiltViewModel
class SearchNewsViewModel @Inject constructor(
    private val repository: NewsRepository,
) : BaseViewModel() {
    private lateinit var _newsFlow: Flow<PagingData<Article>>
    val newsFlow: Flow<PagingData<Article>> get() = _newsFlow

    fun getSearchNews(keyword: String) = launchPagingAsync({
        repository.getSearchNews(keyword).cachedIn(viewModelScope)
    }, {
        _newsFlow = it
    })

}