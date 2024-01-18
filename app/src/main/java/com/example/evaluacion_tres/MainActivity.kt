package com.example.evaluacion_tres

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.evaluacion_tres.direcciones.DirectionsActivity
import com.example.evaluacion_tres.fotos.PhotosActivity
import com.example.evaluacion_tres.ui.theme.Evaluacion_tresTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Evaluacion_tresTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    HomeOptions()
                }
            }
        }
    }
}

@Composable
fun HomeOptions() {
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Botón para ir a la actividad de direcciones
        Button(
            onClick = {
                val intent = Intent(context, DirectionsActivity::class.java)
                context.startActivity(intent)
            },
            modifier = Modifier
                .padding(8.dp)
        ) {
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = "Direcciones")
        }

        // Botón para ir a la actividad de fotos
        Button(
            onClick = {
                val intent = Intent(context, PhotosActivity::class.java)
                context.startActivity(intent)
            },
            modifier = Modifier
                .padding(8.dp)
        ) {
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = "Fotos")
        }
    }
}
