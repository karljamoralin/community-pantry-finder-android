package com.karlsj.communitypantryfinder.remote

import com.karlsj.communitypantryfinder.data.PantryRemoteDataSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.Request

@Module
@InstallIn(SingletonComponent::class)
object RemoteModule {

    @Provides
    fun providePantryRemoteDataSource(
        okHttpClient: OkHttpClient,
        request: Request
    ): PantryRemoteDataSource = PantryOkHttpRemoteDataSource(
        okHttpClient,
        request
    )

    @Provides
    fun provideHttpClient(): OkHttpClient = OkHttpClient()

    @Provides
    fun providesOkHttpRequest(): Request = Request.Builder()
        .url(BuildConfig.URL)
        .build()

}