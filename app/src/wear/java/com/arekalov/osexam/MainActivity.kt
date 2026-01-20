package com.arekalov.osexam

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.wear.compose.material3.AppScaffold
import androidx.wear.compose.material3.ScreenScaffold
import com.arekalov.osexam.ui.AppRoot
import com.arekalov.osexam.ui.theme.OsexamTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            OsexamTheme {
                WearApp()
            }
        }
    }
}

@Composable
fun WearApp() {
    AppScaffold {
        ScreenScaffold(
            timeText = {}
        ) {
            AppRoot()
        }
    }
}
