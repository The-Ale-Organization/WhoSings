package com.musixmatch.whosings.business.util

import javax.inject.Inject
import javax.inject.Named

interface DummyInjected {

    fun someMethod()

}

class DummyInjectedClassImpl @Inject constructor(
    @Named("dummyString") val dummyString: String
) : DummyInjected {

    constructor() : this("default")

    init {
        println("DummyInjectedClassImpl init: $dummyString")
    }

    override fun someMethod() {
        println("DummyInjectedClassImpl someMethod")
    }
}