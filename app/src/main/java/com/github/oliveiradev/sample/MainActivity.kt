package com.github.oliveiradev.sample

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import com.github.oliveiradev.kapture.takePhoto

class MainActivity : AppCompatActivity() {

    private val imageResult by lazy { findViewById<ImageView>(R.id.bitmap) }
    private val takePhoto by lazy { findViewById<Button>(R.id.take_photo) }
    private val takePhotoAndSave by lazy { findViewById<Button>(R.id.take_photo_and_save) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        takePhoto.setOnClickListener { takePhoto() }
        takePhotoAndSave.setOnClickListener { takePhotoAndSave() }
    }

    private fun takePhoto() {
        takePhoto {
            collectAsBitmap { bitmap, throwable ->
                bitmap?.let(imageResult::setImageBitmap)
                throwable?.let { error ->
                    Toast.makeText(
                        this@MainActivity,
                        "Something was wrong ${error.message}",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }

    private fun takePhotoAndSave() {
        takePhoto {
            saveAndCollectAsBitmap(this@MainActivity) { bitmap, throwable ->
                bitmap?.let(imageResult::setImageBitmap)
                throwable?.let { error ->
                    Toast.makeText(
                        this@MainActivity,
                        "Something was wrong ${error.message}",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }
}
