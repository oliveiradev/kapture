package com.github.oliveiradev.sample

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.widget.ImageView
import android.widget.Toast
import com.github.oliveiradev.kapture.takePhoto

class MainActivity : AppCompatActivity() {

    private val imageResult by lazy { findViewById<ImageView>(R.id.bitmap) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        takePhoto { result ->
            result.collectAsBitmap { bitmap, throwable ->
                bitmap?.let(imageResult::setImageBitmap)
                throwable?.let { error ->
                    Toast.makeText(
                        this,
                        "Something was wrong ${error.message}",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }
}
