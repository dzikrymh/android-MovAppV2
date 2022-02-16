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
import me.dzikry.movapp.data.models.response.AccountStateMovieResponse
import me.dzikry.movapp.data.models.response.BaseMovieResponse
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
    private val _accountState = MutableLiveData<Resource<AccountStateMovieResponse>>()
    private val _markAsFavorite = MutableLiveData<Resource<BaseMovieResponse>>()
    private val _trailer = MutableLiveData<Resource<List<Trailer>>>()
    private lateinit var _reviewFlow: Flow<PagingData<Review>>
    val reviewFlow: Flow<PagingData<Review>> get() = _reviewFlow
    val movie: LiveData<Resource<MovieDetail>> get() = _movie
    val markAsFavorite: LiveData<Resource<BaseMovieResponse>> get() = _markAsFavorite
    val accountState: LiveData<Resource<AccountStateMovieResponse>> get() = _accountState
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

    fun getAccountState(session_id: String, movie_id: String) = viewModelScope.launch {
        _accountState.postValue(Resource.Loading())
        try {
            val response = repository.getAccountStatesMovie(
                session_id = session_id,
                movie_id = movie_id,
            )
            _accountState.postValue(Resource.Success(response))
        } catch (e: IOException) {
            _accountState.postValue(Resource.Error(e.message))
        }
    }

    fun markAsFavorite(
        account_id: Int,
        session_id: String,
        movie_id: String,
        fav: Boolean,
    ) = viewModelScope.launch {
        _markAsFavorite.postValue(Resource.Loading())
        try {
            val response = repository.markAsFavoriteMovie(
                account_id = account_id,
                session_id = session_id,
                movie_id = movie_id,
                favorite = fav,
            )
            _markAsFavorite.postValue(Resource.Success(response))
        } catch (e: IOException) {
            _markAsFavorite.postValue(Resource.Error(e.message))
        }
    }
}