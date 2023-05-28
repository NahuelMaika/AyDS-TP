package ayds.apolo.songinfo.home.model.repository.local.spotify

import ayds.apolo.songinfo.home.model.entities.EmptySong
import ayds.apolo.songinfo.home.model.entities.Song
import ayds.apolo.songinfo.home.model.entities.SpotifySong

interface SpotifyCacheStorage{

    fun getFromCache(term: String): Song

    fun saveInCache(term: String,spotifySong: SpotifySong)

}
internal class SpotifyCacheStorageImpl: SpotifyCacheStorage {
    private val theCache = mutableMapOf<String, SpotifySong>()

    override fun getFromCache(term: String): Song {
        val spotifySong = theCache[term]
        if (spotifySong != null)
            spotifySong.isCacheStored = true

        return spotifySong ?: EmptySong
    }
    override fun saveInCache(term: String,spotifySong: SpotifySong){
        theCache[term] = spotifySong
    }
}