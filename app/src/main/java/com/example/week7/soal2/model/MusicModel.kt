package com.example.week7.soal2.model

/**
 * Model Data Artis Musik dari TheAudioDB.
 */
data class Artist(
    val idArtist: String = "",
    val strArtist: String = "",
    val strGenre: String = "",
    val strCountry: String = "",
    val strBiographyEN: String = "",
    val strArtistThumb: String = "",
    val strArtistBanner: String = "",
    val strArtistLogo: String = ""
)

data class ArtistResponse(
    val artists: List<Artist>? = null
)

/**
 * Model Data Album Musik.
 */
data class Album(
    val idAlbum: String = "",
    val idArtist: String = "",
    val strAlbum: String = "",
    val strArtist: String = "",
    val intYearReleased: String = "",
    val strStyle: String = "",
    val strGenre: String = "",
    val strAlbumThumb: String = "",
    val strDescriptionEN: String = ""
)

data class AlbumResponse(
    val album: List<Album>? = null
)

/**
 * Model Data Lagu / Track Musik.
 */
data class Track(
    val idTrack: String = "",
    val idAlbum: String = "",
    val strTrack: String = "",
    val intDuration: String = "",
    val strGenre: String = "",
    val intTrackNumber: String = ""
)

data class TrackResponse(
    val track: List<Track>? = null
)

/**
 * UI State untuk Layar Utama Artis & Album.
 */
sealed class MusicUiState {
    object Loading : MusicUiState()
    data class Success(val artist: Artist?, val albums: List<Album>) : MusicUiState()
    data class Error(val message: String) : MusicUiState()
}

/**
 * UI State untuk Layar Detail Album & Tracks.
 */
sealed class AlbumDetailUiState {
    object Loading : AlbumDetailUiState()
    data class Success(val album: Album, val tracks: List<Track>) : AlbumDetailUiState()
    data class Error(val message: String) : AlbumDetailUiState()
}
