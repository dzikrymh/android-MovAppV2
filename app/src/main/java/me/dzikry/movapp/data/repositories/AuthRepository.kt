package me.dzikry.movapp.data.repositories

import me.dzikry.movapp.data.models.Account
import me.dzikry.movapp.data.models.response.*

interface AuthRepository {
    suspend fun requestToken() : RequestTokenResponse
    suspend fun validateLogin(username: String, password: String, request_token: String) : RequestTokenResponse
    suspend fun createSessionID(request_token: String) : SessionIDResponse
    suspend fun sessionLogout(session_id: String) : SessionLogout
    suspend fun getAccountDetail(session_id: String) : Account
}