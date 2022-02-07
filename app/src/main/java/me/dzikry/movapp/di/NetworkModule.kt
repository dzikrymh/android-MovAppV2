package me.dzikry.movapp.di

import android.content.Context
import com.google.gson.FieldNamingPolicy
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import me.dzikry.movapp.data.networks.AuthAPIs
import me.dzikry.movapp.data.networks.MovieAPIs
import me.dzikry.movapp.data.networks.NewsAPIs
import me.dzikry.movapp.utils.Const
import me.dzikry.movapp.utils.NetworkConnectionInterceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Named
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {
    @Inject
    @Named("provideRetrofitAuth")
    lateinit var retrofitAuth: Retrofit

    @Inject
    @Named("provideRetrofitMovie")
    lateinit var retrofitMovie: Retrofit

    @Inject
    @Named("provideRetrofitNews")
    lateinit var retrofitNews: Retrofit

    @Provides
    @Singleton
    fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor =
        HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)

    @Provides
    @Singleton
    fun provideOkHttpClient(
        @ApplicationContext context: Context,
        httpLoggingInterceptor: HttpLoggingInterceptor
    ): OkHttpClient =
        OkHttpClient.Builder().apply {
            interceptors().add(httpLoggingInterceptor)
            interceptors().add(NetworkConnectionInterceptor(context))
            readTimeout(90, TimeUnit.SECONDS)
            connectTimeout(90, TimeUnit.SECONDS)
        }.build()

    @Provides
    @Singleton
    @Named("provideRetrofitAuth")
    fun provideRetrofitAuth(gson: Gson, okHttpClient: OkHttpClient): Retrofit =
        Retrofit.Builder().apply {
            baseUrl(Const.BASE_URL_AUTH)
            addConverterFactory(GsonConverterFactory.create(gson))
            client(okHttpClient)
        }.build()

    @Provides
    @Singleton
    @Named("provideRetrofitMovie")
    fun provideRetrofitMovie(gson: Gson, okHttpClient: OkHttpClient): Retrofit =
        Retrofit.Builder().apply {
            baseUrl(Const.BASE_URL_MOVIE)
            addConverterFactory(GsonConverterFactory.create(gson))
            client(okHttpClient)
        }.build()

    @Provides
    @Singleton
    @Named("provideRetrofitNews")
    fun provideRetrofitNews(gson: Gson, okHttpClient: OkHttpClient): Retrofit =
        Retrofit.Builder().apply {
            baseUrl(Const.BASE_URL_NEWS)
            addConverterFactory(GsonConverterFactory.create(gson))
            client(okHttpClient)
        }.build()

    @Provides
    @Singleton
    fun provideAuthApi(@Named("provideRetrofitAuth") retrofit: Retrofit): AuthAPIs =
        retrofit.create(AuthAPIs::class.java)

    @Provides
    @Singleton
    fun provideMovieApi(@Named("provideRetrofitMovie") retrofit: Retrofit): MovieAPIs =
        retrofit.create(MovieAPIs::class.java)

    @Provides
    @Singleton
    fun provideNewsApi(@Named("provideRetrofitNews") retrofit: Retrofit): NewsAPIs =
        retrofit.create(NewsAPIs::class.java)

    @Provides
    @Singleton
    fun provideGson(): Gson =
        GsonBuilder()
            .setFieldNamingPolicy(FieldNamingPolicy.IDENTITY)
            .serializeNulls()
            .setLenient()
            .create()
}