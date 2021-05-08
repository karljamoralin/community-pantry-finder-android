package com.karlsj.communitypantryfinder.googlemaps

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.Typeface
import android.location.Location
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.karlsj.communitypantryfinder.googlemaps.PermissionUtils.PermissionDeniedDialog.Companion.newInstance
import com.karlsj.communitypantryfinder.googlemaps.PermissionUtils.isPermissionGranted
import com.karlsj.communitypantryfinder.googlemaps.PermissionUtils.requestPermission
import com.karlsj.communitypantryfinder.R
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
internal class GoogleMapsActivity : AppCompatActivity(R.layout.activity_google_maps),
    OnMapReadyCallback,
    ActivityCompat.OnRequestPermissionsResultCallback,
    GoogleMapsViewModel.Callback {

    private var permissionDenied = false

    private val fusedLocationClient: FusedLocationProviderClient by lazy {
        LocationServices.getFusedLocationProviderClient(this)
    }

    private lateinit var map: GoogleMap

    private val viewModel: GoogleMapsViewModel by viewModels()

    override fun onGetPantrySuccess(mapMarkers: List<MapMarker>) {
        LatLngBounds.builder().run {
            mapMarkers.forEach { mapMarker ->
                map.addMarker(
                    MarkerOptions()
                        .position(mapMarker.position)
                        .title(mapMarker.name)
                        .snippet(mapMarker.snippet)
                )

                include(mapMarker.position)
            }

            build().center
        }.let { center ->
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(center, 6f))
            enableMyLocation()
        }
    }

    override fun onGetPantryError(message: String) {
        Timber.e(message)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        with(supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment) {
            getMapAsync(this@GoogleMapsActivity)
        }
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap.apply {
            setMinZoomPreference(5f)

            uiSettings.isZoomControlsEnabled = true

            setMapStyle(
                MapStyleOptions.loadRawResourceStyle(
                    this@GoogleMapsActivity, R.raw.style_json
                )
            )

            setInfoWindowAdapter(object : InfoWindowAdapter {
                override fun getInfoWindow(arg0: Marker): View? {
                    return null
                }

                override fun getInfoContents(marker: Marker): View {
                    val info = LinearLayout(this@GoogleMapsActivity)
                    info.orientation = LinearLayout.VERTICAL

                    val title = TextView(this@GoogleMapsActivity)
                    title.setTextColor(Color.BLACK)
                    title.gravity = Gravity.CENTER
                    title.setTypeface(null, Typeface.BOLD)
                    title.text = marker.title

                    val snippet = TextView(this@GoogleMapsActivity)
                    snippet.setTextColor(Color.GRAY)
                    snippet.text = marker.snippet
                    info.addView(title)
                    info.addView(snippet)

                    return info
                }
            })
        }

        viewModel.getData(this)
    }

    /**
     * Enables the My Location layer if the fine location permission has been granted.
     */
    private fun enableMyLocation() {
        if (!::map.isInitialized) return

        if (
            ContextCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            map.isMyLocationEnabled = true

            fusedLocationClient.lastLocation
                .addOnSuccessListener { location: Location? ->
                    location?.let {
                        map.moveCamera(
                            CameraUpdateFactory.newLatLngZoom(
                                LatLng(location.latitude, location.longitude),
                                14f
                            )
                        )
                    }
                }
        } else {
            // Permission to access the location is missing. Show rationale and request permission
            requestPermission(
                this,
                LOCATION_PERMISSION_REQUEST_CODE,
                Manifest.permission.ACCESS_FINE_LOCATION,
                false
            )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        if (requestCode != LOCATION_PERMISSION_REQUEST_CODE) {
            return
        }

        if (isPermissionGranted(
                permissions,
                grantResults,
                Manifest.permission.ACCESS_FINE_LOCATION
            )
        ) {
            // Enable the my location layer if the permission has been granted.
            enableMyLocation()
        } else {
            // Permission was denied. Display an error message
            // Display the missing permission error dialog when the fragments resume.
            permissionDenied = true
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun onResumeFragments() {
        super.onResumeFragments()
        if (permissionDenied) {
            // Permission was not granted, display error dialog.
            showMissingPermissionError()
            permissionDenied = false
        }
    }

    /**
     * Displays a dialog with error message explaining that the location permission is missing.
     */
    private fun showMissingPermissionError() {
        newInstance(false).show(supportFragmentManager, "dialog")
    }

    companion object {
        /**
         * Request code for location permission request.
         *
         * @see .onRequestPermissionsResult
         */
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1
    }
}