package com.example.week7.soal2.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.week7.soal2.model.Album
import com.example.week7.soal2.model.AlbumDetailUiState
import com.example.week7.soal2.model.MusicUiState
import com.example.week7.soal2.repository.MusicRepository
import com.example.week7.soal2.repository.MusicRepositoryImpl
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MusicViewModel(
    private val repository: MusicRepository = MusicRepositoryImpl()
) : ViewModel() {

    private val _artistUiState = MutableStateFlow<MusicUiState>(MusicUiState.Loading)
    val artistUiState: StateFlow<MusicUiState> = _artistUiState.asStateFlow()

    private val _albumDetailUiState = MutableStateFlow<AlbumDetailUiState>(AlbumDetailUiState.Loading)
    val albumDetailUiState: StateFlow<AlbumDetailUiState> = _albumDetailUiState.asStateFlow()

    private val _searchQuery = MutableStateFlow("Coldplay")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _selectedAlbum = MutableStateFlow<Album?>(null)
    val selectedAlbum: StateFlow<Album?> = _selectedAlbum.asStateFlow()

    init {
        loadArtistAndAlbums("Coldplay")
    }

    fun onSearchQueryChange(newQuery: String) {
        _searchQuery.value = newQuery
        loadArtistAndAlbums(newQuery)
    }

    fun loadArtistAndAlbums(artistName: String = _searchQuery.value, forceError: Boolean = false) {
        viewModelScope.launch {
            _artistUiState.value = MusicUiState.Loading
            val artistResult = repository.searchArtist(artistName, forceError)
            val albumResult = repository.getAlbums(artistName, forceError)

            if (forceError || artistName.equals("error", ignoreCase = true)) {
                _artistUiState.value = MusicUiState.Error("Unable to resolve host \"www.theaudiodb.com\": No address associated with hostname")
                return@launch
            }

            artistResult.onSuccess { artist ->
                albumResult.onSuccess { albums ->
                    _artistUiState.value = MusicUiState.Success(artist, albums)
                }.onFailure { err ->
                    _artistUiState.value = MusicUiState.Error(err.message ?: "Terjadi kesalahan memuat album")
                }
            }.onFailure { err ->
                _artistUiState.value = MusicUiState.Error(err.message ?: "Terjadi kesalahan memuat artis")
            }
        }
    }

    fun selectAlbum(album: Album) {
        _selectedAlbum.value = album
        loadAlbumDetailAndTracks(album.idAlbum)
    }

    fun loadAlbumDetailAndTracks(albumId: String, forceError: Boolean = false) {
        viewModelScope.launch {
            _albumDetailUiState.value = AlbumDetailUiState.Loading
            val detailResult = repository.getAlbumDetail(albumId, forceError)
            val tracksResult = repository.getTracks(albumId, forceError)

            if (forceError) {
                _albumDetailUiState.value = AlbumDetailUiState.Error("Unable to resolve host \"www.theaudiodb.com\": No address associated with hostname")
                return@launch
            }

            detailResult.onSuccess { album ->
                if (album != null) {
                    tracksResult.onSuccess { tracks ->
                        _albumDetailUiState.value = AlbumDetailUiState.Success(album, tracks)
                    }.onFailure { err ->
                        _albumDetailUiState.value = AlbumDetailUiState.Error(err.message ?: "Gagal memuat track list")
                    }
                } else {
                    _albumDetailUiState.value = AlbumDetailUiState.Error("Album tidak ditemukan")
                }
            }.onFailure { err ->
                _albumDetailUiState.value = AlbumDetailUiState.Error(err.message ?: "Gagal memuat detail album")
            }
        }
    }

    fun backToArtistScreen() {
        _selectedAlbum.value = null
    }

    fun retryArtistScreen() {
        loadArtistAndAlbums(_searchQuery.value, forceError = false)
    }

    fun retryAlbumDetail() {
        _selectedAlbum.value?.let { loadAlbumDetailAndTracks(it.idAlbum, forceError = false) }
    }

    fun triggerErrorTest() {
        loadArtistAndAlbums(_searchQuery.value, forceError = true)
    }
}
