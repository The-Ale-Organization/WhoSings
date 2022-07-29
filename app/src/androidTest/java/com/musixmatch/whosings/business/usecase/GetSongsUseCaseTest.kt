package com.musixmatch.whosings.business.usecase

import android.content.Context
import androidx.test.platform.app.InstrumentationRegistry
import com.musixmatch.whosings.ResourceHelper
import com.musixmatch.whosings.data.api.ApiHelperImpl
import com.musixmatch.whosings.data.api.ApiService
import com.musixmatch.whosings.data.repository.MusicRepositoryImpl
import com.musixmatch.whosings.data.storage.ram.VolatileMemoryManagerImpl
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import okhttp3.OkHttpClient
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


@OptIn(ExperimentalCoroutinesApi::class)
internal class GetSongsUseCaseUITest {

    private val server = MockWebServer()
    lateinit var context: Context

    private lateinit var tracksJson: String
    private lateinit var lyrics1Json: String
    private lateinit var lyrics2Json: String
    private lateinit var lyrics3Json: String

    private val okHttpClient = OkHttpClient
        .Builder()
        .build()

    private val mockRetrofit = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .baseUrl(server.url(""))
        .client(okHttpClient)
        .build()

    private val volatileMemoryManager = VolatileMemoryManagerImpl()

    private val getSongsUseCase = GetSongsUseCase(
        musicRepository = MusicRepositoryImpl(
            apiHelper = ApiHelperImpl(
                apiService = mockRetrofit.create(ApiService::class.java)
            ),
            volatileMemoryManager = volatileMemoryManager
        )
    )


    @Before
    fun setup() {
        context = InstrumentationRegistry.getInstrumentation().context
        tracksJson = ResourceHelper.loadString(context, "tracks.json")
        lyrics1Json = ResourceHelper.loadString(context, "lyrics1.json")
        lyrics2Json = ResourceHelper.loadString(context, "lyrics2.json")
        lyrics3Json = ResourceHelper.loadString(context, "lyrics3.json")
    }

    @After
    fun tearDown() {
        volatileMemoryManager.clear()
    }

    @Test
    fun get_songs_is_working() = runTest(UnconfinedTestDispatcher()) {
        server.enqueue(MockResponse().setBody(tracksJson))
        server.enqueue(MockResponse().setBody(lyrics1Json))
        server.enqueue(MockResponse().setBody(lyrics2Json))
        server.enqueue(MockResponse().setBody(lyrics3Json))

        val songs = getSongsUseCase.getSongs(this)
        assertEquals("First lyrics", songs[0].lyrics)
        assertEquals("Second lyrics", songs[1].lyrics)
        assertEquals("Third lyrics", songs[2].lyrics)
    }
}