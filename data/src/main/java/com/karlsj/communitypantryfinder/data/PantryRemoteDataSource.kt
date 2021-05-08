package com.karlsj.communitypantryfinder.data

interface PantryRemoteDataSource {
    fun getPantries(): List<Pantry>
}