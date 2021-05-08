package com.karlsj.communitypantryfinder.googlemaps

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.model.LatLng
import com.karlsj.communitypantryfinder.domain.PantryService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class GoogleMapsViewModel @Inject constructor(
    private val pantryService: PantryService
) : ViewModel() {

    interface Callback {
        fun onGetPantrySuccess(mapMarkers: List<MapMarker>)
        fun onGetPantryError(message: String)
    }

    fun getData(callback: Callback) {
        viewModelScope.launch(Dispatchers.IO) {
            pantryService.getPantries().map { pantry ->

                val snippet = pantry.run {
                    val sb = StringBuilder()

                    if (streetAddress.isNotEmpty()) sb.append("Street Address: $streetAddress\n")
                    if (barangay.isNotEmpty()) sb.append("Barangay: $barangay\n")
                    if (city.isNotEmpty()) sb.append("City: $city\n")
                    if (province.isNotEmpty()) sb.append("Province: $province\n")
                    if (region.isNotEmpty()) sb.append("Region: $region\n")
                    if (supplies.isNotEmpty()) sb.append("Supplies Available: $supplies\n")
                    if (sched.isNotEmpty()) sb.append("Schedule: $sched\n")
                    if (contact.isNotEmpty()) sb.append("Contact Person: $contact\n")
                    if (number.isNotEmpty()) sb.append("Contact #: $number\n")
                    if (more.isNotEmpty()) sb.append("Other details: $more\n")

                    sb.toString()
                }

                MapMarker(
                    pantry.name,
                    LatLng(pantry.latitude, pantry.longitude),
                    snippet
                )
            }.let { list ->
                Handler(Looper.getMainLooper()).post {
                    callback.onGetPantrySuccess(list)
                }
            }
//            pantryService.getPantries().map { pantry ->
//                Pantry(
//                    pantry.latitude,
//                    pantry.longitude,
//                    pantry.name,
//                    pantry.supplies,
//                    pantry.contact,
//                    pantry.number,
//                    pantry.streetAddress,
//                    pantry.barangay,
//                    pantry.city,
//                    pantry.province,
//                    pantry.region,
//                    pantry.sched,
//                    pantry.more
//                )
//            }.let { list ->
//                Handler(Looper.getMainLooper()).post {
//                    callback.onGetDataSuccess(list)
//                }
//            }
        }
    }
}