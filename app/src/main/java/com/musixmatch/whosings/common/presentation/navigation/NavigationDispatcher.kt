package com.musixmatch.whosings.common.presentation.navigation

import android.util.Log
import androidx.navigation.Navigation
import dagger.hilt.android.scopes.ActivityRetainedScoped
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
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
            Log.d("NavigationDispatcher","launchWhenStarted emitted n. $i on coroutine ${System.identityHashCode(this)}")
            i++
        }
    }

    val flow2: Flow<String> = flow {
        var i = 0
        while (true) {
            delay(2000)
            emit("n. $i")
            Log.d("NavigationDispatcher","repeatOnLifecycle emitted n. $i on coroutine ${System.identityHashCode(this)}")
            i++
        }
    }

    suspend fun navigate(route: Route) =
        withContext(Dispatchers.Main) {
            navigationChangesChannel.send(route)
    }
}

