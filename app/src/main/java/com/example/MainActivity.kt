package com.example

import android.app.PictureInPictureParams
import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.screens.VideoPlayerScreen
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.viewmodel.VideoPlayerViewModel

class MainActivity : ComponentActivity() {
  private var isInPipMode by mutableStateOf(false)

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()
    setContent {
      MyApplicationTheme {
        val viewModel: VideoPlayerViewModel = viewModel()
        VideoPlayerScreen(
            viewModel = viewModel,
            isInPipMode = isInPipMode,
            onEnterPip = { triggerEnterPip() },
            modifier = Modifier.fillMaxSize()
        )
      }
    }
  }

  private fun triggerEnterPip() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
      try {
        val params = PictureInPictureParams.Builder().build()
        enterPictureInPictureMode(params)
      } catch (e: Exception) {
        e.printStackTrace()
      }
    }
  }

  override fun onUserLeaveHint() {
    super.onUserLeaveHint()
    // Auto enter PiP when leaving app
    triggerEnterPip()
  }

  override fun onPictureInPictureModeChanged(
    isInPictureInPictureMode: Boolean,
    newConfig: Configuration
  ) {
    super.onPictureInPictureModeChanged(isInPictureInPictureMode, newConfig)
    isInPipMode = isInPictureInPictureMode
  }
}
