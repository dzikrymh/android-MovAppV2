package me.dzikry.movapp.ui.auth

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import me.dzikry.movapp.data.models.Account
import me.dzikry.movapp.data.models.response.RequestTokenResponse
import me.dzikry.movapp.data.models.response.SessionIDResponse
import me.dzikry.movapp.data.models.response.SessionLogout
import me.dzikry.movapp.data.repositories.AuthRepository
import me.dzikry.movapp.utils.Resource
import okio.IOException
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val repository: AuthRepository,
) : ViewModel() {
    private val _reqToken = MutableLiveData<Resource<RequestTokenResponse>>()
    val reqToken: LiveData<Resource<RequestTokenResponse>> get() = _reqToken
    private val _validateLogin = MutableLiveData<Resource<RequestTokenResponse>>()
    val validateLogin: LiveData<Resource<RequestTokenResponse>> get() = _validateLogin
    private val _sessionIDState = MutableLiveData<Resource<SessionIDResponse>>()
    val sessionIDState: LiveData<Resource<SessionIDResponse>> get() = _sessionIDState
    private val _sessionLogout = MutableLiveData<Resource<SessionLogout>>()
    val sessionLogout: LiveData<Resource<SessionLogout>> get() = _sessionLogout
    private val _account = MutableLiveData<Resource<Account>>()
    val account: LiveData<Resource<Account>> get() = _account

    private val _sessionID = MutableLiveData<String>()
    val sessionID: LiveData<String> get() = _sessionID
    fun setSessionID(session_id: String) {
        _sessionID.postValue(session_id)
    }

    fun requestToken() = viewModelScope.launch {
        _reqToken.postValue(Resource.Loading())
        try {
            val response = repository.requestToken()
            _reqToken.postValue(Resource.Success(response))
        } catch (e: IOException) {
            _reqToken.postValue(Resource.Error(e.message))
        }
    }

    fun validateLogin(username: String, password: String, request_token: String) = viewModelScope.launch {
        _validateLogin.postValue(Resource.Loading())
        try {
            val response = repository.validateLogin(username, password, request_token)
            _validateLogin.postValue(Resource.Success(response))
        } catch (e: IOException) {
            _validateLogin.postValue(Resource.Error(e.message))
        }
    }

    fun requestSessionID(request_token: String) = viewModelScope.launch {
        _sessionIDState.postValue(Resource.Loading())
        try {
            val response = repository.createSessionID(request_token)
            _sessionIDState.postValue(Resource.Success(response))
            _sessionID.postValue(response.session_id)
        } catch (e: IOException) {
            _sessionIDState.postValue(Resource.Error(e.message))
            _sessionID.postValue("")
        }
    }

    fun sessionLogout(session_id: String) = viewModelScope.launch {
        _sessionLogout.postValue(Resource.Loading())
        try {
            val response = repository.sessionLogout(session_id)
            _sessionLogout.postValue(Resource.Success(response))
            _sessionID.postValue("")
        } catch (e: IOException) {
            _sessionLogout.postValue(Resource.Error(e.message))
        }
    }

    fun accountDetail(session_id: String) = viewModelScope.launch {
        _account.postValue(Resource.Loading())
        try {
            val response = repository.getAccountDetail(session_id)
            _account.postValue(Resource.Success(response))
        } catch (e: IOException) {
            _account.postValue(Resource.Error(e.message))
        }
    }
}