package me.dzikry.movapp.ui.home.news

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import me.dzikry.movapp.data.models.Article
import me.dzikry.movapp.data.repositories.NewsRepository
import me.dzikry.movapp.utils.Resource
import java.io.IOException

class NewsViewModel(private val repository: NewsRepository) : ViewModel() {
    private val _headlineNews = MutableLiveData<Resource<List<Article>>>()
    private val _trendingNews = MutableLiveData<Resource<List<Article>>>()
    val headlineNews: LiveData<Resource<List<Article>>> get() = _headlineNews
    val trendingNews: LiveData<Resource<List<Article>>> get() = _trendingNews

    fun clearHeadlinesNews() {
        _headlineNews.postValue(Resource.Loading())
    }

    fun getHeadlinesNews(category: String, page:Int) = viewModelScope.launch {
        _headlineNews.postValue(Resource.Loading())
        try {
            val response = repository.getTopHeadlines(category, page)
            _headlineNews.postValue(Resource.Success(response.articles))
        } catch (e: IOException) {
            _headlineNews.postValue(Resource.Error(e.message))
        }
    }

    fun getTrendingNews(page:Int) = viewModelScope.launch {
        _trendingNews.postValue(Resource.Loading())
        try {
            val response = repository.getTopHeadlines("", page)
            _trendingNews.postValue(Resource.Success(response.articles))
        } catch (e: IOException) {
            _trendingNews.postValue(Resource.Error(e.message))
        }
    }
}