package me.dzikry.movapp.data.repositories

import com.google.gson.Gson
import me.dzikry.movapp.data.models.response.Meta
import me.dzikry.movapp.data.models.response.UserResponse
import me.dzikry.movapp.data.models.response.UserResponseWithoutToken
import me.dzikry.movapp.data.networks.AuthAPIs
import okio.IOException

class AuthRepository(private val api: AuthAPIs) {

    suspend fun login(email: String, password: String) : UserResponse {
        val response = api.login(email, password)

        if (response.isSuccessful) {
            return response.body()!!
        } else {
            val gson = Gson()
            val errorResponse: UserResponse =
                gson.fromJson(response.errorBody()!!.charStream(), UserResponse::class.java)
            throw IOException(errorResponse.meta.message)
        }
    }

    suspend fun register(name: String, email: String, username: String, password: String, phone: String) : UserResponse {
        val response = api.register(name, email, username, password, phone)

        if (response.isSuccessful) {
            return response.body()!!
        } else {
            val gson = Gson()
            val errorResponse: UserResponse =
                gson.fromJson(response.errorBody()!!.charStream(), UserResponse::class.java)
            throw IOException(errorResponse.meta.message)
        }
    }

    suspend fun getUser(token: String) : UserResponseWithoutToken {
        val response = api.getUser(token = token)

        if (response.isSuccessful) {
            return response.body()!!
        } else {
            val gson = Gson()
            val errorResponse: UserResponse =
                gson.fromJson(response.errorBody()!!.charStream(), UserResponse::class.java)
            throw IOException(errorResponse.meta.message)
        }
    }

    suspend fun logout(token: String) : Meta {
        val response = api.logout(token = token)

        if (response.isSuccessful) {
            return response.body()!!
        } else {
            val gson = Gson()
            val errorResponse: UserResponse =
                gson.fromJson(response.errorBody()!!.charStream(), UserResponse::class.java)
            throw IOException(errorResponse.meta.message)
        }
    }
}