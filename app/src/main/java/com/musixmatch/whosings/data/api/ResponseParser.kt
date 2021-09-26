package com.musixmatch.whosings.data.api

import com.musixmatch.whosings.business.error.ErrorResponseException
import com.musixmatch.whosings.business.error.MissingResponseFieldException
import com.musixmatch.whosings.data.model.Response

class ResponseParser {

    fun <T> parse(response: Response<T>): T {
        response.message?.header?.let {
            if (it.statusCode == STATUS_CODE_OK) {
                return response.message.body ?: throw MissingResponseFieldException("body")
            } else {
                throw ErrorResponseException(it.statusCode.toString())
            }
        } ?: throw MissingResponseFieldException("header")
    }
}

const val STATUS_CODE_OK = 200