package com.musixmatch.whosings.common.presentation.navigation

import android.util.Log
import androidx.navigation.Navigation
import dagger.hilt.android.scopes.ActivityRetainedScoped
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@ActivityRetainedScoped
class NavigationDispatcher @Inject constructor() {

    private val navigationChangesChannel = Channel<Route>()

    /**
     * A Flow emitting [Navigation] changes only once to one single collector. If you need to collect from multiple
     * collectors use [shareIn] on the Flow and share the resulting [SharedFlow].
     */
    val navigationChangesFlow: Flow<Route> = navigationChangesChannel.receiveAsFlow()

    val flow1: Flow<String> = callbackFlow {
        var i = 0
        while (true) {
            delay(2000)
            trySend("n. $i")
            Log.d(
                "NavigationDispatcher",
                "launchWhenStarted emitted n. $i on coroutine ${System.identityHashCode(this)}"
            )
            i++
        }
    }

    val flow2: Flow<String> = flow {
        var i = 0
        while (true) {
            delay(2000)
            emit("n. $i")
            Log.d(
                "NavigationDispatcher",
                "repeatOnLifecycle emitted n. $i on coroutine ${System.identityHashCode(this)}"
            )
            i++
        }
    }

    val flowLaunchWhenStarted: Flow<String> = callbackFlow {
        val tag = "launchWhenStarted"
        val producer = Producer(tag) {
            trySend("")
        }
        producer.produce()

        awaitClose {
            println("stopping producer $tag")
            producer.stop()
        }
    }

    val flowRepeatOnLifecycle: Flow<String> = callbackFlow {
        val tag = "repeatOnLifecycle"
        val producer = Producer(tag) {
            trySend("")
        }
        producer.produce()

        awaitClose {
            println("stopping producer $tag")
            producer.stop()
        }
    }

    suspend fun navigate(route: Route) =
        withContext(Dispatchers.Main) {
            navigationChangesChannel.send(route)
        }

    private class Producer(private val tag: String, private val callback: (String) -> Unit) {

        private var job: Job? = null

        fun produce() {
            job = GlobalScope.launch {
                var i = 0
                while (true) {
                    delay(2000)
                    callback("n. $i")
                    Log.d(
                        "NavigationDispatcher",
                        "Producer $tag emitted n. $i on coroutine ${System.identityHashCode(this)}"
                    )
                    i++
                }
            }
        }

        fun stop() {
            job?.cancel()
        }

    }
}

