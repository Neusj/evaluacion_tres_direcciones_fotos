package com.example.evaluacion_tres.direcciones

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import org.osmdroid.config.Configuration
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth

class DirectionsActivity : ComponentActivity() {
    @ExperimentalComposeUiApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyApp()
        }
    }
}

@Composable
fun MyApp() {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        MyMapa()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyMapa() {
    val context = LocalContext.current
    Configuration.getInstance().load(context, context.getSharedPreferences("osmdroid", 0))

    var locationText by remember { mutableStateOf("") }

    val mapView = rememberMapView()

    var locationPermissionGranted by remember { mutableStateOf(false) }
    val requestPermissionLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            locationPermissionGranted = isGranted
            if (isGranted) {
                mapView.overlayManager.add(MyLocationNewOverlay(GpsMyLocationProvider(context), mapView))
            }
        }

    //Para obtener la ubucación del dispositivo
    val fusedLocationClient: FusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context)

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Bottom,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .weight(1f)
        ) {
            AndroidView(
                factory = { mapView },
                modifier = Modifier.fillMaxSize()
            ) { /* no-op */ }
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.surface)
                .padding(16.dp)
        ) {
            TextField(
                value = locationText,
                onValueChange = { locationText = it },
                label = { Text("Introduce la ubicación") },
                keyboardOptions = KeyboardOptions.Default.copy(
                    imeAction = ImeAction.Done,
                    keyboardType = KeyboardType.Text
                ),
                modifier = Modifier.padding(16.dp)
            )

            Button(
                onClick = {

                    // mientra se incorpora una api de geolicalización por defecto
                    // usará las coordenadas se valparaiso
                    val currentLocation = GeoPoint( -33.0458, -71.6197)
                    mapView.controller.animateTo(currentLocation, 15.0, 1000L)

                    val marker = Marker(mapView)
                    marker.position = currentLocation
                    mapView.overlays.add(marker)
                },
                modifier = Modifier.padding(16.dp)
            ) {
                Text("Mostrar ubicación ingresada")
            }

            Button(
                onClick = {
                    if (locationPermissionGranted) {
                        if (ContextCompat.checkSelfPermission(
                                context,
                                Manifest.permission.ACCESS_FINE_LOCATION
                            ) == PackageManager.PERMISSION_GRANTED
                        ) {
                            //Obteniendo las coordenadas con la libreria de goole play location
                            fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                                if (location != null) {
                                    val currentLocation = GeoPoint(location.latitude, location.longitude)
                                    mapView.controller.animateTo(currentLocation, 15.0, 1000L)

                                    val marker = Marker(mapView)
                                    marker.position = currentLocation
                                    mapView.overlays.add(marker)
                                }
                            }
                        } else {
                            requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
                        }
                    } else {
                        requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
                    }
                },
                modifier = Modifier.padding(16.dp)
            ) {
                Text("Mostrar mi ubicación actual")
            }
        }
    }
}


@Composable
fun rememberMapView(): MapView {
    val context = LocalContext.current
    return remember {
        MapView(context).apply {
            setTileSource(org.osmdroid.tileprovider.tilesource.TileSourceFactory.MAPNIK)
            controller.setZoom(15.0)
        }
    }
}
