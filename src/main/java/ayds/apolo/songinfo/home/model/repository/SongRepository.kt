package ayds.apolo.songinfo.home.model.repository

import ayds.apolo.songinfo.home.model.entities.EmptySong
import ayds.apolo.songinfo.home.model.entities.Song
import ayds.apolo.songinfo.home.model.entities.SpotifySong
import ayds.apolo.songinfo.home.model.repository.external.spotify.SpotifyTrackService
import ayds.apolo.songinfo.home.model.repository.local.spotify.SpotifyCacheStorage
import ayds.apolo.songinfo.home.model.repository.local.spotify.SpotifyCacheStorageImpl
import ayds.apolo.songinfo.home.model.repository.local.spotify.sqldb.SpotifySqlDBImpl
import com.google.gson.Gson
import com.google.gson.JsonObject
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.io.IOException
interface SongRepository{
    fun getSongByTerm(term: String): Song
}

internal class SongRepositoryImpl(private val spotifySqlDBImpl:SpotifySqlDBImpl,
                                  private val spotifyTrackService:SpotifyTrackService)
:SongRepository {

    private val cache: SpotifyCacheStorage = SpotifyCacheStorageImpl()
    private val wikipediaService: WikipediaService = WikipediaServiceImpl()

   override fun getSongByTerm(term: String): Song {
        var song = cache.getFromCache(term)

        if(song == EmptySong){
            song = getFromDB(term)

            if(song == EmptySong){
                cache.saveInCache(term, song as SpotifySong)
            }
        }

        if (song == EmptySong){
            song = getFromSpotifyService(term)
        }

        if(song == EmptySong){
            song = wikipediaService.getFromWikipediaService(term)
        }

        return song
    }

    private fun getFromDB(term: String): Song{
        val spotifySong = spotifySqlDBImpl.getSongByTerm(term)
        if (spotifySong != null) {
            spotifySong.isLocallyStored = true
        }
        return spotifySong ?: EmptySong
    }


    private fun getFromSpotifyService(term: String): Song{
        val spotifySong = spotifyTrackService.getSong(term)
        if (spotifySong != null)
            spotifySqlDBImpl.insertSong(term,spotifySong)

        return spotifySong ?: EmptySong
    }

}