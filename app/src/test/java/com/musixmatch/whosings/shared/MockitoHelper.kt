package com.musixmatch.whosings.shared

import org.mockito.Mockito

object MockitoHelper {

    @Suppress("UNCHECKED_CAST")
    fun <T> uninitialized(): T =  null as T

    fun <T> anyObject(): T {
        Mockito.any<T>()
        return uninitialized()
    }

    fun <T> eqObject(value: T): T {
        Mockito.eq(value)
        return uninitialized()
    }

}