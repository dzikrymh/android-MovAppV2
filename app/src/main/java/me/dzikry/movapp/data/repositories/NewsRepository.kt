package me.dzikry.movapp.data.repositories

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import me.dzikry.movapp.data.models.Article
import me.dzikry.movapp.data.models.response.NewsResponse

interface NewsRepository {
    suspend fun getTopHeadlines(category: String, page: Int): NewsResponse
    suspend fun getSearchNews(keyword: String): Flow<PagingData<Article>>
}