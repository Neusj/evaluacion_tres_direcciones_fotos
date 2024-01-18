package com.example.evaluacion_tres.fotos

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import com.example.evaluacion_tres.R

class ShowPhotosActivity : AppCompatActivity() {
    private val pickMedia =
        registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            if (uri != null) {
                imgView.setImageURI(uri)
            }
        }

    lateinit var btnImage: Button
    lateinit var imgView: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show_photos)

        btnImage = findViewById(R.id.btnImage)
        imgView = findViewById(R.id.imgView)
        pickMedia.launch(PickVisualMediaRequest())
        btnImage.setOnClickListener {
            pickMedia.launch(PickVisualMediaRequest())
        }
    }
}

