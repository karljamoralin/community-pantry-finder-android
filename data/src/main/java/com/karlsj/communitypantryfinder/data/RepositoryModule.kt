package com.karlsj.communitypantryfinder.data

import com.karlsj.communitypantryfinder.domain.PantryRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {
    @Provides
    fun provideGetPantryPort(remote: PantryRemoteDataSource): PantryRepository =
        DefaultPantryRepository(remote)
}