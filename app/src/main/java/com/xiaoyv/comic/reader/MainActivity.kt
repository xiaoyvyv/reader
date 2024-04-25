@file:OptIn(ExperimentalMaterial3WindowSizeClassApi::class)

package com.xiaoyv.comic.reader

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import com.google.accompanist.adaptive.calculateDisplayFeatures
import com.xiaoyv.comic.reader.ui.theme.ComicReaderTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ComicReaderTheme {
                val windowSize = calculateWindowSizeClass(this)
                val displayFeatures = calculateDisplayFeatures(this)

                MainPage(
                    windowSize = windowSize,
                    displayFeatures = displayFeatures,
                )
            }
        }
    }
}