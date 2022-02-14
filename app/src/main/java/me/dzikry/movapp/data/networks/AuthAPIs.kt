package me.dzikry.movapp.data.networks

import me.dzikry.movapp.data.models.Account
import me.dzikry.movapp.data.models.response.*
import me.dzikry.movapp.utils.Const
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*
import java.util.concurrent.TimeUnit

interface AuthAPIs {

    @FormUrlEncoded
    @POST("login")
    suspend fun login(
        @Field("email") email: String,
        @Field("password") password: String
    ): Response<UserResponse>

    @FormUrlEncoded
    @POST("register")
    suspend fun register(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("username") username: String,
        @Field("password") password: String,
        @Field("phone") phone: String
    ): Response<UserResponse>

    @GET("user")
    suspend fun getUser(
        @Header("Accept") accept: String = "application/json",
        @Header("Authorization") token: String
    ): Response<UserResponseWithoutToken>

    @POST("logout")
    suspend fun logout(
        @Header("Accept") accept: String = "application/json",
        @Header("Authorization") token: String
    ): Response<Meta>

    /**
     * Auth baru langsung dari themoviedb.org
     **/
    @GET("authentication/token/new")
    suspend fun requestToken(
        @Query("api_key") api_key: String,
    ): Response<RequestTokenResponse>

    @POST("authentication/token/validate_with_login")
    suspend fun validateLogin(
        @Query("api_key") api_key: String,
        @Field("username") username: String,
        @Field("password") password: String,
        @Field("request_token") request_token: String,
    ): Response<RequestTokenResponse>

    @POST("authentication/session/new")
    suspend fun createSessionID(
        @Query("api_key") api_key: String,
        @Field("request_token") request_token: String,
    ): Response<SessionIDResponse>

    @DELETE("authentication/session")
    suspend fun sessionLogout(
        @Query("api_key") api_key: String,
        @Field("session_id") session_id: String,
    ): Response<SessionLogout>

    @GET("account")
    suspend fun getAccountDetail(
        @Query("api_key") api_key: String,
        @Query("session_id") session_id: String,
    ): Response<Account>

}