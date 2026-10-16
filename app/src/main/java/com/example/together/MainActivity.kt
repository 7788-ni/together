package com.example.together

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.CompositionLocalProvider
import com.example.together.ui.LocalAppContainer
import com.example.together.ui.TogetherNavHost
import com.example.together.ui.theme.TogetherTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val appContainer = AppContainer(applicationContext)
        setContent {
            CompositionLocalProvider(LocalAppContainer provides appContainer) {
                TogetherTheme {
                    TogetherNavHost()
                }
            }
        }
    }
}
