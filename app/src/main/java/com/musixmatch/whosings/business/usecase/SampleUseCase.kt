package com.musixmatch.whosings.business.usecase

import com.musixmatch.whosings.data.repository.Repository
import javax.inject.Inject

class SampleUseCase @Inject constructor(
    private val repository: Repository
) {

}