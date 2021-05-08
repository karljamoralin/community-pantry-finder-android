package com.karlsj.communitypantryfinder.data

import com.karlsj.communitypantryfinder.domain.Pantry
import com.karlsj.communitypantryfinder.domain.PantryRepository
import javax.inject.Inject

internal class DefaultPantryRepository @Inject constructor(
    private val remote: PantryRemoteDataSource
) : PantryRepository {
    override fun getPantries(): List<Pantry> {
        return remote.getPantries().map {
            Pantry(
                it.latitude,
                it.longitude,
                it.name,
                it.supplies,
                it.contact,
                it.number,
                it.streetAddress,
                it.barangay,
                it.city,
                it.province,
                it.region,
                it.sched,
                it.more
            )
        }
    }
}