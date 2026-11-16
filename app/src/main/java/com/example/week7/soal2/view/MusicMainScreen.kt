package com.example.week7.soal2.view

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.week7.soal2.viewmodel.MusicViewModel

/**
 * Screen Utama Soal 2 (Menghubungkan Halaman Artis dan Detail Album).
 */
@Composable
fun MusicMainScreen(
    viewModel: MusicViewModel = viewModel()
) {
    val artistUiState by viewModel.artistUiState.collectAsState()
    val albumDetailUiState by viewModel.albumDetailUiState.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()
    val selectedAlbum by viewModel.selectedAlbum.collectAsState()

    if (selectedAlbum == null) {
        MusicArtistScreen(
            uiState = artistUiState,
            searchQuery = searchQuery,
            onSearchQueryChange = { viewModel.onSearchQueryChange(it) },
            onAlbumClick = { viewModel.selectAlbum(it) },
            onRetry = { viewModel.retryArtistScreen() },
            onToggleError = { viewModel.triggerErrorTest() }
        )
    } else {
        AlbumDetailScreen(
            uiState = albumDetailUiState,
            onBack = { viewModel.backToArtistScreen() },
            onRetry = { viewModel.retryAlbumDetail() }
        )
    }
}
