package com.example.evaluacion_tres.fotos

import android.Manifest
import android.app.Activity
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.MediaStore
import android.view.ViewGroup
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.view.LifecycleCameraController
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner

import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import java.io.File
import java.util.concurrent.Executor

@OptIn(ExperimentalPermissionsApi::class, ExperimentalMaterial3Api::class)
@Composable
fun TakePhoto(activity: Activity) {

    val contex = LocalContext.current
    val cameraController = remember {
        LifecycleCameraController(contex)
    }
    val lifeCycle = LocalLifecycleOwner.current
    val permissionState = rememberPermissionState(permission = Manifest.permission.CAMERA)
    LaunchedEffect(Unit){
        permissionState.launchPermissionRequest()
    }

    Scaffold(modifier = Modifier.fillMaxSize(), floatingActionButton = {
        FloatingActionButton(onClick = {
            val executor = ContextCompat.getMainExecutor(contex)
            takePicture(cameraController, executor, contex, activity)
        }) {
            Text(text = "Capturar")
        }
    }){
        if (permissionState.status.isGranted){
            Cameracomposable(modifier = Modifier.padding(it), cameraController, lifeCycle)
        } else {
            Text(text = "Esperando permiso", modifier = Modifier.padding(it))
        }
    }

}

fun takePicture(cameraController: LifecycleCameraController, executor: Executor, context: Context, activity: Activity) {
    val contentValues = ContentValues().apply {
        put(MediaStore.Images.Media.DISPLAY_NAME, "imagenText_${System.currentTimeMillis()}.jpg")
        put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
    }

    val outputOptions = ImageCapture.OutputFileOptions.Builder(
        context.contentResolver,
        MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
        contentValues
    ).build()

    cameraController.takePicture(
        outputOptions,
        executor,
        object : ImageCapture.OnImageSavedCallback {
            override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                val savedUri = outputFileResults.savedUri
                println("Imagen guardada en: $savedUri")

             // Redirige a la actividad para ver fotos miniaturas y
            // seleccionar fotos en patanlla completa
                val intent = Intent(activity, ShowPhotosActivity::class.java)
                activity.startActivity(intent)
            }

            override fun onError(exception: ImageCaptureException) {
                println("Error al guardar la imagen")
            }
        }
    )
}


@Composable
fun Cameracomposable(
    modifier: Modifier = Modifier,
    cameraController: LifecycleCameraController,
    lifeCycle: LifecycleOwner){

    cameraController.bindToLifecycle(lifeCycle)
    AndroidView(modifier = modifier, factory = { context ->
        val previewView = PreviewView(context).apply {
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
        }
        previewView.controller = cameraController
        previewView
    })
}