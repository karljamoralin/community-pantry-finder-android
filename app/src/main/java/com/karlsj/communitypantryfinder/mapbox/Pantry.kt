package com.karlsj.communitypantryfinder.mapbox

import android.os.Parcelable
import com.mapbox.mapboxsdk.geometry.LatLng
import kotlinx.parcelize.Parcelize

@Parcelize
data class Pantry(
    val name: String,
    val position: LatLng,
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
): Parcelable
