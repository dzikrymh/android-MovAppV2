package me.dzikry.movapp.data.networks

import me.dzikry.movapp.data.models.response.Meta
import me.dzikry.movapp.data.models.response.UserResponse
import me.dzikry.movapp.data.models.response.UserResponseWithoutToken
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

//    companion object {
//        operator fun invoke() : AuthAPIs {
//            val logging = HttpLoggingInterceptor()
//            logging.level = HttpLoggingInterceptor.Level.BODY
//            val okHttpClient = OkHttpClient.Builder()
//                .addInterceptor(logging)
//                .readTimeout(90, TimeUnit.SECONDS)
//                .connectTimeout(90, TimeUnit.SECONDS)
//                .build()
//
//            return Retrofit.Builder()
//                .client(okHttpClient)
//                .baseUrl(Const.BASE_URL_AUTH)
//                .addConverterFactory(GsonConverterFactory.create())
//                .build()
//                .create(AuthAPIs::class.java)
//        }
//    }

}