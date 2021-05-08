package com.karlsj.communitypantryfinder.domain

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
object DomainModule {
    @Provides
    fun providePantryService(repository: PantryRepository): PantryService =
        DefaultPantryService(repository)
}