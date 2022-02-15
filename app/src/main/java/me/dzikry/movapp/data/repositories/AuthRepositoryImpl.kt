package me.dzikry.movapp.data.repositories

import com.google.gson.Gson
import me.dzikry.movapp.data.models.Account
import me.dzikry.movapp.data.models.body.SessionLogoutBody
import me.dzikry.movapp.data.models.response.*
import me.dzikry.movapp.data.networks.AuthAPIs
import me.dzikry.movapp.utils.Const
import okio.IOException
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val api: AuthAPIs,
) : AuthRepository {

    override suspend fun requestToken(): RequestTokenResponse {
        val response = api.requestToken(api_key = Const.API_KEY_MOVIE)

        if (response.isSuccessful) {
            return response.body()!!
        } else {
            val gson = Gson()
            val errorResponse: BaseMovieResponse =
                gson.fromJson(response.errorBody()!!.charStream(), BaseMovieResponse::class.java)
            throw IOException(errorResponse.status_message)
        }
    }

    override suspend fun validateLogin(
        username: String,
        password: String,
        request_token: String
    ): RequestTokenResponse {
        val response = api.validateLogin(Const.API_KEY_MOVIE, username, password, request_token)

        if (response.isSuccessful) {
            return response.body()!!
        } else {
            val gson = Gson()
            val errorResponse: BaseMovieResponse =
                gson.fromJson(response.errorBody()!!.charStream(), BaseMovieResponse::class.java)
            throw IOException(errorResponse.status_message)
        }
    }

    override suspend fun createSessionID(request_token: String): SessionIDResponse {
        val response = api.createSessionID(Const.API_KEY_MOVIE, request_token)

        if (response.isSuccessful) {
            return response.body()!!
        } else {
            val gson = Gson()
            val errorResponse: BaseMovieResponse =
                gson.fromJson(response.errorBody()!!.charStream(), BaseMovieResponse::class.java)
            throw IOException(errorResponse.status_message)
        }
    }

    override suspend fun sessionLogout(session_id: String): SessionLogout {
        val response = api.sessionLogout(Const.API_KEY_MOVIE, SessionLogoutBody(session_id = session_id))

        if (response.isSuccessful) {
            return response.body()!!
        } else {
            val gson = Gson()
            val errorResponse: BaseMovieResponse =
                gson.fromJson(response.errorBody()!!.charStream(), BaseMovieResponse::class.java)
            throw IOException(errorResponse.status_message)
        }
    }

    override suspend fun getAccountDetail(session_id: String): Account {
        val response = api.getAccountDetail(Const.API_KEY_MOVIE, session_id)

        if (response.isSuccessful) {
            return response.body()!!
        } else {
            val gson = Gson()
            val errorResponse: BaseMovieResponse =
                gson.fromJson(response.errorBody()!!.charStream(), BaseMovieResponse::class.java)
            throw IOException(errorResponse.status_message)
        }
    }
}