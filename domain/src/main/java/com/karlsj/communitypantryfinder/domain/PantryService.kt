package com.karlsj.communitypantryfinder.domain

import javax.inject.Inject

interface PantryService {
    fun getPantries(): List<Pantry>
}