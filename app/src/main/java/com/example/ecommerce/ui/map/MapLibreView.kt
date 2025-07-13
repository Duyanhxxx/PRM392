package com.example.ecommerce.ui.map

import android.content.Context
import android.os.Bundle
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import org.maplibre.android.MapLibre
import org.maplibre.android.annotations.MarkerOptions
import org.maplibre.android.camera.CameraPosition
import org.maplibre.android.camera.CameraUpdateFactory
import org.maplibre.android.geometry.LatLng
import org.maplibre.android.maps.MapView
import org.maplibre.android.maps.Style

@Composable
fun MapLibreScreen() {
    val context = LocalContext.current
    val mapView = rememberMapLibreViewWithLifecycle(context)

    AndroidView(
        factory = { mapView },
        modifier = Modifier.fillMaxSize()
    )
//    Text("Map screen loaded")
}

@Composable
fun rememberMapLibreViewWithLifecycle(context: Context): MapView {
    MapLibre.getInstance(context)

    val mapView = remember {
        MapView(context).apply {
            onCreate(Bundle())
            getMapAsync { maplibreMap ->
                maplibreMap.setStyle(
                    Style.Builder().fromUri("https://api.maptiler.com/maps/streets/style.json?key=MjOsDkcqAOWnH1TAzEiK")
                ) {
                    val shopLocation = LatLng(21.028511, 105.804817)
                    maplibreMap.moveCamera(
                        CameraUpdateFactory.newCameraPosition(
                            CameraPosition.Builder()
                                .target(shopLocation)
                                .zoom(14.0)
                                .build()
                        )
                    )
                    maplibreMap.addMarker(
                        MarkerOptions()
                            .position(shopLocation)
                            .title("Cửa hàng Ecommerce")
                    )
                }
            }
        }
    }

    val lifecycle = LocalLifecycleOwner.current.lifecycle
    DisposableEffect(lifecycle) {
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_START -> mapView.onStart()
                Lifecycle.Event.ON_RESUME -> mapView.onResume()
                Lifecycle.Event.ON_PAUSE -> mapView.onPause()
                Lifecycle.Event.ON_STOP -> mapView.onStop()
                Lifecycle.Event.ON_DESTROY -> mapView.onDestroy()
                else -> Unit
            }
        }

        lifecycle.addObserver(observer)
        onDispose {
            lifecycle.removeObserver(observer)
        }
    }

    return mapView
}
