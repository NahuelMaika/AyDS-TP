package ayds.apolo.songinfo.home.model.repository

import ayds.apolo.songinfo.home.model.entities.EmptySong
import ayds.apolo.songinfo.home.model.entities.Song
import ayds.apolo.songinfo.home.model.entities.SpotifySong
import ayds.apolo.songinfo.home.model.repository.external.spotify.SongBroker
import ayds.apolo.songinfo.home.model.repository.external.spotify.SpotifyTrackService
import ayds.apolo.songinfo.home.model.repository.local.spotify.SpotifyLocalStorage
import ayds.apolo.songinfo.home.model.repository.local.spotify.cache.SongCache
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import net.bytebuddy.pool.TypePool.Empty
import org.junit.Assert
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test


class SongRepositoryTest {

    private val songCache = mockk<SongCache>(relaxUnitFun = true)
    private val songLocalStorage = mockk<SpotifyLocalStorage>(relaxUnitFun = true)
    private val songBroker = mockk<SongBroker>(relaxUnitFun = true)

    private val songRepository: SongRepository by lazy {
        SongRepositoryImpl(songCache,songLocalStorage,songBroker)
    }

    @Test
    fun `given a term return Song from cache`() {
        val song: SpotifySong = mockk()

        every { songCache.getSongByTerm("artist") } returns song
        val result = songRepository.getSongByTerm("artist")

        assertEquals(result,song)
        assertTrue(song.isCacheStored)

        verify {songRepository.getSongByTerm("artist")}

    }

    @Test
    fun `given a term return Song from local storage`() {
        val song: SpotifySong = mockk()

        every { songCache.getSongByTerm("artist") } returns null
        every { songLocalStorage.getSongByTerm("artist") } returns song

        val result = songRepository.getSongByTerm("artist")

        assertEquals(result,song)
        assertTrue(song.isLocallyStored)

        verify {songLocalStorage.getSongByTerm("artist") }
        verify {songCache.insertSong("artist",song) }

    }

    @Test
    fun `given a term return Song from broker`() {
        val song: SpotifySong = mockk()

        every { songCache.getSongByTerm("artist") } returns null
        every { songLocalStorage.getSongByTerm("artist") } returns null
        every { songBroker.getSong("artist") } returns song

        val result = songRepository.getSongByTerm("artist")

        assertEquals(result,song)

        verify { songBroker.getSong("artist") }
        verify {songCache.insertSong("artist",song) }
        verify { songLocalStorage.insertSong("artist",song)}

    }

    @Test
    fun `given a term return EmptySong`() {
        val song: SpotifySong = mockk()

        every { songCache.getSongByTerm("artist") } returns null
        every { songLocalStorage.getSongByTerm("artist") } returns null
        every { songBroker.getSong("artist") } returns null

        val result = songRepository.getSongByTerm("artist")

        assertEquals(result,EmptySong)

    }

}