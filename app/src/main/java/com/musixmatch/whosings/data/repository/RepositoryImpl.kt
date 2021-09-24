package com.musixmatch.whosings.data.repository

import com.musixmatch.whosings.data.api.ApiHelper
import javax.inject.Inject

class RepositoryImpl @Inject constructor(
    private val apiHelper: ApiHelper
) : Repository {

}