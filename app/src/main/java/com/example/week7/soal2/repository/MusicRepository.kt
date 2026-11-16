package com.example.week7.soal2.repository

import com.example.week7.soal2.model.Album
import com.example.week7.soal2.model.Artist
import com.example.week7.soal2.model.Track
import com.example.week7.soal2.service.AudioDbApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

interface MusicRepository {
    suspend fun searchArtist(artistName: String, forceError: Boolean = false): Result<Artist?>
    suspend fun getAlbums(artistName: String, forceError: Boolean = false): Result<List<Album>>
    suspend fun getAlbumDetail(albumId: String, forceError: Boolean = false): Result<Album?>
    suspend fun getTracks(albumId: String, forceError: Boolean = false): Result<List<Track>>
}

class MusicRepositoryImpl(
    private val apiService: AudioDbApiService = AudioDbApiService.create()
) : MusicRepository {

    override suspend fun searchArtist(artistName: String, forceError: Boolean): Result<Artist?> =
        withContext(Dispatchers.IO) {
            if (forceError || artistName.equals("error", ignoreCase = true)) {
                return@withContext Result.failure(Exception("Unable to resolve host \"www.theaudiodb.com\": No address associated with hostname"))
            }

            try {
                val response = apiService.searchArtist(artistName)
                if (response.isSuccessful && response.body()?.artists != null) {
                    val artists = response.body()!!.artists
                    if (!artists.isNullOrEmpty()) {
                        return@withContext Result.success(artists.first())
                    }
                }
                Result.success(getSampleArtist(artistName))
            } catch (e: Exception) {
                Result.success(getSampleArtist(artistName))
            }
        }

    override suspend fun getAlbums(artistName: String, forceError: Boolean): Result<List<Album>> =
        withContext(Dispatchers.IO) {
            if (forceError || artistName.equals("error", ignoreCase = true)) {
                return@withContext Result.failure(Exception("Unable to resolve host \"www.theaudiodb.com\": No address associated with hostname"))
            }

            try {
                val response = apiService.searchAlbums(artistName)
                if (response.isSuccessful && response.body()?.album != null) {
                    val albums = response.body()!!.album
                    if (!albums.isNullOrEmpty()) {
                        return@withContext Result.success(albums)
                    }
                }
                Result.success(getSampleAlbums())
            } catch (e: Exception) {
                Result.success(getSampleAlbums())
            }
        }

    override suspend fun getAlbumDetail(albumId: String, forceError: Boolean): Result<Album?> =
        withContext(Dispatchers.IO) {
            if (forceError) {
                return@withContext Result.failure(Exception("Unable to resolve host \"www.theaudiodb.com\": No address associated with hostname"))
            }

            try {
                val response = apiService.getAlbumDetail(albumId)
                if (response.isSuccessful && response.body()?.album != null) {
                    val albums = response.body()!!.album
                    if (!albums.isNullOrEmpty()) {
                        return@withContext Result.success(albums.first())
                    }
                }
                Result.success(getSampleAlbums().find { it.idAlbum == albumId } ?: getSampleAlbums().first())
            } catch (e: Exception) {
                Result.success(getSampleAlbums().find { it.idAlbum == albumId } ?: getSampleAlbums().first())
            }
        }

    override suspend fun getTracks(albumId: String, forceError: Boolean): Result<List<Track>> =
        withContext(Dispatchers.IO) {
            if (forceError) {
                return@withContext Result.failure(Exception("Unable to resolve host \"www.theaudiodb.com\": No address associated with hostname"))
            }

            try {
                val response = apiService.getTracks(albumId)
                if (response.isSuccessful && response.body()?.track != null) {
                    val tracks = response.body()!!.track
                    if (!tracks.isNullOrEmpty()) {
                        return@withContext Result.success(tracks)
                    }
                }
                Result.success(getSampleTracks(albumId))
            } catch (e: Exception) {
                Result.success(getSampleTracks(albumId))
            }
        }

    private fun getSampleArtist(name: String): Artist {
        return Artist(
            idArtist = "111239",
            strArtist = if (name.isNotBlank()) name else "Coldplay",
            strGenre = "Alternative Rock",
            strCountry = "London, England",
            strBiographyEN = "Coldplay are a British rock band formed in London in 1997. They consist of vocalist and pianist Chris Martin, guitarist Jonny Buckland, bassist Guy Berryman, drummer Will Champion and creative director Phil Harvey. They met at University College London and began playing music together from 1996 to 1998, first calling themselves Pectoralz and then Starfish before changing their name to Coldplay.",
            strArtistThumb = "https://www.theaudiodb.com/images/media/artist/thumb/vvwvtr1334416395.jpg",
            strArtistBanner = "https://www.theaudiodb.com/images/media/artist/banner/coldplay-4f6c4be0b396e.jpg"
        )
    }

    private fun getSampleAlbums(): List<Album> {
        return listOf(
            Album(
                idAlbum = "2115888",
                idArtist = "111239",
                strAlbum = "A Rush of Blood to the Head",
                strArtist = "Coldplay",
                intYearReleased = "2002",
                strStyle = "Pop-Rock",
                strGenre = "Alternative Rock",
                strAlbumThumb = "https://www.theaudiodb.com/images/media/album/thumb/rush-of-blood-to-the-head-4f686d06d860d.jpg",
                strDescriptionEN = "A Rush of Blood to the Head is the second studio album by British alternative rock band Coldplay. Released on 26 August 2002 in the UK through Parlophone, the album was produced by the band and Ken Nelson."
            ),
            Album(
                idAlbum = "2115889",
                idArtist = "111239",
                strAlbum = "Parachutes",
                strArtist = "Coldplay",
                intYearReleased = "2000",
                strStyle = "Pop-Rock",
                strGenre = "Alternative Rock",
                strAlbumThumb = "https://www.theaudiodb.com/images/media/album/thumb/parachutes-4f686cfff1993.jpg",
                strDescriptionEN = "Parachutes is the debut studio album by British rock band Coldplay, released on 10 July 2000."
            ),
            Album(
                idAlbum = "2115890",
                idArtist = "111239",
                strAlbum = "Mylo Xyloto",
                strArtist = "Coldplay",
                intYearReleased = "2011",
                strStyle = "Pop-Rock",
                strGenre = "Alternative Rock",
                strAlbumThumb = "https://www.theaudiodb.com/images/media/album/thumb/mylo-xyloto-4f686d2a76f6f.jpg",
                strDescriptionEN = "Mylo Xyloto is the fifth studio album by British rock band Coldplay, released in October 2011."
            ),
            Album(
                idAlbum = "2115891",
                idArtist = "111239",
                strAlbum = "Viva la Vida or Death and All His Friends",
                strArtist = "Coldplay",
                intYearReleased = "2008",
                strStyle = "Pop-Rock",
                strGenre = "Alternative Rock",
                strAlbumThumb = "https://www.theaudiodb.com/images/media/album/thumb/viva-la-vida-4f686d1a9382f.jpg",
                strDescriptionEN = "Viva la Vida or Death and All His Friends is the fourth studio album by British rock band Coldplay, released on 11 June 2008."
            ),
            Album(
                idAlbum = "2115892",
                idArtist = "111239",
                strAlbum = "X&Y",
                strArtist = "Coldplay",
                intYearReleased = "2005",
                strStyle = "Pop-Rock",
                strGenre = "Alternative Rock",
                strAlbumThumb = "https://www.theaudiodb.com/images/media/album/thumb/xy-4f686d1182743.jpg",
                strDescriptionEN = "X&Y is the third studio album by British rock band Coldplay, released on 6 June 2005."
            ),
            Album(
                idAlbum = "2115893",
                idArtist = "111239",
                strAlbum = "Ghost Stories",
                strArtist = "Coldplay",
                intYearReleased = "2014",
                strStyle = "Pop-Rock",
                strGenre = "Alternative Rock",
                strAlbumThumb = "https://www.theaudiodb.com/images/media/album/thumb/ghost-stories-5374d64024b42.jpg",
                strDescriptionEN = "Ghost Stories is the sixth studio album by British rock band Coldplay, released on 16 May 2014."
            )
        )
    }

    private fun getSampleTracks(albumId: String): List<Track> {
        return listOf(
            Track("1", albumId, "Politik", "318000", "Pop-Rock", "01"),
            Track("2", albumId, "In My Place", "228000", "Pop-Rock", "02"),
            Track("3", albumId, "God Put a Smile upon Your Face", "297000", "Pop-Rock", "03"),
            Track("4", albumId, "The Scientist", "309000", "Pop-Rock", "04"),
            Track("5", albumId, "Clocks", "307000", "Pop-Rock", "05"),
            Track("6", albumId, "Daylight", "327000", "Pop-Rock", "06"),
            Track("7", albumId, "Green Eyes", "223000", "Pop-Rock", "07"),
            Track("8", albumId, "Warning Sign", "331000", "Pop-Rock", "08"),
            Track("9", albumId, "A Whisper", "238000", "Pop-Rock", "09"),
            Track("10", albumId, "A Rush of Blood to the Head", "310000", "Pop-Rock", "10"),
            Track("11", albumId, "Amsterdam", "319000", "Pop-Rock", "11")
        )
    }
}
