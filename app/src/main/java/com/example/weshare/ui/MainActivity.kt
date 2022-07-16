package com.example.weshare.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.weshare.ui.screens.NavGraphs
import com.example.weshare.ui.theme.WeShareTheme
import com.ramcosta.composedestinations.DestinationsNavHost
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WeShareTheme {
               DestinationsNavHost(navGraph = NavGraphs.root)
            }
        }
    }
}
