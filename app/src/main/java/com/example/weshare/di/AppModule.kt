package com.example.weshare.di

import com.example.weshare.domain.repository.AuthRepository
import com.example.weshare.domain.repository.FirebaseAuthRepositoryImp
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun providesAuthRepository(): AuthRepository = FirebaseAuthRepositoryImp()

}