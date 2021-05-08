package com.karlsj.communitypantryfinder.domain

import javax.inject.Inject

internal class DefaultPantryService @Inject constructor(
    private val repository: PantryRepository
) : PantryService {
    override fun getPantries(): List<Pantry> {
        return repository.getPantries()
    }
}