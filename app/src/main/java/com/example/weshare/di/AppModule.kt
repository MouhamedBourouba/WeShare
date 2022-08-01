package com.example.weshare.di

import android.content.Context
import com.example.weshare.data.localdb.RoomDataBase
import com.example.weshare.domain.repository.Profile.ProfileRepository
import com.example.weshare.domain.repository.Profile.ProfileRepositoryImp
import com.example.weshare.domain.repository.auth.AuthRepository
import com.example.weshare.domain.repository.auth.FirebaseAuthRepositoryImp
import com.example.weshare.domain.repository.complete_account.CompleteAccountRepository
import com.example.weshare.domain.repository.complete_account.CompleteAccountRepositoryImp
import com.example.weshare.domain.repository.search.SearchRepository
import com.example.weshare.domain.repository.search.SearchRepositoryImp
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun providesContext(@ApplicationContext context: Context): Context = context


    @Provides
    @Singleton
    fun providesRoomDB(@ApplicationContext context: Context): RoomDataBase =
        RoomDataBase.getDataBase(context)


    @Provides
    @Singleton
    fun providesAuthRepository(roomDataBase: RoomDataBase): AuthRepository =
        FirebaseAuthRepositoryImp(roomDataBase)


    @Provides
    @Singleton
    fun providesCompleteAccountRepository(
        roomDataBase: RoomDataBase,
    ): CompleteAccountRepository = CompleteAccountRepositoryImp(roomDataBase)

    @Provides
    @Singleton
    fun providesSearchRepository(): SearchRepository = SearchRepositoryImp()

    @Provides
    @Singleton
    fun providesProfileRepository(roomDataBase: RoomDataBase): ProfileRepository = ProfileRepositoryImp(roomDataBase)
}