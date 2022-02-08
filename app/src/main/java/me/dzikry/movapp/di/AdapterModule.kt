package me.dzikry.movapp.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import me.dzikry.movapp.ui.adapter.GenreDetailAdapter
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AdapterModule {
    @Provides
    @Singleton
    fun bindAdapterGenreDetail(): GenreDetailAdapter = GenreDetailAdapter(mutableListOf())
}