package me.dzikry.movapp.ui.movie_detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import me.dzikry.movapp.data.models.MovieDetail
import me.dzikry.movapp.data.models.Review
import me.dzikry.movapp.data.models.Trailer
import me.dzikry.movapp.data.repositories.MovieRepository
import me.dzikry.movapp.utils.BaseViewModel
import me.dzikry.movapp.utils.Resource
import okio.IOException
import javax.inject.Inject

@HiltViewModel
class MovieDetailViewModel @Inject constructor(
    private val repository: MovieRepository,
) : BaseViewModel() {
    private val _movie = MutableLiveData<Resource<MovieDetail>>()
    private val _trailer = MutableLiveData<Resource<List<Trailer>>>()
    private lateinit var _reviewFlow: Flow<PagingData<Review>>
    val reviewFlow: Flow<PagingData<Review>> get() = _reviewFlow
    val movie: LiveData<Resource<MovieDetail>> get() = _movie
    val trailer: LiveData<Resource<List<Trailer>>> get() = _trailer

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

    fun getReviewPaging(movie_id: String) = launchPagingAsync({
        repository.getReviews(movie_id).cachedIn(viewModelScope)
    }, {
        _reviewFlow = it
    })
}