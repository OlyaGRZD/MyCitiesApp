package com.example.mycitiesapp.di

import com.example.mycitiesapp.data.store.SelectedListStoreImpl
import com.example.mycitiesapp.domain.store.SelectedListStore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object SelectedListModule {

    @Singleton
    @Provides
    fun provideSelectedListStore(): SelectedListStore =
        SelectedListStoreImpl()
}