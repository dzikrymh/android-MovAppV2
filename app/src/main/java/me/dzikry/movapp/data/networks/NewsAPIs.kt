package me.dzikry.movapp.data.networks

import me.dzikry.movapp.data.models.response.NewsResponse
import me.dzikry.movapp.utils.Const
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import java.util.concurrent.TimeUnit

interface NewsAPIs {

    @GET("top-headlines")
    suspend fun getTopHeadlines(
        @Query("apiKey") api_key: String,
        @Query("country") country: String,
        @Query("category") category: String,
        @Query("language") language: String,
        @Query("page") page: Int,
        @Query("pageSize") pageSize: Int,
    ): Response<NewsResponse>

    @GET("everything")
    suspend fun getSearchNews(
        @Query("apiKey") api_key: String,
        @Query("q") keyword: String,
        @Query("language") language: String,
        @Query("page") page: Int,
        @Query("pageSize") pageSize: Int,
    ): Response<NewsResponse>

}