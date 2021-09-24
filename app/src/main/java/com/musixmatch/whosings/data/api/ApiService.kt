package com.musixmatch.whosings.data.api

import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET("v2/beers")
    suspend fun getBeers(
        @Query("brewed_after") brewStartDate: String?,
        @Query("brewed_before") brewEndDate: String?,
        @Query("page") page: Int,
        @Query("per_page") chunkSize: Int
    )

}