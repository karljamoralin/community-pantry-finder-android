package com.karlsj.communitypantryfinder.data

data class Pantry(
    val latitude: Double,
    val longitude: Double,
    val name: String,
    val supplies: String,
    val contact: String,
    val number: String,
    val streetAddress: String,
    val barangay: String,
    val city: String,
    val province: String,
    val region: String,
    val sched: String,
    val more: String
)
