package com.karlsj.communitypantryfinder.googlemaps

import com.google.android.gms.maps.model.LatLng

internal data class MapMarker(
    val name: String,
    val position: LatLng,
    val snippet: String
)
