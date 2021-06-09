package com.karlsj.communitypantryfinder.mapbox

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import com.karlsj.communitypantryfinder.R
import com.karlsj.communitypantryfinder.databinding.ActivityMapboxBinding
import com.karlsj.communitypantryfinder.mapbox.PantryDetailsBottomSheet.Companion.ARG_PANTRY
import com.mapbox.mapboxsdk.Mapbox
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory
import com.mapbox.mapboxsdk.geometry.LatLng
import com.mapbox.mapboxsdk.maps.MapView
import com.mapbox.mapboxsdk.maps.MapboxMap
import com.mapbox.mapboxsdk.maps.Style
import com.mapbox.mapboxsdk.plugins.annotation.Symbol
import com.mapbox.mapboxsdk.plugins.annotation.SymbolManager
import com.mapbox.mapboxsdk.plugins.annotation.SymbolOptions
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber


@AndroidEntryPoint
internal class MapboxActivity : AppCompatActivity(), MapboxMapsViewModel.Callback {

    private val binding by lazy {
        ActivityMapboxBinding.inflate(layoutInflater)
    }

    private val map: MutableMap<Symbol, Pantry> = mutableMapOf()
    private var mapView: MapView? = null
    private val viewModel: MapboxMapsViewModel by viewModels()

    private lateinit var mapboxMap: MapboxMap
    private lateinit var mapStyle: Style
    private lateinit var symbolManager: SymbolManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Mapbox.getInstance(this, getString(R.string.mapbox_access_token))

        setContentView(binding.root)

        mapView = binding.map.apply {
            onCreate(savedInstanceState)

            getMapAsync { mapboxMap ->
                this@MapboxActivity.mapboxMap = mapboxMap

                mapboxMap.animateCamera(
                    CameraUpdateFactory.newLatLngZoom(
                        LatLng(12.879721, 121.774017),
                        DEFAULT_ZOOM
                    )
                )

                mapboxMap.setStyle(
                    Style.Builder().fromUri(getString(R.string.mapbox_style_uri))
                ) { style ->

                    this@MapboxActivity.mapStyle = style

                    style.addImage(
                        MARKER_ICON,
                        ResourcesCompat.getDrawable(
                            resources,
                            R.drawable.ic_baseline_place_24,
                            null
                        )!!
                    )

                    symbolManager = SymbolManager(
                        this,
                        mapboxMap,
                        style
                    ).apply {
                        addClickListener { symbol ->
                            PantryDetailsBottomSheet().let { fragment ->
                                fragment.arguments = Bundle().apply {
                                    putParcelable(ARG_PANTRY, map[symbol])
                                }

                                fragment.show(supportFragmentManager, "pantry-bottom-sheet")
                            }

                            true
                        }
                    }
                }

                viewModel.getData(this@MapboxActivity)
            }
        }
    }

    override fun onStart() {
        super.onStart()
        mapView?.onStart()
    }

    override fun onResume() {
        super.onResume()
        mapView?.onResume()
    }

    override fun onPause() {
        super.onPause()
        mapView?.onPause()
    }

    override fun onStop() {
        super.onStop()
        mapView?.onStop()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        mapView?.onSaveInstanceState(outState)
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView?.onLowMemory()
    }

    override fun onDestroy() {
        super.onDestroy()
        mapView?.onDestroy()
    }

    override fun onGetPantrySuccess(pantries: List<Pantry>) {
        Timber.i("# of pantries retrieved: %s", pantries.size)

        pantries.map { pantry ->
            SymbolOptions()
                .withLatLng(pantry.position)
                .withIconImage(MARKER_ICON)
                .withIconSize(1.3f)
        }.let { options ->
            symbolManager.create(options)
        }.forEachIndexed { i, symbol ->
            map[symbol] = pantries[i]
        }
    }

    override fun onGetPantryError(message: String) {
        Timber.e(message)
    }

    companion object {
        private const val DEFAULT_ZOOM = 4.5
        private const val MARKER_ICON = "marker_icon"
    }
}