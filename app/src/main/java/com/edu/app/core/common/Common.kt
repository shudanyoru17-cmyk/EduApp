package com.edu.app.core.common

import java.util.UUID
import javax.inject.Inject
import javax.inject.Qualifier

@Qualifier @Retention(AnnotationRetention.BINARY) annotation class IoDispatcher
@Qualifier @Retention(AnnotationRetention.BINARY) annotation class DefaultDispatcher

/** Injectable time source — keeps timing deterministic in tests. */
class Clock @Inject constructor() {
    fun nowMillis(): Long = System.currentTimeMillis()
}

/** Injectable id generator. */
class IdGenerator @Inject constructor() {
    fun next(): String = UUID.randomUUID().toString()
}
