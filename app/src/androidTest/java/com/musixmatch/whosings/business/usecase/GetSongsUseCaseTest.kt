package com.musixmatch.whosings.business.usecase

import android.content.Context
import androidx.test.platform.app.InstrumentationRegistry
import com.musixmatch.whosings.ResourceHelper
import com.musixmatch.whosings.data.api.ApiHelper
import com.musixmatch.whosings.data.repository.MusicRepositoryImpl
import com.musixmatch.whosings.data.storage.ram.VolatileMemoryManager
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject


@OptIn(ExperimentalCoroutinesApi::class)
@HiltAndroidTest
internal class GetSongsUseCaseUITest {

    lateinit var context: Context

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    private lateinit var tracksJson: String
    private lateinit var lyrics1Json: String
    private lateinit var lyrics2Json: String
    private lateinit var lyrics3Json: String

    @Inject
    lateinit var apiHelper: ApiHelper

    @Inject
    lateinit var server: MockWebServer

    @Inject
    lateinit var volatileMemoryManager: VolatileMemoryManager

    lateinit var getSongsUseCase: GetSongsUseCase


    @Before
    fun setup() {
        hiltRule.inject()
        context = InstrumentationRegistry.getInstrumentation().context
        getSongsUseCase = GetSongsUseCase(
            musicRepository = MusicRepositoryImpl(
                apiHelper = apiHelper,
                volatileMemoryManager = volatileMemoryManager
            )
        )
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