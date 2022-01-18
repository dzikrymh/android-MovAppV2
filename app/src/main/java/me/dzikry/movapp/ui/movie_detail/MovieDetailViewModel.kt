package me.dzikry.movapp.ui.movie_detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import me.dzikry.movapp.data.models.MovieDetail
import me.dzikry.movapp.data.models.Review
import me.dzikry.movapp.data.models.Trailer
import me.dzikry.movapp.data.paging.datasources.ReviewMoviePagingSource
import me.dzikry.movapp.data.repositories.MovieRepository
import me.dzikry.movapp.utils.Resource
import okio.IOException

class MovieDetailViewModel(private val repository: MovieRepository) : ViewModel() {
    private val _movie = MutableLiveData<Resource<MovieDetail>>()
    private val _trailer = MutableLiveData<Resource<List<Trailer>>>()
    private val _reviews = MutableLiveData<Resource<List<Review>>>()
    val movie: LiveData<Resource<MovieDetail>> get() = _movie
    val trailer: LiveData<Resource<List<Trailer>>> get() = _trailer
    val reviews: LiveData<Resource<List<Review>>> get() = _reviews

    fun getMovie(movie_id: String) = viewModelScope.launch {
        _movie.postValue(Resource.Loading())
        try {
            val response = repository.getMovie(movie_id)
            _movie.postValue(Resource.Success(response))
        } catch (e: IOException) {
            _movie.postValue(Resource.Error(e.message))
        }
    }

    fun getTrailer(movie_id: String) = viewModelScope.launch {
        _trailer.postValue(Resource.Loading())
        try {
            val response = repository.getTrailer(movie_id)
            _trailer.postValue(Resource.Success(response.results))
        } catch (e: IOException) {
            _trailer.postValue(Resource.Error(e.message))
        }
    }

    fun getReviews(movie_id: String, page: Int) = viewModelScope.launch {
        _reviews.postValue(Resource.Loading())
        try {
            val response = repository.getReviews(movie_id, page)
            _reviews.postValue(Resource.Success(response.results))
        } catch (e: IOException) {
            _reviews.postValue(Resource.Error(e.message))
        }
    }

    fun getReviewPaging(movie_id: String) : Flow<PagingData<Review>> {
        return Pager(
            config = PagingConfig(
                pageSize = 20,
                maxSize = 200
            ),
            pagingSourceFactory = {
                ReviewMoviePagingSource(movie_id)
            }
        ).flow.cachedIn(viewModelScope)
    }
}