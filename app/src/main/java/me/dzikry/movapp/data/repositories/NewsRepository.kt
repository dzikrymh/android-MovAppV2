package me.dzikry.movapp.data.repositories

import me.dzikry.movapp.data.models.response.NewsResponse
import me.dzikry.movapp.data.networks.NewsAPIs
import me.dzikry.movapp.utils.Const
import okio.IOException

class NewsRepository(private val api: NewsAPIs) {

    suspend fun getTopHeadlines(category: String, page: Int): NewsResponse {
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

    suspend fun getSearchNews(keyword: String, page: Int): NewsResponse {
        val response = api.getSearchNews(
            api_key = Const.API_KEY_NEWS,
            keyword = keyword,
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

}