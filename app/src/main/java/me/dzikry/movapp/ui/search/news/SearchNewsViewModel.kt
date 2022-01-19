package me.dzikry.movapp.ui.search.news

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import kotlinx.coroutines.flow.Flow
import me.dzikry.movapp.data.models.Article
import me.dzikry.movapp.data.networks.NewsAPIs
import me.dzikry.movapp.data.paging.datasources.SearchNewsPagingSource

class SearchNewsViewModel(private val api: NewsAPIs) : ViewModel() {

    fun getSearchNews(keyword: String): Flow<PagingData<Article>> {
        return Pager (config = PagingConfig(pageSize = SearchNewsPagingSource.PAGE_SIZE_NEWS, maxSize = 200),
            pagingSourceFactory = { SearchNewsPagingSource(api, keyword) }).flow.cachedIn(viewModelScope)
    }

}