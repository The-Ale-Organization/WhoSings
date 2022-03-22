package com.musixmatch.whosings.common.presentation.navigation

import androidx.navigation.Navigation
import dagger.hilt.android.scopes.ActivityRetainedScoped
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.shareIn
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

    suspend fun navigate(route: Route) =
        withContext(Dispatchers.Main) {
            navigationChangesChannel.send(route)
    }
}

