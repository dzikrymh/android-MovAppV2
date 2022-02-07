package me.dzikry.movapp.data.repositories

import me.dzikry.movapp.data.models.response.Meta
import me.dzikry.movapp.data.models.response.UserResponse
import me.dzikry.movapp.data.models.response.UserResponseWithoutToken

interface AuthRepository {
    suspend fun login(email: String, password: String) : UserResponse
    suspend fun register(name: String, email: String, username: String, password: String, phone: String) : UserResponse
    suspend fun getUser(token: String) : UserResponseWithoutToken
    suspend fun logout(token: String) : Meta
}