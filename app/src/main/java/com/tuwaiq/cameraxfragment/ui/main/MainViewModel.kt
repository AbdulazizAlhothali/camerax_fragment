package com.tuwaiq.cameraxfragment.ui.main

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.net.Uri
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.view.PreviewView
import androidx.core.content.ContextCompat
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class MainViewModel(application: Application) : AndroidViewModel(application) {
    //val captureBtn: MutableLiveData<Button> = MutableLiveData(binding)
    //var cameraView: PreviewView


    @SuppressLint("StaticFieldLeak")
    private val context = getApplication<Application>().applicationContext
    private var imageCapture: ImageCapture? = null
    private lateinit var outputDirectory: File

    fun takePhoto() {
        viewModelScope.launch(Dispatchers.Main) {
            // Get a stable reference of the modifiable image capture use case
            val imageCapture = imageCapture ?: return@launch

            // Create time-stamped output file to hold the image
            val photoFile = File(
                outputDirectory,
                SimpleDateFormat(
                    MainFragment.FILENAME_FORMAT, Locale.US
                ).format(System.currentTimeMillis()) + ".jpg")

            // Create output options object which contains file + metadata
            val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()


            // Set up image capture listener, which is triggered after photo has
            // been taken
            imageCapture.takePicture(
                outputOptions, ContextCompat.getMainExecutor(context), object : ImageCapture.OnImageSavedCallback {
                    override fun onError(exc: ImageCaptureException) {
                        Log.e(MainFragment.TAG, "Photo capture failed: ${exc.message}", exc)
                    }

                    override fun onImageSaved(output: ImageCapture.OutputFileResults) {
                        val savedUri = Uri.fromFile(photoFile)
                        val msg = "Photo capture succeeded: $savedUri"
                        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
                        Log.d(MainFragment.TAG, msg)
                        /*val i = Intent(this@MainActivity, ImagePreview::class.java)
                        i.putExtra("data",savedUri)
                        startActivity(i)*/
                    }
                })

        }



    }


}