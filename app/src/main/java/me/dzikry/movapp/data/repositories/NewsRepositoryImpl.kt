package me.dzikry.movapp.data.repositories

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import me.dzikry.movapp.data.models.Article
import me.dzikry.movapp.data.models.response.NewsResponse
import me.dzikry.movapp.data.networks.NewsAPIs
import me.dzikry.movapp.data.paging.datasources.SearchNewsPagingSource
import me.dzikry.movapp.utils.Const
import okio.IOException
import javax.inject.Inject

class NewsRepositoryImpl @Inject constructor(
    private val api: NewsAPIs,
) : NewsRepository {

    override suspend fun getTopHeadlines(category: String, page: Int): NewsResponse {
        val response = api.getTopHeadlines(
            api_key = Const.API_KEY_NEWS,
            country = "id",
            category = category,
            language = "id",
            page = page,
            pageSize = 20,
        )
        if (response.isSuccessful) {
            return response.body()!!
        } else {
            throw IOException("Gagal mengambil data")
        }
    }

    override suspend fun getSearchNews(keyword: String): Flow<PagingData<Article>> = Pager(
        config = PagingConfig(
            pageSize = 20,
            maxSize = 200
        ),
        pagingSourceFactory = {
            SearchNewsPagingSource(api, keyword)
        }
    ).flow
}