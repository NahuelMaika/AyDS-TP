package ayds.apolo.songinfo.home.model

import ayds.apolo.songinfo.home.model.repository.SongRepository
import ayds.apolo.songinfo.home.model.repository.SongRepositoryImpl
import ayds.apolo.songinfo.home.model.repository.external.spotify.SpotifyModule
import ayds.apolo.songinfo.home.model.repository.external.spotify.SpotifyTrackService
import ayds.apolo.songinfo.home.model.repository.local.spotify.sqldb.ResultSetToSpotifySongMapper
import ayds.apolo.songinfo.home.model.repository.local.spotify.sqldb.ResultSetToSpotifySongMapperImpl
import ayds.apolo.songinfo.home.model.repository.local.spotify.sqldb.SpotifySqlDBImpl
import ayds.apolo.songinfo.home.model.repository.local.spotify.sqldb.SpotifySqlQueries
import ayds.apolo.songinfo.home.model.repository.local.spotify.sqldb.SpotifySqlQueriesImpl

object HomeModelInjector {

  private val spotifySqlQueries: SpotifySqlQueries = SpotifySqlQueriesImpl()
  private val resultSetToSpotifySongMapper: ResultSetToSpotifySongMapper = ResultSetToSpotifySongMapperImpl()
  private val spotifySqlDB: SpotifySqlDBImpl = SpotifySqlDBImpl(spotifySqlQueries, resultSetToSpotifySongMapper)
  private val spotifyTrackService: SpotifyTrackService = SpotifyModule.spotifyTrackService

  private val repository: SongRepository = SongRepositoryImpl(spotifySqlDB, spotifyTrackService)

  val homeModel: HomeModel = HomeModelImpl(repository)
}