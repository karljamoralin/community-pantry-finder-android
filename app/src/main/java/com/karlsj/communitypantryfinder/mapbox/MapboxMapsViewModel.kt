package com.karlsj.communitypantryfinder.mapbox

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.karlsj.communitypantryfinder.domain.PantryService
import com.mapbox.mapboxsdk.geometry.LatLng
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class MapboxMapsViewModel @Inject constructor(
    private val pantryService: PantryService,
) : ViewModel() {

    interface Callback {
        fun onGetPantrySuccess(pantries: List<Pantry>)
        fun onGetPantryError(message: String)
    }

    fun getData(callback: Callback) {
        viewModelScope.launch(Dispatchers.IO) {
            pantryService.getPantries().map { pantry ->
                Pantry(
                    name = pantry.name,
                    position = LatLng(pantry.latitude, pantry.longitude),
                    supplies = pantry.supplies,
                    contact = pantry.contact,
                    number = pantry.number,
                    streetAddress = pantry.streetAddress,
                    barangay = pantry.barangay,
                    city = pantry.city,
                    province = pantry.province,
                    region = pantry.region,
                    sched = pantry.sched,
                    more = pantry.more
                )
            }.let { list ->
                Handler(Looper.getMainLooper()).post {
                    callback.onGetPantrySuccess(list)
                }
            }
        }
    }
}