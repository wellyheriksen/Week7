package com.example.week7

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.week7.soal1.view.PokemonScreen
import com.example.week7.soal2.view.MusicMainScreen
import com.example.week7.ui.theme.Week7Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Week7Theme {

                // --- SOAL 1: Pokemon Card (PokéTracker) ---
                 PokemonScreen()

                // --- SOAL 2: Music Artist (TheAudioDB) ---
                // MusicMainScreen()
            }
        }
    }
}
