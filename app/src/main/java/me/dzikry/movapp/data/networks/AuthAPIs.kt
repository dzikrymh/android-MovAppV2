package me.dzikry.movapp.data.networks

import me.dzikry.movapp.data.models.Account
import me.dzikry.movapp.data.models.body.SessionLogoutBody
import me.dzikry.movapp.data.models.response.*
import retrofit2.Response
import retrofit2.http.*

interface AuthAPIs {

    @GET("authentication/token/new")
    suspend fun requestToken(
        @Query("api_key") api_key: String,
    ): Response<RequestTokenResponse>

    @FormUrlEncoded
    @POST("authentication/token/validate_with_login")
    suspend fun validateLogin(
        @Query("api_key") api_key: String,
        @Field("username") username: String,
        @Field("password") password: String,
        @Field("request_token") request_token: String,
    ): Response<RequestTokenResponse>

    @FormUrlEncoded
    @POST("authentication/session/new")
    suspend fun createSessionID(
        @Query("api_key") api_key: String,
        @Field("request_token") request_token: String,
    ): Response<SessionIDResponse>

    @HTTP(method = "DELETE", path = "authentication/session", hasBody = true)
    suspend fun sessionLogout(
        @Query("api_key") api_key: String,
        @Body session_body: SessionLogoutBody,
    ): Response<SessionLogout>

    @GET("account")
    suspend fun getAccountDetail(
        @Query("api_key") api_key: String,
        @Query("session_id") session_id: String,
    ): Response<Account>

}