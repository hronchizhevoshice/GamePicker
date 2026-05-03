package com.example.gamepicker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.example.gamepicker.presentation.navigation.AppNavigation
import com.example.gamepicker.presentation.theme.GamePickerTheme
import com.example.gamepicker.presentation.theme.Teal

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            GamePickerTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Teal
                ) {
                    AppNavigation()
                }
            }
        }
    }
}