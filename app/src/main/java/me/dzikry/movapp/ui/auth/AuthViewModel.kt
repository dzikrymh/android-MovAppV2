package me.dzikry.movapp.ui.auth

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import me.dzikry.movapp.data.models.User
import me.dzikry.movapp.data.models.response.Meta
import me.dzikry.movapp.data.repositories.AuthRepository
import me.dzikry.movapp.utils.Resource
import java.io.IOException
import java.lang.StringBuilder

class AuthViewModel(private val repository: AuthRepository) : ViewModel() {
    private val _meta = MutableLiveData<Resource<Meta>>()
    private val _user = MutableLiveData<Resource<User>>()
    private val _token = MutableLiveData<String>()
    val meta: LiveData<Resource<Meta>> get() = _meta
    val user: LiveData<Resource<User>> get() = _user
    val token: LiveData<String> get() = _token

    fun login(email: String, password: String) = viewModelScope.launch {
        _user.postValue(Resource.Loading())
        try {
            val response = repository.login(email, password)
            _user.postValue(Resource.Success(response.data.user))

            val token = StringBuilder()
            token.append(response.data.token_type)
            token.append(" ")
            token.append(response.data.access_token)

            _token.postValue(token.toString())
        } catch (e: IOException) {
            _user.postValue(Resource.Error(e.message))
            _token.postValue("")
        }
    }

    fun register(
        name: String,
        email: String,
        username: String,
        password: String,
        phone: String
    ) = viewModelScope.launch {
        _user.postValue(Resource.Loading())
        try {
            val response = repository.register(name, email, username, password, phone)
            _user.postValue(Resource.Success(response.data.user))

            val token = StringBuilder()
            token.append(response.data.token_type)
            token.append(" ")
            token.append(response.data.access_token)

            _token.postValue(token.toString())
        } catch (e: IOException) {
            _user.postValue(Resource.Error(e.message))
            _token.postValue("")
        }
    }

    fun getUser(token: String) = viewModelScope.launch {
        _user.postValue(Resource.Loading())
        try {
            val response = repository.getUser(token = token)
            _user.postValue(Resource.Success(response.data))
        } catch (e: IOException) {
            _user.postValue(Resource.Error(e.message))
        }
    }

    fun logout(token: String) = viewModelScope.launch {
        _meta.postValue(Resource.Loading())
        try {
            val response = repository.logout(token = token)
            _meta.postValue(Resource.Success(response))
        } catch (e: IOException) {
            _meta.postValue(Resource.Error(e.message))
        }
    }
}