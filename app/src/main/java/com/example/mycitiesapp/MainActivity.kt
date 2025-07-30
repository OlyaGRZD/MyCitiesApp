package com.example.mycitiesapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.mycitiesapp.presentation.ui.MainScreen
import com.example.mycitiesapp.presentation.ui.theme.CityListTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CityListTheme {
                MainScreen()
            }
        }
    }
}


