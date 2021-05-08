package com.karlsj.communitypantryfinder.domain

interface PantryRepository {
    fun getPantries(): List<Pantry>
}