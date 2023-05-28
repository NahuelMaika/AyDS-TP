package ayds.apolo.songinfo.home.model.repository.wikipedia

import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory

object WikipediaModule {
    private const val WIKIPEDIA_URL = "https://en.wikipedia.org/w/"
    private val wikipediaApiRetrofit: Retrofit? = Retrofit.Builder()
    .baseUrl(WIKIPEDIA_URL)
    .addConverterFactory(ScalarsConverterFactory.create())
    .build()

    private val wikipediaAPI = wikipediaApiRetrofit!!.create(WikipediaAPI::class.java)

    val wikipediaService: WikipediaService = WikipediaServiceImpl(this.wikipediaAPI)
}