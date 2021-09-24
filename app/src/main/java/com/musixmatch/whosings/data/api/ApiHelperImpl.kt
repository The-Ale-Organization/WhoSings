package com.musixmatch.whosings.data.api

import javax.inject.Inject


class ApiHelperImpl @Inject constructor(
    private val apiService: ApiService
) : ApiHelper {
}